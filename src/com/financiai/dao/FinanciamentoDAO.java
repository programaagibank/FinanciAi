package com.financiai.dao;

import com.financiai.model.entities.Financiamento;
import com.financiai.model.enums.TipoAmortizacao;
import com.financiai.util.Conexao;

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
                "valor_financiado DOUBLE NOT NULL, " +
                "taxa_juros DOUBLE NOT NULL, " +
                "valor_entrada DOUBLE NOT NULL, " +
                "prazo INT NOT NULL, " +
                "tipo_amortizacao VARCHAR(50) NOT NULL, " +
                "total_pagar DOUBLE NOT NULL)";

        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(criaTabela);
            System.out.println("Tabela 'financiamentos' criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela 'financiamentos': " + e.getMessage(), e);
        }
    }

    // Método para verificar se um financiamento já existe
    private boolean financiamentoExiste(int id) {
        String sql = "SELECT COUNT(*) FROM financiamentos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se financiamento existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para adicionar um financiamento
    public void adicionarFinanciamento(Financiamento financiamento) {
        if (financiamentoExiste(financiamento.getFinanciamentoId())) {
            System.out.println("Financiamento com ID " + financiamento.getFinanciamentoId() + " já existe no banco de dados.");
            return;
        }

        // O QUE ESTAVA ERRADO: Não havia validação dos valores de financiamento antes de inserir no banco de dados.
        // O QUE FOI CORRIGIDO: Adicionei validações para garantir que os valores sejam positivos.
        if (financiamento.getValorFinanciado() <= 0 || financiamento.getTaxaJuros() <= 0 || financiamento.getValorEntrada() < 0 || financiamento.getPrazo() <= 0) {
            throw new IllegalArgumentException("Valores de financiamento, taxa de juros, entrada e prazo devem ser positivos.");
        }

        String sql = "INSERT INTO financiamentos (valor_financiado, taxa_juros, valor_entrada, prazo, tipo_amortizacao, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, financiamento.getValorFinanciado());
            stmt.setDouble(2, financiamento.getTaxaJuros());
            stmt.setDouble(3, financiamento.getValorEntrada());
            stmt.setInt(4, financiamento.getPrazo());
            stmt.setString(5, financiamento.getTipoAmortizacao().toString());
            stmt.setDouble(6, financiamento.getTotalPagar());
            stmt.executeUpdate();

            // Recupera o ID gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    financiamento.setFinanciamentoId(generatedKeys.getInt(1)); // Atualiza o objeto com o ID gerado
                }
            }

            System.out.println("Financiamento adicionado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar financiamento: " + e.getMessage(), e);
        } finally {
            // O QUE ESTAVA ERRADO: O método fecharConexao() não era chamado após as operações no banco de dados.
            // O QUE FOI CORRIGIDO: Adicionei o fechamento da conexão no bloco finally para garantir que a conexão seja fechada.
            fecharConexao();
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
                        rs.getDouble("valor_financiado")
                );
                financiamento.setTotalPagar(rs.getDouble("total_pagar"));
                financiamentos.add(financiamento);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar financiamentos: " + e.getMessage(), e);
        } finally {
            // O QUE ESTAVA ERRADO: O método fecharConexao() não era chamado após as operações no banco de dados.
            // O QUE FOI CORRIGIDO: Adicionei o fechamento da conexão no bloco finally para garantir que a conexão seja fechada.
            fecharConexao();
        }
        return financiamentos;
    }

    // Método para buscar um financiamento por ID
    public Financiamento buscarFinanciamentoPorId(int id) {
        String sql = "SELECT * FROM financiamentos WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Financiamento financiamento = new Financiamento(
                            rs.getInt("prazo"),
                            rs.getDouble("taxa_juros"),
                            TipoAmortizacao.valueOf(rs.getString("tipo_amortizacao")),
                            rs.getDouble("valor_entrada"),
                            rs.getDouble("valor_financiado")
                    );
                    financiamento.setTotalPagar(rs.getDouble("total_pagar"));
                    return financiamento;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar financiamento por ID: " + e.getMessage(), e);
        } finally {
            // O QUE ESTAVA ERRADO: O método fecharConexao() não era chamado após as operações no banco de dados.
            // O QUE FOI CORRIGIDO: Adicionei o fechamento da conexão no bloco finally para garantir que a conexão seja fechada.
            fecharConexao();
        }
        return null; // Retorna null se o financiamento não for encontrado
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