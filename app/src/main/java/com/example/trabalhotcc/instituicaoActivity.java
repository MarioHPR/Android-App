package com.example.trabalhotcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhotcc.dao.InstituicaoDAO;
import com.example.trabalhotcc.modelo.Consulta;
import com.example.trabalhotcc.modelo.Instituicao;
import com.example.trabalhotcc.modelo.Localidade;

import org.json.JSONException;

public class instituicaoActivity extends AppCompatActivity {

    // declarar objeto para manipular uma Pessoa. Inicialmente null para inclusão
    private Instituicao instituicao = null;
    private Localidade  local = null;

    // declarar objeto para manipular o banco de dados
    private InstituicaoDAO acessaBanco;

    // declarar objetos para interagir com a tela
    private TextView editTextNome;
    private TextView editTextCidade;
    private TextView editTextCep;
    private TextView editTextBairro;
    private TextView editTextRua;
    private TextView editTextNumero;
    private Button btSalvar;
    private Button btExcluir;
    private Intent it;
    private int idEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao);

        acessaBanco = new InstituicaoDAO(getBaseContext());
        // vincula Componentes com Views da tela
        editTextNome   = findViewById(R.id.editTextNome);
        editTextCidade = findViewById(R.id.editTextCidade);
        editTextCep    = findViewById(R.id.editTextCep);
        editTextBairro = findViewById(R.id.editTextBairro);
        editTextRua    = findViewById(R.id.editTextRua);
        editTextNumero = findViewById(R.id.editTextNumero);
        btSalvar       = findViewById(R.id.btSalvar);
        btExcluir      = findViewById(R.id.btExcluir);

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instituicao = new Instituicao();
                local       = new Localidade();

                // valida os campos obrigatórios
                if (!validaCamposObrigatorios()) {
                    return;
                }
                // pegar dados da tela
                String nomeInstituicao = editTextNome.getText().toString();
                String cidade          = editTextCidade.getText().toString();
                String cep             = editTextCep.getText().toString();
                String bairro          = editTextBairro.getText().toString();
                String rua             = editTextRua.getText().toString();
                String numero          = editTextNumero.getText().toString();


                // seta valores no cliente
                instituicao.setNome(nomeInstituicao);
                local.setCidade(cidade);
                local.setCep(cep);
                local.setBairro(bairro);
                local.setRua(rua);
                local.setNumero(Integer.parseInt(numero));
                //instituicao.setId_local();// olhar depois

                    try {
                        acessaBanco.inserirLocal(local,instituicao);
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    public boolean validaCamposObrigatorios(){
        if(editTextNome.getText().toString().isEmpty()) {
            this.editTextNome.setError("Nome instituição é obrigatório");
            return false;
        }
        if(editTextCidade.getText().toString().isEmpty()) {
            this.editTextCidade.setError("Cidade é obrigatório");
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
