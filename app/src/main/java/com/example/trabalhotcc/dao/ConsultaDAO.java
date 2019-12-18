package com.example.trabalhotcc.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhotcc.modelo.Consulta;
import com.example.trabalhotcc.utils.AdaptadorGenerico;
import com.example.trabalhotcc.utils.NukeSSLCerts;
import com.example.trabalhotcc.utils.ProjetoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConsultaDAO {

    private RequestQueue filaEnviadoraDeMensagens;
    private Context activityExecutando;



    public ConsultaDAO(Context activityExecutando) {
        // salva o Contexto
        this.activityExecutando  = activityExecutando;
        // Cria uma fila para envio de mensagens por Volley
        filaEnviadoraDeMensagens = Volley.newRequestQueue(activityExecutando);
        // caso ambiente de teste, comando ingora certificados SSL
        if(ProjetoUtils.AMBIENTE_DE_TESTE){
            NukeSSLCerts.nuke();
        }
    }

    public void buscarUm(int id, Response.Listener<JSONObject> listener) {
        String url = ProjetoUtils.ENDERECO_SERVIDOR + "consultas/?busca=" + id+"&usu="+1;
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                listener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {}
                }
        );
        filaEnviadoraDeMensagens.add(requisicao);
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

    public void atualizar(Consulta con) throws JSONException {
        String url = ProjetoUtils.ENDERECO_SERVIDOR + "consultas/";
        JSONObject dados = new JSONObject();
        dados.put("diagnostico", con.getDiagnostico());
        dados.put("prescricao", con.getPrescricao());
        dados.put("nome_medico", con.getNome_medico());
        dados.put("id_instituicao", con.getId_instituicao());
        dados.put("id",con.getId());
        dados.put("id_usuario", 1);

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

    public void inserir(final Consulta con) throws JSONException {
        final String url = ProjetoUtils.ENDERECO_SERVIDOR + "consultas/";
        JSONObject dados = new JSONObject();
        dados.put("diagnostico", con.getDiagnostico());
        dados.put("prescricao", con.getPrescricao());
        dados.put("nome_medico", con.getNome_medico());
        dados.put("id_instituicao", con.getId_instituicao());
        dados.put("id_usuario", con.getId_usuario());
        JsonObjectRequest requisicao = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            con.setId(response.getInt("id"));
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


    public void listarTodosPorFiltro(final AdaptadorGenerico<Consulta> adaptadorLista) {
        String url = ProjetoUtils.ENDERECO_SERVIDOR + "consultas/?id=1";
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
                                Consulta c = new Consulta();
                                JSONObject o = (JSONObject) response.get(i);
                                try {
                                    Log.d("AQUIIIII",""+o.getString("diagnostico"));
                                    c.setId(o.getInt("id"));
                                    c.setDiagnostico(o.getString("diagnostico"));
                                    c.setPrescricao(o.getString("prescricao"));
                                    c.setNome_medico(o.getString("nome_medico"));
                                    c.setId_instituicao(o.getInt("id_instituicao"));
                                    c.setId_usuario(o.getInt("id_usuario"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                adaptadorLista.add(c);
                            }
                            adaptadorLista.notifyDataSetChanged();

                        } catch (JSONException e) {
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
    }

}

