package financiai.financiai.services;

import financiai.financiai.model.Parcela;
import java.util.ArrayList;
import java.util.List;

public class SAC {
    public List<Parcela> calcularParcela(Double valorFinanciamento, Double taxaJuros, int prazo, String cpfCliente) {
        validarParametros(valorFinanciamento, taxaJuros, prazo);
        if (cpfCliente == null || cpfCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF do cliente não pode ser nulo ou vazio");
        }

        List<Parcela> parcelas = new ArrayList<>();
        double amortizacaoConstante = valorFinanciamento / prazo;
        double saldoDevedor = valorFinanciamento;

        for (int i = 1; i <= prazo; i++) {
            double juros = saldoDevedor * taxaJuros;
            double parcela = amortizacaoConstante + juros;
            saldoDevedor -= amortizacaoConstante;

            parcelas.add(new Parcela(i, parcela, amortizacaoConstante, juros, saldoDevedor, cpfCliente));
        }

        return parcelas;
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