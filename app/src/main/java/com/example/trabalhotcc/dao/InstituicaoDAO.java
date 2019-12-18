package com.example.trabalhotcc.dao;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhotcc.modelo.Instituicao;
import com.example.trabalhotcc.modelo.Localidade;
import com.example.trabalhotcc.utils.NukeSSLCerts;
import com.example.trabalhotcc.utils.ProjetoUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class InstituicaoDAO {
    private RequestQueue filaEnviadoraDeMensagens;
    private Context activityExecutando;

    public InstituicaoDAO(Context activityExecutando) {
        // salva o Contexto
        this.activityExecutando  = activityExecutando;
        // Cria uma fila para envio de mensagens por Volley
        filaEnviadoraDeMensagens = Volley.newRequestQueue(activityExecutando);
        // caso ambiente de teste, comando ingora certificados SSL
        if(ProjetoUtils.AMBIENTE_DE_TESTE){
            NukeSSLCerts.nuke();
        }
    }

    public void excluir(int id) {
        String url = ProjetoUtils.ENDERECO_SERVIDOR + "consultas/" + id;
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

    public void inserirLocal(final Localidade local, final Instituicao instituicao) throws JSONException {
        final String url = ProjetoUtils.ENDERECO_SERVIDOR + "localidade/";
        final String url2 = ProjetoUtils.ENDERECO_SERVIDOR + "instituicao/";
        final JSONObject dados = new JSONObject();
        final JSONObject dados2 = new JSONObject();

        dados.put("cidade", local.getCidade());
        dados.put("cep", local.getCep());
        dados.put("bairro", local.getBairro());
        dados.put("rua", local.getRua());
        dados.put("numero", local.getNumero());

        dados2.put("nome", instituicao.getNome());
        dados2.put("id_usuario",1);

        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dados,
                new Response.Listener<JSONObject>() {
                    int id_local;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            id_local = (response.getInt("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            dados2.put("id_local", id_local);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest requisicao2 = new JsonObjectRequest(
                                Request.Method.POST,
                                url2,
                                dados2,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {}
                                }
                        );
                        filaEnviadoraDeMensagens.add(requisicao2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {}
                }
        );
        filaEnviadoraDeMensagens.add(requisicao);
    }
}
