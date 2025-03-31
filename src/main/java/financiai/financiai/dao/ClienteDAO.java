package financiai.financiai.dao;

import financiai.financiai.model.Cliente;
import java.sql.*;

public class ClienteDAO {

    public void verificarTabela(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "cpf VARCHAR(14) NOT NULL UNIQUE, " +
                    "renda_mensal DECIMAL(10,2) NOT NULL)";
            stmt.execute(sql);
            System.out.println("Tabela clientes verificada/criada");
        }
    }

    public void adicionarCliente(Cliente cliente, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        String sql = "INSERT INTO clientes (nome, cpf, renda_mensal) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setDouble(3, cliente.getRenda_mensal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public Cliente buscarClientePorCpf(String cpf, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setRenda_mensal(rs.getDouble("renda_mensal"));
                return cliente;
            }
            return null;
        }
    }
}