package com.financiai.services;

import com.financiai.model.entities.Financiamento;

import java.util.List;

public interface Amortizacao {

    List<Double> calculaParcela(Double valorFinanciamento, Double taxaJuros, int prazo);
    List<Double> calculaAmortizacao(Double valorFinanciamento, Double taxaJuros, int prazo);

    List<Double> calculaParcela(Financiamento financiamento);
}