package com.example.trabalhotcc.ui.slideshow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabalhotcc.R;
import com.example.trabalhotcc.dao.ConsultaDAO;
import com.example.trabalhotcc.consultaActivity;
import com.example.trabalhotcc.modelo.Consulta;
import com.example.trabalhotcc.utils.AdaptadorGenerico;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import static android.content.Context.MODE_PRIVATE;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    // Objetos para interagir com a tela
    private RecyclerView recyclerView;
    private SearchView filtro;

    // adaptador para o ListView
    private AdaptadorGenerico<Consulta> adaptadorLista;

    // objeto para acesar o banco de dados
    private ConsultaDAO acessaBanco;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        // instancia objeto do banco
        this.acessaBanco = new ConsultaDAO(getContext());


        // vincula objetos de interação com a tela o XML
        recyclerView     = root.findViewById(R.id.recyclerViewLista);

        // instancia adaptador
        adaptadorLista   = new AdaptadorGenerico<>(getActivity().getBaseContext());
        // configura o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        // adiciona linha entre itens
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // vincula adaptador com o RecyclerView
        recyclerView.setAdapter(adaptadorLista);

        // evento ao clicar em um item
        adaptadorLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // pega o item selecionado
                Consulta p = (Consulta) adaptadorLista.getItem(position);
                // cria um Intent para Edição
                Intent it = new Intent(getActivity(), consultaActivity.class);
                // adiciona parâmetros para informar edição
                it.putExtra("idEdicao",p.getId());
                startActivity(it);
            }
        });




        final TextView textView = root.findViewById(R.id.text_slideshow);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cria um Intent para inclusão
                Intent it = new Intent(getActivity().getBaseContext(), consultaActivity.class);
                // inicia activity
                startActivity(it);
            }
        });


            onResume();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sessao = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        int id = sessao.getInt("idLogado",-1);


        // pega o texto para filtro
//        String texto = filtro.getQuery().toString();
//        // limpa tela
        adaptadorLista.clear();
//        // atualiza itens na tela
//        if(acessaBanco.isConnected(getContext())){
            acessaBanco.listarTodosPorFiltro(adaptadorLista);
//
//            Toast.makeText(
//                    getActivity(),
//                    "COM CONEXAO AGORA! " + acessaBanco.isConnected(getContext()),
//                    Toast.LENGTH_SHORT).show();
//        } else{
//           // adaptadorLista.addAll(offLine.listarTodosPorFiltro(texto));
//            //offLine.
//            Toast.makeText(
//                    getActivity(),
//                    "SEM CONEXAO!",
//                    Toast.LENGTH_SHORT).show();
//        }
//        // informa a tela que os dados mudaram
        adaptadorLista.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                getActivity().finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }
}