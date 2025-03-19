package com.financiai.controller;

import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.services.Price;
import com.financiai.services.SAC;

import java.util.ArrayList;
import java.util.List;

public class FinanciamentoController {



    public static void calcularFinanciamento(double rendaMensal, double valorImovel, double valorEntrada,
                                             double taxaJurosAnual, int prazo, TipoAmortizacao amortizacao) {
        double valorFinanciamento = valorImovel - valorEntrada;
        double taxaJurosMensal = taxaJurosAnual / 12 / 100 ;
        double limiteParcela = rendaMensal * 0.3;

        if(amortizacao == TipoAmortizacao.PRICE){

            Price price = new Price();
            List<Double> parcelas = price.calculaParcela(valorFinanciamento, taxaJurosMensal, prazo);
            List<Double> amortizacoes = price.calculaAmortizacao(valorFinanciamento, taxaJurosMensal, prazo);

            if (parcelas.getFirst() > limiteParcela || amortizacoes.getFirst() > limiteParcela) {
                System.out.println("Financiamento não aprovado. A primeira parcela ou amortização excede 30% da renda mensal.");
                return;
            }

            System.out.println("Parcela | Valor | Amortização | Juros");
            for (int i = 0; i < 5; i++) {
                double juros = parcelas.get(i) - amortizacoes.get(i);
                System.out.printf("%d | %.2f | %.2f | %.2f\n", i + 1, parcelas.get(i), amortizacoes.get(i), juros);
            }
            System.out.println("...");
            for (int i = prazo - 5; i < prazo; i++) {
                double juros = parcelas.get(i) - amortizacoes.get(i);
                System.out.printf("%d | %.2f | %.2f | %.2f\n", i + 1, parcelas.get(i), amortizacoes.get(i), juros);
            }
        }
        if (amortizacao == TipoAmortizacao.SAC){
            SAC sac = new SAC();
            List<Double> parcelasSac = sac.calculaParcela(valorFinanciamento, taxaJurosMensal, prazo);
            List<Double> amortizacaoSac = sac.calculaAmortizacao(valorFinanciamento, taxaJurosMensal, prazo);
        }
    }

}
