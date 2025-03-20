package com.financiai.model.entities;

public class Parcelas {

    private int id; // Adicionado o atributo id
    private int numeroParcela;
    private Double valorParcela;
    private Double valorAmortizacao; 

    // Construtor com todos os atributos
    public Parcelas(int id, int numeroParcela, Double valorParcela, Double valorAmortizacao) {
        this.id = id;
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
    }

    // Construtor sem o id (para casos onde o id é gerado automaticamente)
    public Parcelas(int numeroParcela, Double valorParcela, Double valorAmortizacao) {
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Double getValorAmortizacao() {
        return valorAmortizacao;
    }

    public void setValorAmortizacao(Double valorAmortizacao) {
        this.valorAmortizacao = valorAmortizacao;
    }
}
