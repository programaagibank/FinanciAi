package com.financiai.model.entities;

public class Cliente {
    private int id;
    private String nome;
    private Double rendaMensal;

    public Cliente(){}

    public Cliente(int id, String nome, double rendaMensal){

        this.id = id;
        this.nome = nome;
        this.rendaMensal = rendaMensal;

    }

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

    public Double getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(Double rendaMensal) {
        this.rendaMensal = rendaMensal;
    }
}
