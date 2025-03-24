package org.financiai.dao;

import org.financiai.model.entities.Cliente;
import org.financiai.model.entities.Financiamento;
import org.financiai.model.entities.Imovel;
import org.financiai.model.enums.TipoAmortizacao;
import org.financiai.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanciamentoDAO {
    private Connection conexao;

    // Construtor
    public FinanciamentoDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaFinanciamentos(); // Verifica e cria a tabela se não existir
    }

    // Método para verificar se a tabela financiamentos já existe
    private boolean tabelaExiste() {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'financiamentos'";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se a tabela 'financiamentos' existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para criar a tabela financiamentos se não existir
    private void criarTabelaFinanciamentos() {
        if (tabelaExiste()) {
            System.out.println("Tabela 'financiamentos' já existe.");
            return;
        }

        String criaTabela = "CREATE TABLE financiamentos (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "cliente_cpf VARCHAR(14) NOT NULL, " +
                "cliente_nome VARCHAR(100) NOT NULL, " +
                "cliente_renda_mensal DOUBLE NOT NULL, " +
                "tipo_imovel VARCHAR(50) NOT NULL, " +
                "valor_imovel DOUBLE NOT NULL, " +
                "valor_entrada DOUBLE NOT NULL, " +
                "valor_financiado DOUBLE NOT NULL, " +
                "taxa_juros DOUBLE NOT NULL, " +
                "prazo INT NOT NULL, " +
                "tipo_amortizacao VARCHAR(50) NOT NULL, " +
                "total_pagar DOUBLE NOT NULL, " +
                "FOREIGN KEY (cliente_cpf) REFERENCES clientes(cpf)" +
                ")";

        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(criaTabela);
            System.out.println("Tabela 'financiamentos' criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela 'financiamentos': " + e.getMessage(), e);
        }
    }

    // Método para adicionar um financiamento
    public void adicionarFinanciamento(Financiamento financiamento, Cliente cliente, Imovel imovel) throws SQLException {
        String sql = "INSERT INTO financiamentos (cliente_cpf, cliente_nome, cliente_renda_mensal, tipo_imovel, valor_imovel, valor_entrada, valor_financiado, taxa_juros, prazo, tipo_amortizacao, total_pagar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conexao.setAutoCommit(false); // Inicia a transação

            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setDouble(3, cliente.getRendaMensal());
            stmt.setString(4, imovel.getTipoImovel().toString());
            stmt.setDouble(5, imovel.getValorImovel());
            stmt.setDouble(6, financiamento.getValorEntrada());
            stmt.setDouble(7, financiamento.getValorFinanciado());
            stmt.setDouble(8, financiamento.getTaxaJuros());
            stmt.setInt(9, financiamento.getPrazo());
            stmt.setString(10, financiamento.getTipoAmortizacao().toString());
            stmt.setDouble(11, financiamento.getTotalPagar());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    financiamento.setFinanciamentoId(generatedKeys.getInt(1));
                    System.out.println("Financiamento salvo com ID: " + financiamento.getFinanciamentoId()); // Log para depuração
                }
            }

            conexao.commit(); // Confirma a transação
        } catch (SQLException e) {
            conexao.rollback(); // Reverte a transação em caso de erro
            throw new RuntimeException("Erro ao adicionar financiamento: " + e.getMessage(), e);
        } finally {
            conexao.setAutoCommit(true); // Restaura o modo de autocommit
        }
    }

    // Método para listar todos os financiamentos
    public List<Financiamento> listarFinanciamentos() {
        List<Financiamento> financiamentos = new ArrayList<>();
        String sql = "SELECT * FROM financiamentos";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Financiamento financiamento = new Financiamento(
                        rs.getInt("prazo"),
                        rs.getDouble("taxa_juros"),
                        TipoAmortizacao.valueOf(rs.getString("tipo_amortizacao")),
                        rs.getDouble("valor_entrada"),
                        rs.getDouble("valor_financiado"),
                        rs.getDouble("total_pagar") // Adicionado o total_pagar
                );
                financiamento.setFinanciamentoId(rs.getInt("id")); // Define o ID do financiamento
                financiamentos.add(financiamento);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar financiamentos: " + e.getMessage(), e);
        }
        return financiamentos;
    }

    // Método para fechar a conexão
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com o banco de dados encerrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão: " + e.getMessage(), e);
        }
    }
}