package org.financiai.services;

import java.util.List;

public interface Amortizacao {

    List<Double> calculaParcela(Double valorFinanciamento, Double taxaJuros, int prazo);
    List<Double> calculaAmortizacao(Double valorFinanciamento, Double taxaJuros, int prazo);

}