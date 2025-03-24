package org.financiai.dao;

import org.financiai.model.entities.Cliente;
import org.financiai.util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection conexao;

    public ClienteDAO() {
        conexao = Conexao.conectar(); // Conecta ao banco de dados
        criarTabelaClientes(); // Verifica e cria a tabela se não existir
    }

    // Método para verificar se a tabela clientes já existe
    private boolean tabelaExiste() {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'clientes'";
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se a tabela 'clientes' existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para criar a tabela clientes se não existir
    private void criarTabelaClientes() {
        if (tabelaExiste()) {
            System.out.println("Tabela 'clientes' já existe.");
            return;
        }

        String criaTabela = "CREATE TABLE clientes (" +
                "cpf VARCHAR(14) PRIMARY KEY, " + // CPF como chave primária
                "nome VARCHAR(100) NOT NULL, " +
                "renda_mensal DOUBLE NOT NULL)";

        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(criaTabela);
            System.out.println("Tabela 'clientes' criada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela 'clientes': " + e.getMessage(), e);
        }
    }

    // Método para verificar se um cliente já existe
    private boolean clienteExiste(String cpf) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cpf = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar se cliente existe: " + e.getMessage(), e);
        }
        return false;
    }

    // Método para adicionar um cliente
    public void adicionarCliente(Cliente cliente) {
        // Validação do CPF
        if (!validarCPF(cliente.getCpf())) {
            throw new IllegalArgumentException("CPF inválido. O CPF deve ter 11 dígitos ou 14 caracteres (com máscara).");
        }

        if (clienteExiste(cliente.getCpf())) {
            System.out.println("Cliente com CPF " + cliente.getCpf() + " já existe no banco de dados.");
            return;
        }

        String sql = "INSERT INTO clientes (cpf, nome, renda_mensal) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setDouble(3, cliente.getRendaMensal());
            stmt.executeUpdate();
            System.out.println("Cliente adicionado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar cliente: " + e.getMessage(), e);
        }
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDouble("renda_mensal")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    // Método para buscar um cliente por CPF
    public Cliente buscarClientePorCpf(String cpf) {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getDouble("renda_mensal")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
        }
        return null; // Retorna null se o cliente não for encontrado
    }
    // Método para validar o CPF
    private boolean validarCPF(String cpf) {
        // Verifica se o CPF é nulo
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem 11 dígitos (sem máscara) ou 14 caracteres (com máscara)
        return cpf.length() == 11 || cpf.length() == 14;
    }

    // Método para fechar a conexão
    public void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão fechada com sucesso.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão: " + e.getMessage(), e);
        }
    }


}

