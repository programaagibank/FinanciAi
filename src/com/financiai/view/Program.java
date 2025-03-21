package com.financiai.view;

import com.financiai.controller.FinanciamentoController;
import com.financiai.dao.ClienteDAO;
import com.financiai.dao.ImovelDAO;
import com.financiai.model.entities.Cliente;
import com.financiai.model.entities.Imovel;
import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.model.enums.TipoImovel;
import com.financiai.util.Conexao;

import java.sql.Connection;

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

        // Criando os objetos DAO
        ClienteDAO clienteDAO = new ClienteDAO();
        ImovelDAO imovelDAO = new ImovelDAO();
        FinanciamentoController financiamentoController = new FinanciamentoController();

        // Dados dos clientes
        Cliente[] clientes = {
                new Cliente("João Silva", "78505544005", 5000.00),
                new Cliente("Maria Oliveira", "30827970080", 6000.00),
                new Cliente("Carlos Souza", "28536722045", 7000.00),
                new Cliente("Ana Costa", "40676005004", 8000.00),
                new Cliente("Pedro Santos", "96878994098", 9000.00)
        };

        // Dados dos imóveis
        Imovel[] imoveis = {
                new Imovel(TipoImovel.CASA, 300000.00),
                new Imovel(TipoImovel.APARTAMENTO, 400000.00),
                new Imovel(TipoImovel.APARTAMENTO, 200000.00),
                new Imovel(TipoImovel.CASA, 350000.00),
                new Imovel(TipoImovel.APARTAMENTO, 450000.00)
        };

        // Dados dos financiamentos
        double[] valoresEntrada = {60000.00, 80000.00, 40000.00, 70000.00, 90000.00};
        double[] taxasJurosAnual = {0.065, 0.07, 0.06, 0.075, 0.068};
        int prazo = 60; // 60 parcelas

        // Adicionando clientes e imóveis ao banco de dados
        for (Cliente cliente : clientes) {
            clienteDAO.adicionarCliente(cliente);
        }

        for (Imovel imovel : imoveis) {
            imovelDAO.adicionarImovel(imovel);
        }

        // Simulando os financiamentos
        for (int i = 0; i < 5; i++) {
            try {
                financiamentoController.simularFinanciamento(
                        clientes[i].getCpf(), // CPF do cliente
                        imoveis[i].getTipoImovel(), // Tipo do imóvel
                        imoveis[i].getValorImovel(), // Valor do imóvel
                        taxasJurosAnual[i], // Taxa de juros anual
                        valoresEntrada[i], // Valor de entrada
                        prazo, // Prazo (60 parcelas)
                        TipoAmortizacao.PRICE // Tipo de amortização (Price)
                );
            } catch (IllegalArgumentException e) {
                System.err.println("Erro ao simular financiamento: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Processo concluído: 5 clientes, 5 imóveis e 5 financiamentos foram criados e simulados com sucesso!");
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