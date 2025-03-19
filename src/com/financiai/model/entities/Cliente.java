package com.financiai.model.entities;

public class Cliente {
    private int id;
    private String nome;
    private Double rendaMensal;

    public Cliente(){}

    public Cliente(String nome, double rendaMensal){

        this.nome = nome;
        this.rendaMensal = rendaMensal;

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
