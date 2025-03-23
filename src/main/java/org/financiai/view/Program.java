package org.financiai.view;

import org.financiai.controller.FinanciamentoController;
import org.financiai.dao.ClienteDAO;
import org.financiai.dao.FinanciamentoDAO;
import org.financiai.dao.ImovelDAO;
import org.financiai.dao.ParcelasDAO;
import org.financiai.model.entities.Cliente;
import org.financiai.model.entities.Financiamento;
import org.financiai.model.entities.Imovel;
import org.financiai.model.entities.Parcelas;
import org.financiai.model.enums.TipoAmortizacao;
import org.financiai.model.enums.TipoImovel;
import org.financiai.services.Price;
import org.financiai.services.SAC;
import org.financiai.util.GeradorPDF;

import java.util.ArrayList;
import java.util.List;

public class Program {

    // Método principal que inicia a execução do programa
    public static void main(String[] args) {

        // Exibe o logo da aplicação
        Program.exibirLogo();

        // Inicializa os DAOs (Data Access Objects) para interagir com o banco de dados
        ClienteDAO clienteDAO = new ClienteDAO();
        ImovelDAO imovelDAO = new ImovelDAO();
        FinanciamentoDAO financiamentoDAO = new FinanciamentoDAO();
        ParcelasDAO parcelasDAO = new ParcelasDAO();

        // Cria 5 clientes com nome, CPF e renda mensal
        Cliente cliente1 = new Cliente("João Silva", "123.456.789-00", 5000.0);
        Cliente cliente2 = new Cliente("Maria Oliveira", "987.654.321-00", 6000.0);
        Cliente cliente3 = new Cliente("Carlos Souza", "111.222.333-44", 7000.0);
        Cliente cliente4 = new Cliente("Ana Costa", "555.666.777-88", 8000.0);
        Cliente cliente5 = new Cliente("Pedro Rocha", "999.888.777-66", 9000.0);

        // Adiciona os clientes ao banco de dados
        clienteDAO.adicionarCliente(cliente1);
        clienteDAO.adicionarCliente(cliente2);
        clienteDAO.adicionarCliente(cliente3);
        clienteDAO.adicionarCliente(cliente4);
        clienteDAO.adicionarCliente(cliente5);

        // Cria 5 imóveis com tipo e valor
        Imovel imovel1 = new Imovel(TipoImovel.CASA, 200000.0);
        Imovel imovel2 = new Imovel(TipoImovel.APARTAMENTO, 300000.0);
        Imovel imovel3 = new Imovel(TipoImovel.APARTAMENTO, 100000.0);
        Imovel imovel4 = new Imovel(TipoImovel.CASA, 250000.0);
        Imovel imovel5 = new Imovel(TipoImovel.APARTAMENTO, 350000.0);

        // Adiciona os imóveis ao banco de dados
        imovelDAO.adicionarImovel(imovel1);
        imovelDAO.adicionarImovel(imovel2);
        imovelDAO.adicionarImovel(imovel3);
        imovelDAO.adicionarImovel(imovel4);
        imovelDAO.adicionarImovel(imovel5);

        // Lista de clientes e imóveis para facilitar a simulação de financiamentos
        Cliente[] clientes = {cliente1, cliente2, cliente3, cliente4, cliente5};
        Imovel[] imoveis = {imovel1, imovel2, imovel3, imovel4, imovel5};

        // Simula financiamentos para cada cliente (um financiamento por cliente)
        for (int i = 0; i < clientes.length; i++) {
            Cliente cliente = clientes[i];
            Imovel imovel = imoveis[i]; // Cada cliente simula um financiamento para um imóvel correspondente

            simularFinanciamento(cliente, imovel, financiamentoDAO, parcelasDAO);
        }

        // Fecha as conexões com o banco de dados
        clienteDAO.fecharConexao();
        imovelDAO.fecharConexao();
        financiamentoDAO.fecharConexao();
        parcelasDAO.fecharConexao();
    }

    // Método para simular um financiamento para um cliente e um imóvel
    private static void simularFinanciamento(Cliente cliente, Imovel imovel, FinanciamentoDAO financiamentoDAO, ParcelasDAO parcelasDAO) {
        // Calcula o valor de entrada (20% do valor do imóvel)
        double valorEntrada = imovel.getValorImovel() * 0.2;
        // Calcula o valor financiado (valor do imóvel menos a entrada)
        double valorFinanciado = imovel.getValorImovel() - valorEntrada;
        // Define a taxa de juros anual (10%)
        double taxaJurosAnual = 10.0;
        // Define o prazo do financiamento em meses (48 meses = 4 anos)
        int prazo = 48;
        // Define o tipo de amortização (Price)
        TipoAmortizacao tipoAmortizacao = TipoAmortizacao.PRICE;

        // Calcula o financiamento usando o controlador
        FinanciamentoController.calcularFinanciamento(
                cliente.getRendaMensal(),
                imovel.getValorImovel(),
                valorEntrada,
                taxaJurosAnual,
                prazo,
                tipoAmortizacao
        );

        // Calcula o valor total a ser pago
        double totalPagar = calcularTotalPagar(valorFinanciado, taxaJurosAnual / 12 / 100, prazo, tipoAmortizacao);

        // Cria o objeto Financiamento com os dados calculados
        Financiamento financiamento = new Financiamento(
                prazo,
                taxaJurosAnual,
                tipoAmortizacao,
                valorEntrada,
                valorFinanciado,
                totalPagar
        );

        try {
            // Adiciona o financiamento ao banco de dados
            financiamentoDAO.adicionarFinanciamento(financiamento, cliente, imovel);

            // Calcula as parcelas e as amortizações com base no tipo de amortização
            List<Double> parcelas;
            List<Double> amortizacoes;

            if (tipoAmortizacao == TipoAmortizacao.PRICE) {
                Price price = new Price();
                parcelas = price.calculaParcela(valorFinanciado, taxaJurosAnual / 12 / 100, prazo);
                amortizacoes = price.calculaAmortizacao(valorFinanciado, taxaJurosAnual / 12 / 100, prazo);
            } else if (tipoAmortizacao == TipoAmortizacao.SAC) {
                SAC sac = new SAC();
                parcelas = sac.calculaParcela(valorFinanciado, taxaJurosAnual / 12 / 100, prazo);
                amortizacoes = sac.calculaAmortizacao(valorFinanciado, taxaJurosAnual / 12 / 100, prazo);
            } else {
                throw new IllegalArgumentException("Tipo de amortizacao invalido.");
            }

            // Lista para armazenar as parcelas que serão salvas no banco de dados (apenas 5 primeiras e 5 últimas)
            List<Parcelas> parcelasParaBanco = new ArrayList<>();

            // Lista para armazenar todas as parcelas para o PDF
            List<Parcelas> parcelasParaPDF = new ArrayList<>();

            // Itera sobre todas as parcelas
            for (int i = 0; i < parcelas.size(); i++) {
                // Cria o objeto Parcelas para cada parcela
                Parcelas parcela = new Parcelas(
                        i + 1, // Número da parcela
                        parcelas.get(i), // Valor da parcela
                        amortizacoes.get(i), // Valor da amortização
                        financiamento.getFinanciamentoId() // ID do financiamento
                );

                // Adiciona a parcela à lista do PDF (todas as parcelas)
                parcelasParaPDF.add(parcela);

                // Adiciona apenas as 5 primeiras e as 5 últimas parcelas ao banco de dados
                if (i < 5 || i >= parcelas.size() - 5) {
                    parcelasParaBanco.add(parcela);
                    parcelasDAO.adicionarParcela(parcela); // Salva no banco de dados
                }
            }

            // Gera o PDF com todas as parcelas
            GeradorPDF.gerarPDF(financiamento, cliente, imovel, parcelasParaPDF);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para calcular o valor total a ser pago com base no tipo de amortização
    private static double calcularTotalPagar(double valorFinanciado, double taxaJurosMensal, int prazo, TipoAmortizacao tipoAmortizacao) {
        if (tipoAmortizacao == TipoAmortizacao.PRICE) {
            // Fórmula do Price: Valor da parcela * número de parcelas
            double valorParcela = valorFinanciado * (taxaJurosMensal / (1 - Math.pow(1 + taxaJurosMensal, -prazo)));
            return valorParcela * prazo;
        } else if (tipoAmortizacao == TipoAmortizacao.SAC) {
            // Fórmula do SAC: Soma de todas as parcelas
            double amortizacao = valorFinanciado / prazo;
            double totalPagar = 0;
            double saldoDevedor = valorFinanciado;
            for (int i = 1; i <= prazo; i++) {
                double juros = saldoDevedor * taxaJurosMensal;
                double parcela = amortizacao + juros;
                totalPagar += parcela;
                saldoDevedor -= amortizacao;
            }
            return totalPagar;
        }
        throw new IllegalArgumentException("Tipo de amortização inválido.");
    }

    // Método para exibir o logo da aplicação
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