package com.example.trabalhotcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhotcc.dao.ConsultaDAO;
import com.example.trabalhotcc.modelo.Consulta;
import com.example.trabalhotcc.modelo.Instituicao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class consultaActivity extends AppCompatActivity {

    // declarar objeto para manipular uma Pessoa. Inicialmente null para inclusão
    private Consulta consulta = null;

    // declarar objeto para manipular o banco de dados
    private ConsultaDAO acessaBanco;
    // declara os Componentes para interagir com a tela
    private Spinner spinnerPais;

    // declarar objetos para interagir com a tela
    private TextView editTextMedico;
    private TextView editTextDiagnostico;
    private TextView editTextPrescricao;
    private int spinner;
    private Intent it;
    private int idEdicao;
    private Button btSalvar;
    private Button btExcluir;
    private ImageButton imageButton;

    // declara o adaptador
    private ArrayAdapter<String> adaptadorSpinnerPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        acessaBanco = new ConsultaDAO(getBaseContext());
        // vincula Componentes com Views da tela
        spinnerPais         = findViewById(R.id.spinner);
        editTextMedico      = findViewById(R.id.editTextMedico);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        editTextPrescricao  = findViewById(R.id.editTextPrescricao);
        btSalvar            = findViewById(R.id.btSalvar);
        btExcluir           = findViewById(R.id.btExcluir);
        imageButton         = findViewById(R.id.imageButton);


        //instancia o adaptador
        adaptadorSpinnerPais = new ArrayAdapter<>(
                getBaseContext(),                   // 1 - contexto
                android.R.layout.simple_list_item_1 // 2 - modelo de linha (texto)
        );
        // vincula o adaptador com o Spinner
        spinnerPais.setAdapter(adaptadorSpinnerPais);

        adaptadorSpinnerPais.add("Selecione  instituição");
        ///////////////////////////////////////////////////////////////////////
        // Cria uma fila para envio de mensagens por Volley
        RequestQueue filaEnviadoraDeMensagens = Volley.newRequestQueue(getBaseContext());
        //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig. Procurar por IPv4
        String url = "http://192.168.0.108:3000/instituicao/?id=1";// 192.168.0.108 em casa no if 31.6

        // parâmetros para enviar por POST usando um Map
        final Map<String, String> parametrosPOST = new HashMap<>();

        // cria a requisição de mensagem e tratamento de resposta
        JsonArrayRequest requisicao = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Instituicao c = new Instituicao();
                                JSONObject o = (JSONObject) response.get(i);
                                try {
                                    c.setId(o.getInt("id"));
                                    c.setNome(o.getString("nome"));
                                    c.setId_local(o.getInt("id_local"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adaptadorSpinnerPais.add(c.getNome());
                            }

                        } catch (org.json.JSONException e) {
                            Log.d("TAG_EXEMPLO",
                                    "Erro ao converter resultado do Json: " + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG_EXEMPLO",
                                "Erro encontrado ao tentar enviar mensagem", error);
                    }
                }
        );
        filaEnviadoraDeMensagens.add(requisicao);
        ///////////////////////////////////////////////////////////////////////
        // Pega Intent para descobrir se a ação é Edição ou Inclusão
        it = getIntent();
        idEdicao = it.getIntExtra("idEdicao", -1);
        // instanciar objetos

        if (idEdicao > 0) {
            // busca o cliente no banco de dados com o ID para edição

            acessaBanco.buscarUm(idEdicao, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    // adiciona dados nas Views
                    try {
                        editTextDiagnostico.setText(response.getString("diagnostico"));
                        editTextMedico.setText(response.getString("nome_medico"));
                        editTextPrescricao.setText(response.getString("prescricao"));
                        spinnerPais.setSelection(response.getInt("id_instituicao"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            it.removeExtra("idEdicao");
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cria um Intent para inclusão
                Intent it = new Intent(getBaseContext(), instituicaoActivity.class);
                // inicia activity
                startActivity(it);
            }
        });

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consulta = new Consulta();

                // valida os campos obrigatórios
                if (!validaCamposObrigatorios()) {
                    return;
                }
                // pegar dados da tela
                String diagnostico = editTextDiagnostico.getText().toString();
                String medico      = editTextMedico.getText().toString();
                String prescricao  = editTextPrescricao.getText().toString();


                // seta valores no cliente
                consulta.setDiagnostico(diagnostico);
                consulta.setNome_medico(medico);
                consulta.setPrescricao(prescricao);
                consulta.setId_instituicao(spinnerPais.getSelectedItemPosition());
                consulta.setId_usuario(1);// olhar depois
                if (idEdicao > 0) {
                    consulta.setId(idEdicao);
                    Toast.makeText(
                            getBaseContext(),
                            "O registro = "+consulta.getId(),
                            Toast.LENGTH_SHORT
                    ).show();
                    // Atualiza os dados da pessoa no banco de dados
                    try {
                        acessaBanco.atualizar(consulta);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // insere nova pessoa no banco de dados
                    try {
                        acessaBanco.inserir(consulta);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }
        });
        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getBaseContext(),
                        "O registro = "+idEdicao,
                        Toast.LENGTH_SHORT
                ).show();
                // Se for inclusão não permite exclusão
                if (idEdicao < 0) {
                    Toast.makeText(
                            getBaseContext(),
                            "O registro ainda não foi inserido!",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    // Cria mensagem de alerta
                   verificaConfirmacao();
                }
                it.removeExtra("idEdicao");
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sessao = getSharedPreferences("config", MODE_PRIVATE);
        int id = sessao.getInt("idLogado",-1);

    }

    public boolean validaCamposObrigatorios(){
        if(editTextDiagnostico.getText().toString().isEmpty()) {
            this.editTextDiagnostico.setError("Diagnostico é obrigatório");
            return false;
        }
        if(editTextPrescricao.getText().toString().isEmpty()) {
            this.editTextPrescricao.setError("Prescrição é obrigatório");
            return false;
        }
        // telefone, e-mail e endereço são opcionais
        return true;
    }

    public void verificaConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this, // 1 - Activity que irá executar o Alerta
                R.style.Theme_AppCompat_DayNight_Dialog); // 2 - Estilo do Alerta
        // Adiciona texto do Título
        builder.setTitle("Exclusão da Consulta");
        // Adiciona mensagem do Alerta
        builder.setMessage("Você realmente deseja excluir essa consulta?");
        // Adiciona botão, se null apesas fecha o alerta
        builder.setNegativeButton("Não", null);
        // adiciona botão, evento onClick adicionado
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // excluir Pessoa no banco de dados
                acessaBanco.excluir(idEdicao);
                // exibe mensagem de sucesso
                Toast.makeText(
                        getBaseContext(),
                        "Consulta Excluída com Sucesso!",
                        Toast.LENGTH_SHORT).show();
                //acessaBanco.fechaConexaoBanco();
                finish();
            }
        });
        // exibe o Alerta na tela
        builder.show();
    }


}
