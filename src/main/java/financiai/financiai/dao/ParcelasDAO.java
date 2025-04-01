package financiai.financiai.dao;

import financiai.financiai.model.Parcela;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcelasDAO {
    private static boolean tabelaVerificada = false;

    public void verificarTabela(Connection conexao) throws SQLException {
        if (!tabelaVerificada) {
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
                        "FOREIGN KEY (financiamento_id) REFERENCES financiamentos(id), " +
                        "UNIQUE KEY uk_parcela (financiamento_id, numero_parcela))"; // Adicionado constraint única
                stmt.execute(sql);
                tabelaVerificada = true;
            }
        }
    }

    // Método para verificar se parcela já existe
    private boolean parcelaExiste(Parcela parcela, Connection conexao) throws SQLException {
        String sql = "SELECT 1 FROM parcelas WHERE financiamento_id = ? AND numero_parcela = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, parcela.getFinanciamentoId());
            stmt.setInt(2, parcela.getNumeroParcela());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Método modificado para verificar existência antes de inserir
    public void adicionarParcela(Parcela parcela, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        if (!parcelaExiste(parcela, conexao)) {
            String sql = "INSERT INTO parcelas (cpf, financiamento_id, numero_parcela, valor_parcela, " +
                    "valor_amortizacao, valor_juros, saldo_devedor) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
    }

    // Método de lote com verificação de existência
    private void inserirLoteParcelas(List<Parcela> parcelas, Connection conexao) throws SQLException {
        String sql = "INSERT INTO parcelas (cpf, financiamento_id, numero_parcela, valor_parcela, " +
                "valor_amortizacao, valor_juros, saldo_devedor) VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " + // Cláusula para evitar erros de duplicação
                "valor_parcela = VALUES(valor_parcela), " +
                "valor_amortizacao = VALUES(valor_amortizacao), " +
                "valor_juros = VALUES(valor_juros), " +
                "saldo_devedor = VALUES(saldo_devedor)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            for (Parcela parcela : parcelas) {
                stmt.setString(1, parcela.getCpf());
                stmt.setInt(2, parcela.getFinanciamentoId());
                stmt.setInt(3, parcela.getNumeroParcela());
                stmt.setDouble(4, parcela.getValorParcela());
                stmt.setDouble(5, parcela.getValorAmortizacao());
                stmt.setDouble(6, parcela.getValorJuros());
                stmt.setDouble(7, parcela.getSaldoDevedor());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    // Método de busca mantido
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