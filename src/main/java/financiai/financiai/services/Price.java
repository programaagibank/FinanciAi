package financiai.financiai.services;

import financiai.financiai.model.Parcela;
import java.util.ArrayList;
import java.util.List;

public class Price {
    public List<Parcela> calcularParcela(Double valorFinanciamento, Double taxaJuros, int prazo, String cpfCliente) {
        validarParametros(valorFinanciamento, taxaJuros, prazo);
        if (cpfCliente == null || cpfCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do cliente não pode ser nulo ou vazio");
        }

        List<Parcela> parcelas = new ArrayList<>();
        double parcelaConstante = calcularParcelaConstante(valorFinanciamento, taxaJuros, prazo);
        double saldoDevedor = valorFinanciamento;

        for (int i = 1; i <= prazo; i++) {
            double juros = saldoDevedor * taxaJuros;
            double amortizacao = parcelaConstante - juros;
            saldoDevedor -= amortizacao;

            parcelas.add(new Parcela(i, parcelaConstante, amortizacao, juros, saldoDevedor, cpfCliente));
        }

        return parcelas;
    }

    private double calcularParcelaConstante(Double valorFinanciamento, Double taxaJuros, int prazo) {
        return valorFinanciamento * (taxaJuros / (1 - Math.pow(1 + taxaJuros, -prazo)));
    }

    private void validarParametros(Double valorFinanciamento, Double taxaJuros, int prazo) {
        if (valorFinanciamento == null || taxaJuros == null) {
            throw new IllegalArgumentException("Valores não podem ser nulos");
        }
        if (valorFinanciamento <= 0 || taxaJuros <= 0 || prazo <= 0) {
            throw new IllegalArgumentException("Valores devem ser positivos");
        }
    }
}