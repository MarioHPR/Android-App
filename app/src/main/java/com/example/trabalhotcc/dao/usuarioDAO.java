package com.example.trabalhotcc.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhotcc.modelo.Instituicao;
import com.example.trabalhotcc.modelo.Usuario;
import com.example.trabalhotcc.utils.NukeSSLCerts;
import com.example.trabalhotcc.utils.ProjetoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class usuarioDAO {
    private RequestQueue filaEnviadoraDeMensagens;
    private Context activityExecutando;
    // declara os Componentes para interagir com a tela

    // declara o adaptador
    private ArrayAdapter<String> adaptadorSpinnerPais;


    public usuarioDAO(Context activityExecutando) {
        // salva o Contexto
        this.activityExecutando  = activityExecutando;
        // Cria uma fila para envio de mensagens por Volley
        filaEnviadoraDeMensagens = Volley.newRequestQueue(activityExecutando);
        // caso ambiente de teste, comando ingora certificados SSL
        if(ProjetoUtils.AMBIENTE_DE_TESTE){
            NukeSSLCerts.nuke();
        }
    }

        // arrumar
    public void excluir(int id) {
        String url = ProjetoUtils.ENDERECO_SERVIDOR + "/" + id;
        Log.d("URL",
                "Erro ao converter resultado do Json: " + url);
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
        filaEnviadoraDeMensagens.add(requisicao);
    }

    public void atualizar(Usuario usu) throws JSONException {

        String url = ProjetoUtils.ENDERECO_SERVIDOR + "usuario/" + usu.getId();
        JSONObject dados = new JSONObject();
        dados.put("nome", usu.getNome());
        dados.put("cpf", usu.getCpf());
        dados.put("dataNascimento", usu.getDataNascimento());
        dados.put("usuario", usu.getUsuario());
        dados.put("senha", usu.getSenha());

        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                dados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(
                                activityExecutando,    // 1 - Contexto
                                "Alterado com sucesso!",    // 2 - mensagem
                                Toast.LENGTH_SHORT)         // 3 - duração
                                .show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {}
                }
        );
        filaEnviadoraDeMensagens.add(requisicao);
    }

    public void inserir(final Usuario usu) throws JSONException {
        final String url = ProjetoUtils.ENDERECO_SERVIDOR + "usuario/";
        JSONObject dados = new JSONObject();
        dados.put("nome", usu.getNome());
        dados.put("cpf", usu.getCpf());
        dados.put("dataNascimento", usu.getDataNascimento());
        dados.put("usuario", usu.getUsuario());
        dados.put("senha", usu.getSenha());
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            usu.setId(response.getInt("id"));
                            Toast.makeText(
                                    activityExecutando,    // 1 - Contexto
                                    "Adicionado com sucesso!",    // 2 - mensagem
                                    Toast.LENGTH_SHORT)         // 3 - duração
                                    .show();
                        } catch (JSONException e) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {}
                }
        );
        filaEnviadoraDeMensagens.add(requisicao);
    }

    public void instituicao(int i,Spinner spinnerPais){
        ///////////////////////////////////////////////////////////////////////
        // Cria uma fila para envio de mensagens por Volley
        //RequestQueue filaEnviadoraDeMensagens = Volley.newRequestQueue(getActivity());
        //Dica: obter o IP abrir o terminal de comando cmd e escrever ipconfig. Procurar por IPv4

        String url = "http://192.168.0.108:3000/instituicao/?id="+i;// 192.168.0.108 em casa no if 31.6

        // parâmetros para enviar por POST usando um Map
        final Map<String, String> parametrosPOST = new HashMap<>();

        // vincula o adaptador com o Spinner
        spinnerPais.setAdapter(adaptadorSpinnerPais);

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
/*
    public void listarTodosPorFiltro(String texto, final AdaptadorGenerico<Instituicao> adaptadorLista) {
        String url = ProjetoUtils.ENDERECO_SERVIDOR + "cliente/?nome=" + texto;
        JsonArrayRequest requisicao = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            adaptadorLista.clear();
                            for (int i = 0; i < response.length(); i++) {
                                Cliente c = new Cliente();
                                JSONObject o = (JSONObject) response.get(i);
                                try {
                                    c.setId(o.getInt("id"));
                                    c.setNome(o.getString("nome"));
                                    c.setEndereco(o.getString("endereco"));
                                    c.setEmail(o.getString("email"));
                                    c.setTelefone(o.getString("telefone"));
                                    c.setCpf(o.getString("cpf"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                adaptadorLista.add(c);
                            }
                            adaptadorLista.notifyDataSetChanged();

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
    }
    public static boolean isConnected(Context cont){
        ConnectivityManager conmag = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conmag != null ) {
            conmag.getActiveNetworkInfo();

            //Verifica internet pela WIFI
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                return true;
            }

            //Verifica se tem internet móvel
            if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                return true;
            }
        }

        return false;
    }*/
}
