package org.financiai.model.entities;

public class Parcelas {


    private int id; // ID da parcela
    private int numeroParcela;
    private Double valorParcela;
    private Double valorAmortizacao;
    private int financiamentoId; // ID do financiamento associado à parcela


    // Construtor com todos os atributos (incluindo id)
    public Parcelas(int id, int numeroParcela, Double valorParcela, Double valorAmortizacao, int financiamentoId) {
        this.id = id;// ID da parcela
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.valorAmortizacao = valorAmortizacao;
        this.financiamentoId = financiamentoId;// ID do financiamento associado à parcela
    }

    // Getters e Setters

    public int getId() {// ID da parcela
        return id;
    }

    public void setId(int id) {// ID da parcela
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

    public int getFinanciamentoId() {// ID do financiamento associado à parcela
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {// ID do financiamento associado à parcela
        this.financiamentoId = financiamentoId;
    }


}














