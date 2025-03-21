package com.financiai.services;

import java.util.ArrayList;
import java.util.List;

/*
 * Calcula as parcelas de um financiamento utilizando o método Price.
 */
public class Price implements Amortizacao {

    @Override
    public List<Double> calculaParcela(double valorFinanciamento, double taxaJurosMensal, int prazo) {
        if (valorFinanciamento <= 0 || taxaJurosMensal <= 0 || prazo <= 0) {
            throw new IllegalArgumentException("Valores de financiamento, taxa de juros e prazo devem ser positivos.");
        }

        List<Double> parcelas = new ArrayList<>();
        double parcelaConstante = valorFinanciamento * (taxaJurosMensal / (1 - Math.pow(1 + taxaJurosMensal, -prazo)));
        for (int i = 0; i < prazo; i++) {
            parcelas.add(parcelaConstante);
        }
        return parcelas;
    }

    @Override
    public List<Double> calculaAmortizacao(double valorFinanciamento, double taxaJuros, int prazo) {
        List<Double> amortizacoes = new ArrayList<>();
        double parcelaConstante = valorFinanciamento * (taxaJuros / (1 - Math.pow(1 + taxaJuros, -prazo)));
        double saldoDevedor = valorFinanciamento;
        for (int i = 0; i < prazo; i++) {
            double juros = saldoDevedor * taxaJuros;
            double amortizacao = parcelaConstante - juros;
            amortizacoes.add(amortizacao);
            saldoDevedor -= amortizacao;
        }
        return amortizacoes;
    }
}