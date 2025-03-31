package financiai.financiai.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetupDAO {

    public void criarTabelas(Connection conexao) throws SQLException {
        // Verificar se tabelas já existem
        if (tabelasExistem(conexao)) {
            System.out.println("Tabelas já existem, pulando criação");
            return;
        }

        try (Statement stmt = conexao.createStatement()) {
            // Desativar verificação de chaves estrangeiras temporariamente
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");

            // Tabela clientes
            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +

                    "nome VARCHAR(100) NOT NULL, " +
                    "cpf VARCHAR(11) NOT NULL UNIQUE, " +
                    "renda_mensal DECIMAL(10,2) NOT NULL)");

            // Tabela imoveis
            stmt.execute("CREATE TABLE IF NOT EXISTS imoveis (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "tipo_imovel VARCHAR(20) NOT NULL, " +
                    "valor_imovel DECIMAL(10,2) NOT NULL)");

            // Tabela financiamentos
            stmt.execute("CREATE TABLE IF NOT EXISTS financiamentos (" +
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
                    "FOREIGN KEY (imovel_id) REFERENCES imoveis(id))");

            // Tabela parcelas
            stmt.execute("CREATE TABLE IF NOT EXISTS parcelas (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "financiamento_id INT NOT NULL, " +
                    "numero_parcela INT NOT NULL, " +
                    "valor_parcela DECIMAL(10,2) NOT NULL, " +
                    "valor_amortizacao DECIMAL(10,2) NOT NULL, " +
                    "valor_juros DECIMAL(10,2) NOT NULL, " +
                    "saldo_devedor DECIMAL(10,2) NOT NULL, " +
                    "FOREIGN KEY (financiamento_id) REFERENCES financiamentos(id))");

            // Reativar verificação de chaves estrangeiras
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");

            System.out.println("Tabelas criadas com sucesso!");
        }
    }

    public boolean tabelasExistem(Connection conexao) throws SQLException {
        String[] tabelas = {"clientes", "imoveis", "financiamentos", "parcelas"};

        for (String tabela : tabelas) {
            try (ResultSet rs = conexao.getMetaData().getTables(null, null, tabela, null)) {
                if (!rs.next()) {
                    System.out.println("Tabela não encontrada: " + tabela);
                    return false;
                }
            }
        }
        System.out.println("Todas as tabelas existem");
        return true;
    }
}