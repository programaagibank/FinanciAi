package financiai.financiai.dao;

import financiai.financiai.model.Parcela;
import java.sql.*;

public class ParcelasDAO {

    public void verificarTabela(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS parcelas (" +
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

        String sql = "INSERT INTO parcelas (financiamento_id, numero_parcela, valor_parcela, " +
                "valor_amortizacao, valor_juros, saldo_devedor) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, parcela.getFinanciamentoId());
            stmt.setInt(2, parcela.getNumeroParcela());
            stmt.setDouble(3, parcela.getValorParcela());
            stmt.setDouble(4, parcela.getValorAmortizacao());
            stmt.setDouble(5, parcela.getValorJuros());
            stmt.setDouble(6, parcela.getSaldoDevedor());
            stmt.executeUpdate();
        }
    }
}