package financiai.financiai.dao;

import financiai.financiai.model.Parcela;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcelasDAO {

    public void verificarTabela(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS parcelas (" +
                    "cpf VARCHAR(11) NOT NULL, " +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "financiamento_id INT NOT NULL, " +
                    "numero_parcela INT NOT NULL, " +
                    "valor_parcela DECIMAL(10,2) NOT NULL, " +
                    "valor_amortizacao DECIMAL(10,2) NOT NULL, " +
                    "valor_juros DECIMAL(10,2) NOT NULL, " +
                    "saldo_devedor DECIMAL(10,2) NOT NULL, " +

                    "FOREIGN KEY (financiamento_id) REFERENCES financiamentos(id))";
            stmt.execute(sql);
            System.out.println("Tabela parcelas verificada/criada");
        }
    }

    public void adicionarParcela(Parcela parcela, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        String sql = "INSERT INTO parcelas ( cpf,financiamento_id, numero_parcela, valor_parcela, " +
                "valor_amortizacao, valor_juros, saldo_devedor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, parcela.getCpf());
            stmt.setInt(2, parcela.getFinanciamentoId());
            stmt.setInt(3, parcela.getNumeroParcela());
            stmt.setDouble(4, parcela.getValorParcela());
            stmt.setDouble(5, parcela.getValorAmortizacao());
            stmt.setDouble(6, parcela.getValorJuros());
            stmt.setDouble(7, parcela.getSaldoDevedor());

            stmt.executeUpdate();
        }
    }

    public List<Parcela> buscarParcelasPorCpfCliente(String cpf, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        String sql = "SELECT p.* FROM parcelas p " +
                "JOIN financiamentos f ON p.financiamento_id = f.id " +
                "JOIN clientes c ON f.cliente_id = c.id " +
                "WHERE c.cpf = ? " +
                "ORDER BY p.financiamento_id, p.numero_parcela";

        List<Parcela> parcelas = new ArrayList<>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Parcela parcela = new Parcela(
                        rs.getInt("numero_parcela"),
                        rs.getDouble("valor_parcela"),
                        rs.getDouble("valor_amortizacao"),
                        rs.getDouble("valor_juros"),
                        rs.getDouble("saldo_devedor"),
                        rs.getString("cpf")
                );
                parcela.setFinanciamentoId(rs.getInt("financiamento_id"));
                parcelas.add(parcela);
            }
        }

        return parcelas;
    }
}