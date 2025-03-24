package org.financiai.controller;

import org.financiai.model.enums.TipoAmortizacao;
import org.financiai.services.Price;
import org.financiai.services.SAC;
import org.financiai.view.FinanciamentoView;

import java.util.List;

/*
 * Calcula o Financiamento como um todo, utilizando métodos.
 * @param rendaMensal renda mensal do cliente.
 * @param valorImovel quanto custa o imóvel da simulação.
 * @param velorEntrada quanto o cliente dará de entrada.
 * @param taxaJurosanual a taxa que será aplicada no financiamento.
 * @param prazo quantidade total de meses para o financiamento.
 * @enum  amortizacao, tipo da amortização escolhida.
 * @return encerra o método caso atenda a condição
 * */

public class FinanciamentoController {

    private static final double LIMITE_PARCELA_RENDA = 0.3;

    public static void calcularFinanciamento(double rendaMensal, double valorImovel, double valorEntrada,
                                             double taxaJurosAnual, int prazo, TipoAmortizacao amortizacao) {
        double valorFinanciamento = valorImovel - valorEntrada;
        double taxaJurosMensal = taxaJurosAnual / 12 / 100;
        double limiteParcela = rendaMensal * LIMITE_PARCELA_RENDA;

        if (amortizacao == TipoAmortizacao.PRICE) {
            Price price = new Price();
            List<Double> parcelas = price.calculaParcela(valorFinanciamento, taxaJurosMensal, prazo);
            List<Double> amortizacoes = price.calculaAmortizacao(valorFinanciamento, taxaJurosMensal, prazo);


            if (parcelas.getFirst() > limiteParcela || amortizacoes.getFirst() > limiteParcela) {
                FinanciamentoView.imprimirErro("Financiamento não aprovado! A primeira parcela ou amortização excede 30% da renda mensal.");
                return;
            }

            FinanciamentoView.imprimirTabela(parcelas, amortizacoes, prazo);
            System.out.println();
            FinanciamentoView.imprimirValorTotal(price.totalPagoPrice(valorFinanciamento, taxaJurosMensal, prazo));

        }

        if (amortizacao == TipoAmortizacao.SAC) {
            SAC sac = new SAC();
            List<Double> parcelasSac = sac.calculaParcela(valorFinanciamento, taxaJurosMensal, prazo);
            List<Double> amortizacaoSac = sac.calculaAmortizacao(valorFinanciamento, taxaJurosMensal, prazo);

            FinanciamentoView.imprimirValorTotal(sac.totalPagoSac(valorFinanciamento, taxaJurosMensal, prazo));

            if (parcelasSac.getFirst() > limiteParcela || amortizacaoSac.getFirst() > limiteParcela) {
                FinanciamentoView.imprimirErro("Financiamento nao aprovado! A primeira parcela ou amortizacao excede 30% da renda mensal.");
                return;
            }

            FinanciamentoView.imprimirTabela(parcelasSac, amortizacaoSac, prazo);
            System.out.println();
            FinanciamentoView.imprimirValorTotal(sac.totalPagoSac(valorFinanciamento, taxaJurosMensal, prazo));

        }

    }

}
