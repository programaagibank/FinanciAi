package com.financiai.services;

import java.util.List;

/*
 * Interface para cálculo de parcelas e amortização de financiamentos.
 */
public interface Amortizacao {
    // O QUE ESTAVA ERRADO: Os métodos retornavam double, mas as implementações retornavam List<Double>.
    // O QUE FOI CORRIGIDO: Corrigi a assinatura dos métodos para retornar List<Double>.
    List<Double> calculaParcela(double valorFinanciamento, double taxaJuros, int prazo);
    List<Double> calculaAmortizacao(double valorFinanciamento, double taxaJuros, int prazo);
}