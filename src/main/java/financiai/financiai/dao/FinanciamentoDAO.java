package financiai.financiai.dao;

import financiai.financiai.model.Financiamento;
import java.sql.*;
import java.time.LocalDate;

public class FinanciamentoDAO {

    public void verificarTabela(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS financiamentos (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "cliente_id INT NOT NULL, " +
                    "imovel_id INT NOT NULL, " +
                    "taxa_juros DECIMAL(5,4) NOT NULL, " +
                    "valor_entrada DECIMAL(10,2) NOT NULL, " +
                    "valor_financiado DECIMAL(10,2) NOT NULL, " +
                    "prazo INT NOT NULL, " +
                    "tipo_amortizacao VARCHAR(10) NOT NULL, " +
                    "total_pagar DECIMAL(10,2) NOT NULL, " +
                    "data_simulacao DATE NOT NULL, " +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id), " +
                    "FOREIGN KEY (imovel_id) REFERENCES imoveis(id))";
            stmt.execute(sql);
            System.out.println("Tabela financiamentos verificada/criada");
        }
    }

    public void adicionarFinanciamento(Financiamento financiamento, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        String sql = "INSERT INTO financiamentos (cliente_id, imovel_id, taxa_juros, valor_entrada, " +
                "valor_financiado, prazo, tipo_amortizacao, total_pagar, data_simulacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, financiamento.getClienteId());
            stmt.setInt(2, financiamento.getImovelId());
            stmt.setDouble(3, financiamento.getTaxaJuros());
            stmt.setDouble(4, financiamento.getValorEntrada());
            stmt.setDouble(5, financiamento.getValorFinanciado());
            stmt.setInt(6, financiamento.getPrazo());
            stmt.setString(7, financiamento.getTipoAmortizacao().toString());
            stmt.setDouble(8, financiamento.getTotalPagar());
            stmt.setDate(9, Date.valueOf(
                    financiamento.getDataSimulacao() != null ?
                            financiamento.getDataSimulacao() :
                            LocalDate.now()
            ));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    financiamento.setId(rs.getInt(1));
                }
            }
        }
    }
}