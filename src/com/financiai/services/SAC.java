package com.financiai.services;

import java.util.ArrayList;
import java.util.List;

/*
 * Calcula as parcelas de um financiamento utilizando o método SAC.
 */
public class SAC implements Amortizacao {

    @Override
    public List<Double> calculaParcela(double valorFinanciamento, double taxaJuros, int prazo) {
        // O QUE ESTAVA ERRADO: Não havia validação para a taxa de juros.
        // O QUE FOI CORRIGIDO: Adicionei validação para garantir que a taxa de juros seja positiva.
        if (valorFinanciamento <= 0 || taxaJuros <= 0 || prazo <= 0) {
            throw new IllegalArgumentException("Valores de financiamento, taxa de juros e prazo devem ser positivos.");
        }

        List<Double> parcelaSAC = new ArrayList<>();
        double saldoDevedor = valorFinanciamento;
        double valorAmortizacao = valorFinanciamento / prazo;
        for (int i = 0; i < prazo; i++) {
            double parcela = valorAmortizacao + (saldoDevedor * taxaJuros);
            parcelaSAC.add(parcela);
            saldoDevedor -= valorAmortizacao;
        }
        return parcelaSAC;
    }

    @Override
    public List<Double> calculaAmortizacao(double valorFinanciamento, double taxaJuros, int prazo) {
        List<Double> amortizacao = new ArrayList<>();
        double valorAmortizacao = valorFinanciamento / prazo;
        for (int i = 0; i < prazo; i++) {
            amortizacao.add(valorAmortizacao);
        }
        return amortizacao;
    }
}