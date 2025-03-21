package com.financiai.services;

import java.util.ArrayList;
import java.util.List;

/*
 * Calcula as parcelas de um financiamento utilizando o método SAC.
 */
public class SAC implements Amortizacao {

    @Override
    public List<Double> calculaParcela(double valorFinanciamento, double taxaJurosMensal, int prazo) {
        if (valorFinanciamento <= 0 || taxaJurosMensal <= 0 || prazo <= 0) {
            throw new IllegalArgumentException("Valores de financiamento, taxa de juros e prazo devem ser positivos.");
        }

        List<Double> parcelaSAC = new ArrayList<>();
        double saldoDevedor = valorFinanciamento;
        double valorAmortizacao = valorFinanciamento / prazo;
        for (int i = 0; i < prazo; i++) {
            double parcela = valorAmortizacao + (saldoDevedor * taxaJurosMensal);
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