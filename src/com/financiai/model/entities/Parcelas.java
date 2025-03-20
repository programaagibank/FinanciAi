package com.financiai.model.entities;

public class Parcelas {

    private int id; // ID da parcela
    private int numeroParcela; // Número da parcela
    private Double valorParcela; // Valor da parcela
    private Double valorAmortizacao; // Valor da amortização
    private int financiamentoId; // ID do financiamento associado à parcela

    // Construtor com todos os atributos (incluindo id)
    public Parcelas(int id, int numeroParcela, Double valorParcela, Double valorAmortizacao, int financiamentoId) {
        this.id = id;
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
        this.financiamentoId = financiamentoId;
    }

    // Construtor sem o id (para casos onde o id é gerado automaticamente)
    public Parcelas(int numeroParcela, Double valorParcela, Double valorAmortizacao, int financiamentoId) {
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
        this.financiamentoId = financiamentoId;
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

    public int getFinanciamentoId() {
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {
        this.financiamentoId = financiamentoId;
    }

    // Método toString para exibir as informações da parcela
    @Override
    public String toString() {
        return "Parcela {" +
                "id=" + id +
                ", numeroParcela=" + numeroParcela +
                ", valorParcela=" + valorParcela +
                ", valorAmortizacao=" + valorAmortizacao +
                ", financiamentoId=" + financiamentoId +
                '}';
    }
}