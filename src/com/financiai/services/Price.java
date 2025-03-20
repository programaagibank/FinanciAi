package com.financiai.services;

import java.util.ArrayList;
import java.util.List;

/*
 * Calcula as parcelas de um financiamento utilizando o método Price.
 */
public class Price implements Amortizacao {

    @Override
    public List<Double> calculaParcela(double valorFinanciamento, double taxaJuros, int prazo) {
        // O QUE ESTAVA ERRADO: Não havia validação para a taxa de juros.
        // O QUE FOI CORRIGIDO: Adicionei validação para garantir que a taxa de juros seja positiva.
        if (valorFinanciamento <= 0 || taxaJuros <= 0 || prazo <= 0) {
            throw new IllegalArgumentException("Valores de financiamento, taxa de juros e prazo devem ser positivos.");
        }

        List<Double> parcelas = new ArrayList<>();
        double parcelaConstante = valorFinanciamento * (taxaJuros / (1 - Math.pow(1 + taxaJuros, -prazo)));
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