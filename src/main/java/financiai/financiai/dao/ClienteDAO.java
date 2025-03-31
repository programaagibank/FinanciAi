package financiai.financiai.dao;

import financiai.financiai.model.Cliente;

import java.sql.*;

public class ClienteDAO {

    public void adicionarCliente(Cliente cliente, Connection conexao) throws SQLException {
        // Primeiro valida o CPF
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new SQLException("CPF não pode ser nulo ou vazio");
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

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                  //  cliente.setId(rs.getInt(1));
                  //esse   System.out.println("Cliente inserido com ID: " + cliente.getId());
                } else {
                    throw new SQLException("Falha ao obter ID do cliente");
                }
            }
        }
    }
}