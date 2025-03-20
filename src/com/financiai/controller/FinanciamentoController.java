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
import java.time.LocalDate;
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
        Connection conn = null;
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false); // Inicia a transação

            // Recuperar informações do cliente e do imóvel
            Cliente cliente = clienteDAO.buscarClientePorCpf(clienteCpf); // Busca cliente por CPF
            List<Imovel> imoveis = imovelDAO.buscarImoveisPorTipo(tipoImovel); // Busca imóveis por tipo

            // Verifica se o cliente foi encontrado
            if (cliente == null) {
                throw new RuntimeException("Cliente não encontrado com o CPF: " + clienteCpf);
            }

            // Verifica se há imóveis do tipo especificado
            if (imoveis.isEmpty()) {
                throw new RuntimeException("Nenhum imóvel encontrado do tipo: " + tipoImovel);
            }

            // Seleciona o primeiro imóvel da lista (ou outro critério, se necessário)
            Imovel imovel = imoveis.get(0);

            // Calcular o valor financiado (valor total do imóvel menos a entrada)
            double valorFinanciado = valorTotalImovel - valorEntrada;

            // Calcular o total a pagar e as parcelas
            Amortizacao amortizacao = tipoAmortizacao == TipoAmortizacao.PRICE ? new Price() : new SAC();
            double totalPagar = calcularTotalPagar(valorFinanciado, taxaJuros, prazo, amortizacao);

            // Gerar um novo ID para o financiamento
            int financiamentoId = gerarNovoIdFinanciamento();

            // Criar o financiamento
            Financiamento financiamento = new Financiamento(prazo, taxaJuros, tipoAmortizacao, valorEntrada, valorFinanciado, financiamentoId);
            financiamento.setTotalPagar(totalPagar);

            // Salvar o financiamento no banco de dados
            financiamentoDAO.adicionarFinanciamento(financiamento);

            // Calcular as parcelas
            List<Parcelas> parcelas = amortizacao.calcularParcelas(financiamento);

            // Salvar apenas as 5 primeiras e as 5 últimas parcelas
            parcelasDAO.adicionarParcelasLimitadas(parcelas, NUMERO_PARCELAS_SALVAR);

            // Commit da transação
            conn.commit();

            // Gerar o PDF com todas as parcelas e informações do cliente/imóvel
            GeradorPDF.gerarPDF(
                    financiamento.getFinanciamentoId(),
                    cliente.getNome(),
                    valorTotalImovel,
                    valorEntrada,
                    imovel.getTipoImovel(),
                    valorFinanciado,
                    totalPagar,
                    totalPagar - valorFinanciado,
                    totalPagar,
                    totalPagar - valorFinanciado,
                    parcelas
            );

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback em caso de erro
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Erro ao simular financiamento: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura o modo de autocommit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // Método para calcular o total a pagar
    private double calcularTotalPagar(double valorFinanciado, double taxaJuros, int prazo, Amortizacao amortizacao) {
        double totalPagar = 0;
        try {
            for (int i = 1; i <= prazo; i++) {
                double valorParcela = amortizacao.calculaParcela(valorFinanciado, taxaJuros, prazo, i);
                totalPagar += valorParcela;
            }
        } catch (Exception e) {
            System.err.println("Erro ao calcular o total a pagar: " + e.getMessage());
            e.printStackTrace();
        }
        return totalPagar;
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