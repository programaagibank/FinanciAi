package com.financiai.model.entities;

import com.financiai.model.enums.TipoAmortizacao;

public class Financiamento {

    private Double taxaJuros;
    private double valorFinanciado;
    private double valorEntrada;
    private int prazo;
    private TipoAmortizacao tipoAmortizacao;
    private double totalPagar;
    private int financiamentoId; // adicionado para usar na classe finaciamento dao

    // Construtor com todos os atributos, incluindo o financiamentoId
    public Financiamento(int prazo, Double taxaJuros, TipoAmortizacao tipoAmortizacao, double valorEntrada, double valorFinanciado, int financiamentoId) {
        this.prazo = prazo;
        this.taxaJuros = taxaJuros;
        this.tipoAmortizacao = tipoAmortizacao;
        this.valorEntrada = valorEntrada;
        this.valorFinanciado = valorFinanciado;
        this.financiamentoId = financiamentoId; // adicionado para usar na classe finaciamento dao
    }

    // Getters e Setters

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public Double getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(Double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public TipoAmortizacao getTipoAmortizacao() {
        return tipoAmortizacao;
    }

    public void setTipoAmortizacao(TipoAmortizacao tipoAmortizacao) {
        this.tipoAmortizacao = tipoAmortizacao;
    }

    public double getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public double getValorFinanciado() {
        return valorFinanciado;
    }

    public void setValorFinanciado(double valorFinanciado) {
        this.valorFinanciado = valorFinanciado;
    }

    public int getFinanciamentoId() {
        return financiamentoId; // // adicionado para usar na classe finaciamento dao
    }

    public void setFinanciamentoId(int financiamentoId) {
        this.financiamentoId = financiamentoId; // adicionado para usar na classe finaciamento dao
    }
}