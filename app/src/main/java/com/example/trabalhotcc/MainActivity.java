package com.example.trabalhotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText login;
    private EditText senha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.user);
        senha = findViewById(R.id.senha);


        SharedPreferences sessao = getSharedPreferences("config", Context.MODE_PRIVATE);
        int id = sessao.getInt("idLogado",-1);
        if(id > 0){
            Intent i = new Intent(getBaseContext(), PrincipalActivity.class);
            startActivity(i);
        }
    }

    public void logar(View v){
        final String log = login.getText().toString();
        String key = senha.getText().toString();
        // Cria uma fila para envio de mensagens por Volley
        RequestQueue filaEnviadoraDeMensagens = Volley.newRequestQueue(this);
        //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig. Procurar por IPv4
        String urlServidor = "http://192.168.31.6:3000/login";// 192.168.0.108 em casa no if 31.6

        // parâmetros para enviar por POST usando um Map
                final Map<String, String> parametrosPOST = new HashMap<>();
        // adiciona-se os parâmetros por chave -> valor
                parametrosPOST.put("login",String.valueOf(log));
                parametrosPOST.put("senha",String.valueOf(key));

        // cria a requisição de mensagem e tratamento de resposta
                StringRequest requisicao = new StringRequest(
                        Request.Method.POST, // 1 - Método usado para enviar mensagem
                        urlServidor,         // 2 - Endereço do servidor
                        new Response.Listener<String>() { // 3 - Objeto para tratar resposta
                            @Override
                            public void onResponse(String response) {
                                Log.d("DEBUG_LOGIN","RES: "+response);
                                // {"id":7,"cpf":"1","nome":"w'","dataNascimento":"2001-01-01T02:00:00.000Z",
                                //  "usuario":"1","senha":"1","id_contato":null,"id_local":null}
                                //
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if(object.has("id")){
                                        int id = object.getInt("id");
                                        // cria u intent
                                        Intent i = new Intent(getBaseContext(), PrincipalActivity.class);
                                        i.putExtra("id",id);

                                        SharedPreferences sessao = getSharedPreferences("config",MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sessao.edit();
                                        edit.putInt("idLogado",id);
                                        edit.apply();

                                        startActivity(i);
                                        // exibe o resultado na tela usando Toast
                                        Toast.makeText(
                                                getBaseContext(),
                                                "ID : " + id,
                                                Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() { // 4 - Objeto para tratar erro
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // exibe o erro na tela e nos Logs
                                Toast.makeText(
                                        getBaseContext(),
                                        "Erro: "+error,
                                        Toast.LENGTH_LONG).show();
                                // log de erro no LogCat
                                Log.e("TAG_EXEMPLO",
                                        "Erro encontrado ao tentar enviar mensagem",error);
                            }
                        }){
                    // sobrescrever método getParams para enviar dados por POST
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return parametrosPOST;
                    }
                };

        // envia a mensagem ao servidor
                filaEnviadoraDeMensagens.add(requisicao);

    }
}
