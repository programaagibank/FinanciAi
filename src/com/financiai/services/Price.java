package com.financiai.services;

import java.util.ArrayList;
import java.util.List;

public class Price implements Amortizacao{


    @Override
    public List<Double> calculaParcela(Double valorFinanciamento, Double taxaJuros, int prazo) {
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

