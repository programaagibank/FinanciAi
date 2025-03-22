package org.financiai.model.entities;

import org.financiai.model.enums.TipoAmortizacao;

public class Financiamento {


    private int financiamentoId; // Adicionado para armazenar o ID gerado pelo banco de dados
    private Double taxaJuros;
    private double valorFinanciado;
    private double valorEntrada;
    private int prazo;
    private TipoAmortizacao tipoAmortizacao;
    private double totalPagar; // adicionado pois estava faltando isto


    public Financiamento(int prazo, Double taxaJuros, TipoAmortizacao tipoAmortizacao, double valorEntrada, double valorFinanciado, double totalPagar) {
        this.prazo = prazo;
        this.taxaJuros = taxaJuros;
        this.tipoAmortizacao = tipoAmortizacao;
        this.valorEntrada = valorEntrada;
        this.valorFinanciado = valorFinanciado;
        this.totalPagar = totalPagar;
    }

    // Getter e Setter para financiamentoId



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

    public int getFinanciamentoId() {// Adicionado para armazenar o ID gerado pelo banco de dados
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {// Adicionado para armazenar o ID gerado pelo banco de dados
        this.financiamentoId = financiamentoId;
    }

}
