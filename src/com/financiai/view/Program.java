package com.financiai.view;

import com.financiai.dao.Conexao;
import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.model.enums.TipoImovel;
import com.financiai.services.Price;

import java.sql.Connection;

import static com.financiai.controller.FinanciamentoController.calcularFinanciamento;

public class Program {
    public static void main(String[] args) {
        System.out.println("Testando conexão com o banco de dados...");
        try (Connection conexao = Conexao.conectar()) {
            if (conexao != null) {
                System.out.println("Conexão estabelecida com sucesso!");
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chamando o método para exibir o logo
        exibirLogo();

        double rendaMensal = 5000.00;
        double valorImovel = 300000.00;
        double valorEntrada = 60000.00;
        double taxaJurosAnual = 6.5;
        int prazo = 240;
        TipoAmortizacao amort = TipoAmortizacao.PRICE;

        calcularFinanciamento(rendaMensal, valorImovel, valorEntrada, taxaJurosAnual, prazo, amort);

    }

    public static void exibirLogo() {
        System.out.println();
        System.out.println("███████╗██╗███╗   ██╗ █████╗ ███╗   ██╗ ██████╗██╗ █████╗ ██╗");
        System.out.println("██╔════╝██║████╗  ██║██╔══██╗████╗  ██║██╔════╝██║██╔══██╗██║");
        System.out.println("█████╗  ██║██╔██╗ ██║███████║██╔██╗ ██║██║     ██║███████║██║");
        System.out.println("██╔══╝  ██║██║╚██╗██║██╔══██║██║╚██╗██║██║     ██║██╔══██║██║");
        System.out.println("██║     ██║██║ ╚████║██║  ██║██║ ╚████║╚██████╗██║██║  ██║██║");
        System.out.println("╚═╝     ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚═╝╚═╝  ╚═╝╚═╝");
        System.out.println();
        System.out.println("           Sua solução financeira inteligente!");
        System.out.println();
    }
}

