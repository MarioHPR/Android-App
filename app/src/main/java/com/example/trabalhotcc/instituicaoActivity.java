package com.example.trabalhotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhotcc.modelo.Instituicao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class instituicaoActivity extends AppCompatActivity {
    // declara os Componentes para interagir com a tela
    private Spinner spinnerPais;
    // declara o adaptador
    private ArrayAdapter<String> adaptadorSpinnerPais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao);
        // vincula Componentes com Views da tela
        spinnerPais = findViewById(R.id.spinner);
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
    }
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sessao = getSharedPreferences("config", MODE_PRIVATE);
        int id = sessao.getInt("idLogado",-1);

    }
}
