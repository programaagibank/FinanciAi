package org.financiai.model.entities;

public class Cliente {

    private String nome;
    private Double rendaMensal;
    private String cpf;

    public Cliente(String nome, String cpf, double rendaMensal) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }
        this.nome = nome;
        this.cpf = cpf;
        this.rendaMensal = rendaMensal;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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