package financiai.financiai.dao;

import financiai.financiai.model.Imovel;

import java.sql.*;

public class ImovelDAO {

    public void adicionarImovel(Imovel imovel, Connection conexao) throws SQLException {
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