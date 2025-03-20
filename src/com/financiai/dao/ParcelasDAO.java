package com.financiai.dao;

import com.financiai.model.entities.Parcelas;
import com.financiai.util.Conexao;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParcelasDAO {
    private Connection conexao;

    public ParcelasDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaParcelas(); // Verifica e cria a tabela se não existir
    }

    // Método para criar a tabela parcelas se não existir
    private void criarTabelaParcelas() {
        String verificaTabela = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'parcelas'";
        String criaTabela = "CREATE TABLE IF NOT EXISTS parcelas (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "numero_parcela INT NOT NULL, " +
                "valor_parcela DOUBLE NOT NULL, " +
                "valor_amortizacao DOUBLE NOT NULL, " + // Adicionado o campo valor_amortizacao
                "financiamento_id INT NOT NULL)"; // Adicionado o campo financiamento_id

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(verificaTabela)) {

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Tabela 'parcelas' já existe.");
            } else {
                stmt.executeUpdate(criaTabela);
                System.out.println("Tabela 'parcelas' criada com sucesso!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar tabela 'parcelas': " + e.getMessage(), e);
        }
    }

    // Método para verificar se uma parcela já existe
    private boolean parcelaExiste(int numeroParcela, int financiamentoId) {
        String sql = "SELECT COUNT(*) FROM parcelas WHERE numero_parcela = ? AND financiamento_id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, numeroParcela);
            stmt.setInt(2, financiamentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se parcela existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para formatar valores com duas casas decimais
    private double formatarValor(double valor) {
        DecimalFormat df = new DecimalFormat("#.##"); // Formata com duas casas decimais
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US)); // Usa ponto como separador decimal
        return Double.parseDouble(df.format(valor));
    }

    // Método para adicionar uma nova parcela
    public void adicionarParcela(Parcelas parcela) {
        if (parcelaExiste(parcela.getNumeroParcela(), parcela.getFinanciamentoId())) {
            System.out.println("Parcela " + parcela.getNumeroParcela() + " já existe no banco de dados para o financiamento ID " + parcela.getFinanciamentoId() + ".");
            return;
        }

        // Validação do valor da parcela e da amortização
        if (parcela.getValorParcela() <= 0 || parcela.getValorAmortizacao() <= 0) {
            throw new IllegalArgumentException("O valor da parcela e da amortização devem ser positivos.");
        }

        // Formata os valores antes de salvar
        double valorParcelaFormatado = formatarValor(parcela.getValorParcela());
        double valorAmortizacaoFormatado = formatarValor(parcela.getValorAmortizacao());

        String sql = "INSERT INTO parcelas (numero_parcela, valor_parcela, valor_amortizacao, financiamento_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, parcela.getNumeroParcela());
            stmt.setDouble(2, valorParcelaFormatado);
            stmt.setDouble(3, valorAmortizacaoFormatado);
            stmt.setInt(4, parcela.getFinanciamentoId());
            stmt.executeUpdate();

            // Recupera o ID gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    parcela.setId(generatedKeys.getInt(1)); // Atualiza o objeto com o ID gerado
                }
            }

            System.out.println("Parcela adicionada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar parcela: " + e.getMessage(), e);
        } finally {
            fecharConexao();
        }
    }

    // Método para adicionar parcelas limitadas (primeiras e últimas)
    public void adicionarParcelasLimitadas(List<Parcelas> parcelas, int limite) {
        if (parcelas == null || parcelas.isEmpty()) {
            throw new IllegalArgumentException("A lista de parcelas não pode ser nula ou vazia.");
        }

        // Adiciona as primeiras 'limite' parcelas
        for (int i = 0; i < Math.min(limite, parcelas.size()); i++) {
            adicionarParcela(parcelas.get(i));
        }

        // Adiciona as últimas 'limite' parcelas
        for (int i = Math.max(parcelas.size() - limite, 0); i < parcelas.size(); i++) {
            adicionarParcela(parcelas.get(i));
        }
    }

    // Método para listar todas as parcelas
    public List<Parcelas> listarParcelas() {
        List<Parcelas> parcelas = new ArrayList<>();
        String sql = "SELECT * FROM parcelas";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Parcelas parcela = new Parcelas(
                        rs.getInt("numero_parcela"),
                        rs.getDouble("valor_parcela"),
                        rs.getDouble("valor_amortizacao"),
                        rs.getInt("financiamento_id")
                );
                parcelas.add(parcela);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar parcelas: " + e.getMessage(), e);
        } finally {
            fecharConexao();
        }
        return parcelas;
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