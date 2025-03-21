package org.financiai.services;
import java.util.ArrayList;
import java.util.List;

/*
 * Calcula as parcelas de um financiamento utilizando o método SAC.
 * @param valorFinanciamento Valor total do financiamento.
 * @param taxaJuros Taxa de juros mensal.
 * @param prazo Número de parcelas.
 * @return Lista de parcelas calculadas.
 * @throws IllegalArgumentException Se os valores de entrada forem inválidos.
 * */

public class SAC implements Amortizacao{
    @Override
    public List<Double> calculaParcela(Double valorFinanciamento, Double taxaJuros, int prazo) {

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
    public List<Double> calculaAmortizacao(Double valorFinanciamento, Double taxaJuros, int prazo) {
       List<Double> amortizacao = new ArrayList<>();
       double valorAmortizacao = valorFinanciamento / prazo;

        for (int i = 0; i < prazo; i++) {
            amortizacao.add(valorAmortizacao);
        }
        return amortizacao;
    }

    public double totalPagoSac(double valorFinanciamento, Double taxaJuros, int prazo) {
        List<Double> parcelas = calculaParcela(valorFinanciamento, taxaJuros, prazo);
        double totalPago = 0;

        for (double parcela : parcelas) {
            totalPago += parcela;
        }
        return totalPago;
    }
}
