package com.financiai.model.entities;

import com.financiai.model.enums.TipoAmortizacao;

public class Financiamento {

    private int id;
    private int idCliente;
    private int idImovel;
    private Double taxaJuros;
    private double valorFinanciado;
    private double valorEntrada;
    private int prazo;
    private TipoAmortizacao tipoAmortizacao;
    private double totalPagar;

    public Financiamento(int id, int idCliente, int idImovel, int prazo,
                         Double taxaJuros, TipoAmortizacao tipoAmortizacao, double totalPagar,
                         double valorEntrada, double valorFinanciado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idImovel = idImovel;
        this.prazo = prazo;
        this.taxaJuros = taxaJuros;
        this.tipoAmortizacao = tipoAmortizacao;
        this.totalPagar = totalPagar;
        this.valorEntrada = valorEntrada;
        this.valorFinanciado = valorFinanciado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdImovel() {
        return idImovel;
    }

    public void setIdImovel(int idImovel) {
        this.idImovel = idImovel;
    }

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


}
