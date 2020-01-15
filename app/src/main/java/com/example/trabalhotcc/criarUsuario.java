package com.example.trabalhotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhotcc.dao.usuarioDAO;
import com.example.trabalhotcc.modelo.Consulta;
import com.example.trabalhotcc.modelo.Usuario;

import org.json.JSONException;

public class criarUsuario extends AppCompatActivity {

    private usuarioDAO acessaBanco;
    private Usuario usuario = null;

    private Button btSalvar;
    private TextView editTextNome;
    private TextView editTextCPF;
    private TextView editTextNascimento;
    private TextView editTextLogin;
    private TextView editTextSenha;
    private int idEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);

        acessaBanco = new usuarioDAO(getBaseContext());

                // vincula Componentes com Views da tela
        editTextNome       = findViewById(R.id.editTextNome);
        editTextCPF        = findViewById(R.id.editTextCPF);
        editTextNascimento = findViewById(R.id.editTextNascimento);
        editTextLogin      = findViewById(R.id.editTextLogin);
        editTextSenha      = findViewById(R.id.editTextSenha);
        btSalvar           = findViewById(R.id.btrSalvar);



        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();

                // valida os campos obrigatórios
                if (!validaCamposObrigatorios()) {
                    return;
                }
                // pegar dados da tela
                String nome  = editTextNome.getText().toString();
                String cpf   = editTextCPF.getText().toString();
                String nasc  = editTextNascimento.getText().toString();
                String login = editTextLogin.getText().toString();
                String senha = editTextSenha.getText().toString();

                // seta valores no cliente
                usuario.setNome(nome);
                usuario.setCpf(cpf);
                usuario.setDataNascimento(nasc);
                usuario.setUsuario(login);
                usuario.setSenha(senha);
                try {
                    acessaBanco.inserir(usuario);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }
    public boolean validaCamposObrigatorios(){
        if(editTextNome.getText().toString().isEmpty()) {
            this.editTextNome.setError("Nome é obrigatório");
            return false;
        }
        if(editTextCPF.getText().toString().isEmpty()) {
            this.editTextCPF.setError("CPF é obrigatório");
            return false;
        }
        // telefone, e-mail e endereço são opcionais
        return true;
    }
}
