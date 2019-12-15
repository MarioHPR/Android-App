package com.example.trabalhotcc.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhotcc.R;
import com.example.trabalhotcc.modelo.Consulta;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorGenerico<ModeloEscolhido> extends RecyclerView.Adapter<LinhaExibidaGenerica> {
    private List<ModeloEscolhido> list;
    private Context activityAtual;

    // escutador de clicks do item
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public AdaptadorGenerico(Context context) {
        this.activityAtual = context;
        list = new ArrayList<>();
    }

    @Override
    public LinhaExibidaGenerica onCreateViewHolder(ViewGroup parent, int viewType) {
        final LinhaExibidaGenerica linha = new LinhaExibidaGenerica(LayoutInflater.from(activityAtual)
                .inflate(R.layout.linha_simples_de_item_generica, parent, false));
        return linha;
    }

    @Override
    public void onBindViewHolder(@NonNull LinhaExibidaGenerica linha, final int posicaoClicada) {
        // adiciona texto
        linha.textoExibido.setText(list.get(posicaoClicada).toString());

        // evento de Click
        linha.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(null,v,posicaoClicada,v.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear(){
        list.clear();
    }

    public void addAll(List<ModeloEscolhido> dados){
        list.addAll(dados);
    }
    public void add(Consulta c){
        list.add((ModeloEscolhido) c);
    }

    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ModeloEscolhido getItem(int posicaoSelecionada){
        return list.get(posicaoSelecionada);
    }
}
