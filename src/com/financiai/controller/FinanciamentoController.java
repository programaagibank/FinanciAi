package com.financiai.controller;

import com.financiai.dao.*;
import com.financiai.model.enums.TipoImovel;
import com.financiai.util.GeradorPDF;
import com.financiai.model.entities.Cliente;
import com.financiai.model.entities.Financiamento;
import com.financiai.model.entities.Imovel;
import com.financiai.model.entities.Parcelas;
import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.services.Amortizacao;
import com.financiai.services.Price;
import com.financiai.services.SAC;
import com.financiai.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    // Método para gerar um novo ID de financiamento
    public int gerarNovoIdFinanciamento() throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM financiamentos";
        int novoId = 1;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                novoId = rs.getInt("max_id") + 1;
            }
        }

        return novoId;
    }

    // Método para simular um financiamento
    public void simularFinanciamento(String clienteCpf, TipoImovel tipoImovel, double valorTotalImovel, double taxaJuros, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            // Buscar cliente e imóvel
            Cliente cliente = buscarCliente(clienteCpf);
            Imovel imovel = buscarImovel(tipoImovel);

            // Calcular financiamento
            Financiamento financiamento = calcularFinanciamento(valorTotalImovel, taxaJuros, valorEntrada, prazo, tipoAmortizacao);

            // Salvar financiamento e parcelas
            salvarFinanciamento(conn, financiamento);
            List<Parcelas> parcelas = calcularESalvarParcelas(conn, financiamento);

            // Commit da transação
            conn.commit();

            // Gerar PDF
           // GeradorPDF.gerarPDF(financiamento, cliente, parcelas);

        } catch (Exception e) {
            System.err.println("Erro ao simular financiamento: " + e.getMessage());
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
    private Financiamento calcularFinanciamento(double valorTotalImovel, double taxaJuros, double valorEntrada, int prazo, TipoAmortizacao tipoAmortizacao) {
        // O QUE ESTAVA ERRADO: Não havia validação dos valores de financiamento antes de calcular.
        // O QUE FOI CORRIGIDO: Adicionei validações para garantir que os valores sejam positivos.
        if (valorTotalImovel <= 0 || taxaJuros <= 0 || valorEntrada < 0 || prazo <= 0) {
            throw new IllegalArgumentException("Valores de financiamento, taxa de juros, entrada e prazo devem ser positivos.");
        }

        double valorFinanciado = valorTotalImovel - valorEntrada;
        Amortizacao amortizacao = tipoAmortizacao == TipoAmortizacao.PRICE ? new Price() : new SAC();
        double totalPagar = amortizacao.calculaParcela(valorFinanciado, taxaJuros, prazo).stream().mapToDouble(Double::doubleValue).sum();

        Financiamento financiamento = new Financiamento(prazo, taxaJuros, tipoAmortizacao, valorEntrada, valorFinanciado);
        financiamento.setTotalPagar(totalPagar);

        return financiamento;
    }

    // Método para salvar o financiamento no banco de dados
    private void salvarFinanciamento(Connection conn, Financiamento financiamento) throws SQLException {
        financiamentoDAO.adicionarFinanciamento(financiamento);
    }

    // Método para calcular e salvar as parcelas
    private List<Parcelas> calcularESalvarParcelas(Connection conn, Financiamento financiamento) throws SQLException {
        // Obtém a estratégia de amortização (Price ou SAC)
        Amortizacao amortizacao = financiamento.getTipoAmortizacao() == TipoAmortizacao.PRICE ? new Price() : new SAC();

        // Calcula os valores das parcelas
        List<Double> valoresParcelas = amortizacao.calculaParcela(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo());

        // Calcula os valores da amortização
        List<Double> valoresAmortizacao = amortizacao.calculaAmortizacao(financiamento.getValorFinanciado(), financiamento.getTaxaJuros(), financiamento.getPrazo());

        // Cria a lista de parcelas
        List<Parcelas> parcelas = new ArrayList<>();

        // Preenche a lista de parcelas
        for (int i = 0; i < valoresParcelas.size(); i++) {
            // Cria uma nova parcela com número, valor da parcela, valor da amortização e ID do financiamento
            Parcelas parcela = new Parcelas(
                    i + 1, // Número da parcela
                    valoresParcelas.get(i), // Valor da parcela
                    valoresAmortizacao.get(i), // Valor da amortização
                    financiamento.getFinanciamentoId() // ID do financiamento
            );
            parcelas.add(parcela);
        }

        // Salva as parcelas no banco de dados (apenas as primeiras e últimas)
        parcelasDAO.adicionarParcelasLimitadas(parcelas, NUMERO_PARCELAS_SALVAR);

        return parcelas;
    }

    // Método para listar todos os financiamentos
    public void listarFinanciamentos() {
        try {
            System.out.println("Lista de Financiamentos:");
            financiamentoDAO.listarFinanciamentos().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("Erro ao listar financiamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para listar todas as parcelas de um financiamento
    public void listarParcelas(int financiamentoId) {
        try {
            System.out.println("Parcelas do Financiamento ID " + financiamentoId + ":");
            parcelasDAO.listarParcelas().stream()
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
            parcelasDAO.fecharConexao();
            clienteDAO.fecharConexao();
            imovelDAO.fecharConexao();
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }
}