package com.financiai.services;

import java.util.List;

public interface Amortizacao {

    List<Double> calculaParcelas(Double valorFinanciamento, Double taxaJuros, int prazo);
    List<Double> calculaAmortizacao(Double valorFinanciamento, Double taxaJuros, int prazo);

}