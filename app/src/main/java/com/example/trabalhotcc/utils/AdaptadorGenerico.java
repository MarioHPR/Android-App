package com.example.trabalhotcc.utils;

public class AdaptadorGenerico {
   /* private List<ModeloEscolhido> list;
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
    public void add(Cliente c){
        list.add((ModeloEscolhido) c);
    }

    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public ModeloEscolhido getItem(int posicaoSelecionada){
        return list.get(posicaoSelecionada);
    }*/
}
