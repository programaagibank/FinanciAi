package com.financiai.model.entities;

public class Parcelas {

    private int numeroParcela;
    private Double valorParcela;
    private Double valorAmortizacao;

    public Parcelas(int numeroParcela, Double valorParcela) {
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
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


}
