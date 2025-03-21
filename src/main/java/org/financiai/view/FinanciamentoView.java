package org.financiai.view;

import java.util.List;

public class FinanciamentoView {

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    public static void imprimirValorTotal(double valorTotal) {
        System.out.println(YELLOW + "══════════════════════════════════════" + RESET);
        System.out.printf(YELLOW + "         📌 VALOR TOTAL PAGO 📌        \n" + RESET);
        System.out.println(YELLOW + "══════════════════════════════════════" + RESET);
        System.out.printf("         💰 %sR$ %.2f%s\n", GREEN, valorTotal, RESET);
        System.out.println(YELLOW + "══════════════════════════════════════" + RESET);
    }

    public static void imprimirTabela(List<Double> parcelas, List<Double> amortizacoes, int prazo) {
        System.out.println(CYAN + "\nParcela  |   Valor   |  Amortização  |   Juros" + RESET);
        System.out.println(CYAN + "----------------------------------------------" + RESET);

        for (int i = 0; i < 5; i++) {
            double juros = parcelas.get(i) - amortizacoes.get(i);
            System.out.printf("%-8d | %s%-9.2f%s | %s%-13.2f%s | %s%-8.2f%s\n",
                    i + 1, GREEN, parcelas.get(i), RESET,
                    GREEN, amortizacoes.get(i), RESET,
                    GREEN, juros, RESET);
        }

        System.out.println(CYAN + "        ..." + RESET);

        for (int i = prazo - 5; i < prazo; i++) {
            double juros = parcelas.get(i) - amortizacoes.get(i);
            System.out.printf("%-8d | %s%-9.2f%s | %s%-13.2f%s | %s%-8.2f%s\n",
                    i + 1, GREEN, parcelas.get(i), RESET,
                    GREEN, amortizacoes.get(i), RESET,
                    GREEN, juros, RESET);
        }
    }

    public static void imprimirErro(String mensagem) {
        System.out.println(RED + "❌ " + mensagem + RESET);
    }
}
