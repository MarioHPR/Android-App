package com.example.trabalhotcc.modelo;

public class Instituicao {
    private int id;
    private String nome;
    private int id_local;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    @Override
    public String toString() {
        return nome;
    }
}
