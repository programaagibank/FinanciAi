package com.financiai.services;
import java.util.ArrayList;
import java.util.List;

public class SAC implements Amortizacao{
    @Override
    public List<Double> calculaParcela(Double valorFinanciamento, Double taxaJuros, int prazo) {
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
    public List<Double> calculaAmortizacao(Double valorFinanciamento, Double taxaJuros, int prazo) {
       List<Double> amortizacao = new ArrayList<>();
       double valorAmortizacao = valorFinanciamento / prazo;

        for (int i = 0; i < prazo; i++) {
            amortizacao.add(valorAmortizacao);
        }
        return amortizacao;
    }
}
