package com.example.trabalhotcc.utils;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhotcc.R;

public class LinhaExibidaGenerica extends RecyclerView.ViewHolder  {
    public TextView textoExibido;

    public LinhaExibidaGenerica(View itemView) {
        super(itemView);
        textoExibido = itemView.findViewById(R.id.textoGenerico);
    }
}
