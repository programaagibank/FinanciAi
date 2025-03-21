package org.financiai.dao;

import org.financiai.model.entities.Imovel;
import org.financiai.model.enums.TipoImovel;
import org.financiai.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImovelDAO {
    private Connection conexao;

    public ImovelDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaImoveis(); // Verifica e cria a tabela se não existir
    }

    // Método para criar a tabela imoveis se não existir
    private void criarTabelaImoveis() {
        String verificaTabela = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'imoveis'";
        String criaTabela = "CREATE TABLE IF NOT EXISTS imoveis (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " + // Adicionado uma chave primária autoincrementada
                "tipo_imovel VARCHAR(50) NOT NULL, " +
                "valor_imovel DOUBLE NOT NULL)";

        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(verificaTabela)) {

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Tabela 'imoveis' já existe.");
            } else {
                stmt.executeUpdate(criaTabela);
                System.out.println("Tabela 'imoveis' criada com sucesso!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar tabela 'imoveis': " + e.getMessage(), e);
        }
    }

    // Método para adicionar um imóvel
    public void adicionarImovel(Imovel imovel) {
        // Validação do valor do imóvel
        if (imovel.getValorImovel() <= 0) {
            throw new IllegalArgumentException("O valor do imóvel deve ser positivo.");
        }

        String sql = "INSERT INTO imoveis (tipo_imovel, valor_imovel) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, imovel.getTipoImovel().toString());
            stmt.setDouble(2, imovel.getValorImovel());
            stmt.executeUpdate();
            System.out.println("Imóvel adicionado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar imóvel: " + e.getMessage(), e);
        }
    }

    // Método para listar todos os imóveis
    public List<Imovel> listarImoveis() {
        List<Imovel> imoveis = new ArrayList<>();
        String sql = "SELECT * FROM imoveis";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Imovel imovel = new Imovel(
                        TipoImovel.valueOf(rs.getString("tipo_imovel")),
                        rs.getDouble("valor_imovel")
                );
                imoveis.add(imovel);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar imóveis: " + e.getMessage(), e);
        }
        return imoveis;
    }

    // Método para buscar imóveis por tipo
    public List<Imovel> buscarImoveisPorTipo(TipoImovel tipo) {
        List<Imovel> imoveis = new ArrayList<>();
        String sql = "SELECT * FROM imoveis WHERE tipo_imovel = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tipo.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Imovel imovel = new Imovel(
                            TipoImovel.valueOf(rs.getString("tipo_imovel")),
                            rs.getDouble("valor_imovel")
                    );
                    imoveis.add(imovel);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar imóveis por tipo: " + e.getMessage(), e);
        }
        return imoveis;
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