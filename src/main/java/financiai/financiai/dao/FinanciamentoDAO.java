package financiai.financiai.dao;

import financiai.financiai.model.Financiamento;

import java.sql.*;
import java.time.LocalDate;

public class FinanciamentoDAO {
    public void adicionarFinanciamento(Financiamento financiamento, Connection conexao) throws SQLException {
        String sql = "INSERT INTO financiamentos ( imovel_id, taxa_juros, valor_entrada, " +
                "valor_financiado, prazo, 5ipo_amortizacao, total_pagar, data_simulacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Validações
            if (financiamento.getClienteId() <= 0) throw new SQLException("ID do cliente inválido");
            if (financiamento.getImovelId() <= 0) throw new SQLException("ID do imóvel inválido");


            stmt.setInt(2, financiamento.getImovelId());
            stmt.setDouble(3, financiamento.getTaxaJuros());
            stmt.setDouble(4, financiamento.getValorEntrada());
            stmt.setDouble(5, financiamento.getValorFinanciado());
            stmt.setInt(6, financiamento.getPrazo());
            stmt.setString(7, financiamento.getTipoAmortizacao().name()); // Usar name() em vez de toString()
            stmt.setDouble(8, financiamento.getTotalPagar());
            stmt.setDate(9, java.sql.Date.valueOf(
                    financiamento.getDataSimulacao() != null ?
                            financiamento.getDataSimulacao() :
                            LocalDate.now()
            ));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar financiamento, nenhuma linha afetada");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    financiamento.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter ID do financiamento");
                }
            }
        }
    }

}