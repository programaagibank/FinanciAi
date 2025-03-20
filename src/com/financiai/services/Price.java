package com.financiai.services;

import java.util.ArrayList;
import java.util.List;

/*
 * Calcula as parcelas de um financiamento utilizando o método Price.
 * @param valorFinanciamento Valor total do financiamento.
 * @param taxaJuros Taxa de juros mensal.
 * @param prazo Número de parcelas.
 * @return Lista de parcelas calculadas.
 * @throws IllegalArgumentException Se os valores de entrada forem inválidos.
 * */


public class Price implements Amortizacao{


    @Override
    public List<Double> calculaParcela(Double valorFinanciamento, Double taxaJuros, int prazo) {

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
    public List<Double> calculaAmortizacao(Double valorFinanciamento, Double taxaJuros, int prazo) {
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

