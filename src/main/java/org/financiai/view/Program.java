package org.financiai.view;

import org.financiai.model.entities.Cliente;
import org.financiai.model.entities.Financiamento;
import org.financiai.model.entities.Imovel;
import org.financiai.model.enums.TipoAmortizacao;
import org.financiai.model.enums.TipoImovel;
import org.financiai.util.Conexao;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.sql.Connection;

import static org.financiai.controller.FinanciamentoController.calcularFinanciamento;

public class Program {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
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
        Cliente cliente = new Cliente("Marcus", "12345678900", rendaMensal);
        Financiamento financiamento = new Financiamento(prazo, taxaJurosAnual, amortizacao, valorEntrada, valorImovel-valorEntrada);

        //método responsável por dar o "start" nas contas e chamar os métodos de impressão.
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

