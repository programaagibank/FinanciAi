package com.financiai.model.entities;

import com.financiai.model.enums.TipoAmortizacao;

public class Financiamento {

    private Double taxaJuros;
    private double valorFinanciado;
    private double valorEntrada;
    private int prazo;
    private TipoAmortizacao tipoAmortizacao;
    private double totalPagar;
    private int financiamentoId; // Adicionado para usar na classe FinanciamentoDAO

    // Construtor com todos os atributos, exceto financiamentoId (gerado automaticamente)
    public Financiamento(int prazo, Double taxaJuros, TipoAmortizacao tipoAmortizacao, double valorEntrada, double valorFinanciado) {
        this.prazo = prazo;
        this.taxaJuros = taxaJuros;
        this.tipoAmortizacao = tipoAmortizacao;
        this.valorEntrada = valorEntrada;
        this.valorFinanciado = valorFinanciado;
        this.totalPagar = calcularTotalPagar(); // Calcula o total a pagar com base nos outros atributos
    }

    // Método para calcular o total a pagar (pode ser personalizado conforme a lógica de negócio)
    private double calcularTotalPagar() {
        // Exemplo de cálculo simples: totalPagar = valorFinanciado + (valorFinanciado * taxaJuros)
        return valorFinanciado + (valorFinanciado * taxaJuros);
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
        return financiamentoId;
    }

    public void setFinanciamentoId(int financiamentoId) {
        this.financiamentoId = financiamentoId;
    }
}