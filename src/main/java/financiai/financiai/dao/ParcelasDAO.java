package financiai.financiai.dao;

import financiai.financiai.model.Parcela;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParcelasDAO {

    public void adicionarParcela(Parcela parcela, Connection conexao) throws SQLException {
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