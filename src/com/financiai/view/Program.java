package com.financiai.view;

import com.financiai.util.Conexao;
import com.financiai.model.entities.Cliente;
import com.financiai.model.entities.Financiamento;
import com.financiai.model.entities.Imovel;
import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.model.enums.TipoImovel;

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

        double rendaMensal = 8000.00;
        double valorImovel = 300000.00;
        double valorEntrada = 60000.00;
        double taxaJurosAnual = 6.5;
        int prazo = 240;

        TipoAmortizacao amortizacao = TipoAmortizacao.PRICE;
        TipoImovel imovelType  = TipoImovel.CASA;

        Imovel imovel = new Imovel(imovelType, valorImovel);
        Cliente cliente = new Cliente("Marcus", rendaMensal);
        Financiamento financiamento = new Financiamento(prazo, taxaJurosAnual, amortizacao, valorEntrada, valorImovel-valorEntrada);

        calcularFinanciamento(cliente.getRendaMensal(), imovel.getValorImovel(), financiamento.getValorEntrada(),
                              financiamento.getTaxaJuros(), financiamento.getPrazo(), amortizacao);

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
        System.out.println();
    }
}

