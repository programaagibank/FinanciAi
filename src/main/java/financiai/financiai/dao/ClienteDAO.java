package financiai.financiai.dao;

import financiai.financiai.model.Cliente;

import java.sql.*;

public class ClienteDAO {

    public void adicionarCliente(Cliente cliente, Connection conexao) throws SQLException {
        // Primeiro valida o CPF
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new SQLException("CPF não pode ser nulo ou vazio");
        }

        // Verifica se o cliente já existe
        if (clienteExiste(cliente.getCpf(), conexao)) {
            throw new SQLException("Cliente com CPF " + cliente.getCpf() + " já existe.");
        }

        String sql = "INSERT INTO clientes (nome, cpf, renda_mensal) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setDouble(3, cliente.getRendaMensal());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar cliente, nenhuma linha afetada");
            }
        }
    }

    private boolean clienteExiste(String cpf, Connection conexao) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE cpf = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}