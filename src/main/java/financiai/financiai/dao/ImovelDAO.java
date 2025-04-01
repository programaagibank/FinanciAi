package financiai.financiai.dao;

import financiai.financiai.model.Imovel;
import java.sql.*;

public class ImovelDAO {

    public void verificarTabela(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS imoveis (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "tipo_imovel VARCHAR(20) NOT NULL, " +
                    "valor_imovel DECIMAL(10,2) NOT NULL)";
            stmt.execute(sql);
            System.out.println("Tabela imoveis verificada/criada");
        }
    }

    public void adicionarImovel(Imovel imovel, Connection conexao) throws SQLException {
        verificarTabela(conexao);

        String sql = "INSERT INTO imoveis (tipo_imovel, valor_imovel) VALUES (?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, imovel.getTipoImovel().toString());
            stmt.setDouble(2, imovel.getValorImovel());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    imovel.setId(rs.getInt(1));
                }
            }
        }
    }
}