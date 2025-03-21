package com.financiai.controller;

import com.financiai.dao.*;
import com.financiai.model.enums.TipoImovel;
import com.financiai.model.entities.Cliente;
import com.financiai.model.entities.Financiamento;
import com.financiai.model.entities.Imovel;
import com.financiai.model.entities.Parcelas;
import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.services.Amortizacao;
import com.financiai.services.Price;
import com.financiai.services.SAC;
import com.financiai.util.Conexao;
import com.financiai.util.GeradorPDF;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinanciamentoController {

    private static final int NUMERO_PARCELAS_SALVAR = 5; // Número de parcelas a serem salvas (primeiras e últimas)

    private FinanciamentoDAO financiamentoDAO;
    private ParcelasDAO parcelasDAO;
    private ClienteDAO clienteDAO;
    private ImovelDAO imovelDAO;

    public FinanciamentoController() {
        this.financiamentoDAO = new FinanciamentoDAO();
        this.parcelasDAO = new ParcelasDAO();
        this.clienteDAO = new ClienteDAO();
        this.imovelDAO = new ImovelDAO();
    }

    public void simularFinanciamento(String clienteCpf, TipoImovel tipoImovel, double valorTotalImovel, double taxaJurosAnual, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
        Connection conn = null;
        try {
            conn = Conexao.conectar(); // Abre a conexão manualmente
            conn.setAutoCommit(false); // Inicia a transação

            // Validações iniciais
            if (valorTotalImovel <= 0 || taxaJurosAnual <= 0 || valorEntrada < 0 || prazo <= 0) {
                throw new IllegalArgumentException("Valores de financiamento, taxa de juros, entrada e prazo devem ser positivos.");
            }

            if (valorEntrada >= valorTotalImovel) {
                throw new IllegalArgumentException("O valor de entrada não pode ser maior ou igual ao valor total do imóvel.");
            }

            // Buscar cliente e imóvel
            Cliente cliente = buscarCliente(clienteCpf);
            Imovel imovel = buscarImovel(tipoImovel);

            // Converte a taxa de juros anual para mensal (em formato decimal)
            double taxaJurosMensal = taxaJurosAnual / 12;
            System.out.println("Taxa de juros mensal: " + taxaJurosMensal); // Log para depuração

            // Calcular financiamento
            Financiamento financiamento = calcularFinanciamento(valorTotalImovel, taxaJurosMensal, valorEntrada, prazo, tipoAmortizacao);
            System.out.println("Financiamento calculado: " + financiamento); // Log para depuração

            // Salvar financiamento e parcelas
            salvarFinanciamento(conn, financiamento, cliente, imovel);
            List<Parcelas> parcelas = calcularESalvarParcelas(conn, financiamento);
            System.out.println("Parcelas calculadas e salvas: " + parcelas.size()); // Log para depuração

            // Commit da transação
            conn.commit();

            // Gerar PDF com as informações do financiamento
            gerarPDF(financiamento, cliente, imovel, parcelas);

        } catch (Exception e) {
            System.err.println("Erro ao simular financiamento: " + e.getMessage());
            e.printStackTrace();

            // Rollback em caso de erro
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } finally {
            // Fechar a conexão manualmente
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // Método para gerar o PDF
    private void gerarPDF(Financiamento financiamento, Cliente cliente, Imovel imovel, List<Parcelas> parcelas) {
        try {
            // Calcula os totais pagos e juros totais para Price e SAC
            Amortizacao price = new Price();
            List<Double> parcelasPrice = price.calculaParcela(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo());
            double totalPagoPrice = parcelasPrice.stream().mapToDouble(Double::doubleValue).sum();
            double jurosTotaisPrice = totalPagoPrice - financiamento.getValorFinanciado();

            Amortizacao sac = new SAC();
            List<Double> parcelasSAC = sac.calculaParcela(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo());
            double totalPagoSAC = parcelasSAC.stream().mapToDouble(Double::doubleValue).sum();
            double jurosTotaisSAC = totalPagoSAC - financiamento.getValorFinanciado();

            // Chama o gerador de PDF
            GeradorPDF.gerarPDF(
                    financiamento.getFinanciamentoId(), // ID do financiamento
                    cliente.getNome(), // Nome do cliente
                    imovel.getValorImovel(), // Valor total do imóvel
                    financiamento.getValorEntrada(), // Valor de entrada
                    imovel.getTipoImovel(), // Tipo de imóvel
                    financiamento.getValorFinanciado(), // Valor financiado
                    totalPagoPrice, // Total pago no Price
                    jurosTotaisPrice, // Juros totais no Price
                    totalPagoSAC, // Total pago no SAC
                    jurosTotaisSAC, // Juros totais no SAC
                    parcelas // Lista de parcelas
            );

        } catch (Exception e) {
            System.err.println("Erro ao gerar o PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para buscar cliente por CPF
    private Cliente buscarCliente(String clienteCpf) {
        Cliente cliente = clienteDAO.buscarClientePorCpf(clienteCpf);
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado com o CPF: " + clienteCpf);
        }
        return cliente;
    }

    // Método para buscar imóvel por tipo
    private Imovel buscarImovel(TipoImovel tipoImovel) {
        List<Imovel> imoveis = imovelDAO.buscarImoveisPorTipo(tipoImovel);
        if (imoveis.isEmpty()) {
            throw new RuntimeException("Nenhum imóvel encontrado do tipo: " + tipoImovel);
        }
        return imoveis.get(0); // Seleciona o primeiro imóvel da lista
    }

    // Método para calcular o financiamento
    private Financiamento calcularFinanciamento(double valorTotalImovel, double taxaJurosMensal, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
        double valorFinanciado = valorTotalImovel - valorEntrada;

        Amortizacao amortizacao = tipoAmortizacao == TipoAmortizacao.PRICE ? new Price() : new SAC();
        double totalPagar = amortizacao.calculaParcela(valorFinanciado, taxaJurosMensal, prazo).stream().mapToDouble(Double::doubleValue).sum();

        Financiamento financiamento = new Financiamento(prazo, taxaJurosMensal, tipoAmortizacao, valorEntrada, valorFinanciado);
        financiamento.setTotalPagar(totalPagar);

        return financiamento;
    }

    // Método para salvar o financiamento no banco de dados
    private void salvarFinanciamento(Connection conn, Financiamento financiamento, Cliente cliente, Imovel imovel) throws SQLException {
        financiamentoDAO.adicionarFinanciamento(conn, financiamento, cliente, imovel);
    }

    // Método para calcular e salvar as parcelas
    private List<Parcelas> calcularESalvarParcelas(Connection conn, Financiamento financiamento) throws SQLException {
        Amortizacao amortizacao = financiamento.getTipoAmortizacao() == TipoAmortizacao.PRICE ? new Price() : new SAC();

        List<Double> valoresParcelas = amortizacao.calculaParcela(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo());
        List<Double> valoresAmortizacao = amortizacao.calculaAmortizacao(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo());

        List<Parcelas> parcelas = new ArrayList<>();
        for (int i = 0; i < valoresParcelas.size(); i++) {
            Parcelas parcela = new Parcelas(
                    i + 1,
                    valoresParcelas.get(i),
                    valoresAmortizacao.get(i),
                    financiamento.getFinanciamentoId()
            );
            parcelas.add(parcela);
        }

        parcelasDAO.adicionarParcelasLimitadas(conn, parcelas, NUMERO_PARCELAS_SALVAR);
        return parcelas;
    }


    // Método para listar todos os financiamentos
    public void listarFinanciamentos() {
        try (Connection conn = Conexao.conectar()) {
            System.out.println("Lista de Financiamentos:");
            financiamentoDAO.listarFinanciamentos(conn).forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erro ao listar financiamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para listar todas as parcelas de um financiamento
    public void listarParcelas(int financiamentoId) {
        try (Connection conn = Conexao.conectar()) {
            System.out.println("Parcelas do Financiamento ID " + financiamentoId + ":");
            parcelasDAO.listarParcelas(conn).stream()
                    .filter(p -> p.getFinanciamentoId() == financiamentoId)
                    .forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erro ao listar parcelas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para fechar as conexões
    public void fecharConexoes() {
        try {
            financiamentoDAO.fecharConexao();
            clienteDAO.fecharConexao();
            imovelDAO.fecharConexao();
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }
}