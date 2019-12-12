package com.example.trabalhotcc.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.trabalhotcc.R;

public class SlideshowFragment extends Fragment {

    // declara os Componentes para interagir com a tela
    private Spinner spinnerPais;
    // declara o adaptador
    private ArrayAdapter<String> adaptadorSpinnerPais;

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                // vincula Componentes com Views da tela
                spinnerPais = getActivity().findViewById(R.id.spinnerCidade);
                // vincula o adaptador com o Spinner
                spinnerPais.setAdapter(adaptadorSpinnerPais);
            }
        });


        //instancia o adaptador
        adaptadorSpinnerPais = new ArrayAdapter<>(
                getActivity().getBaseContext(),                   // 1 - contexto
                android.R.layout.simple_list_item_1 // 2 - modelo de linha (texto)
        );


        // adicionar dados
        adaptadorSpinnerPais.add("Selecione o País");
        adaptadorSpinnerPais.add("Brasil");
        adaptadorSpinnerPais.add("Estados Unidos");
        adaptadorSpinnerPais.add("Reino Unido");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sessao = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        int id = sessao.getInt("idLogado",-1);

    }
}