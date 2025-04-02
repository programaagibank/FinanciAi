package financiai.financiai.controller;

import financiai.financiai.model.*;
import financiai.financiai.services.*;
import financiai.financiai.dao.*;
import financiai.financiai.util.Conexao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanciamentoController {
    @FXML private TextField nomeClienteField;
    @FXML private TextField cpfClienteField;
    @FXML private TextField rendaClienteField;
    @FXML private TextField valorImovelField;
    @FXML private TextField valorEntradaField;
    @FXML private TextField prazoField;
    @FXML private ChoiceBox<String> tipoImovelBox;
    @FXML private ChoiceBox<String> tipoFinanciamentoBox;
    @FXML private Label resultadoLabel;
    @FXML private Label statusBancoLabel;
    @FXML private ChoiceBox<String> bancoBox;
    @FXML private Label taxaJurosLabel;

    private Stage primaryStage;
    private boolean bancoPronto = false;
    private Cliente cliente;
    private Imovel imovel;
    private Financiamento financiamento;
    private List<Parcela> parcelas;
    private final Map<String, Double> taxasBancos = new HashMap<>();

    @FXML
    public void initialize() {
        tipoImovelBox.getItems().addAll("Casa", "Apartamento");
        tipoFinanciamentoBox.getItems().addAll("SAC", "Price");
        bancoBox.getItems().addAll("ITAU", "AGIBANK");
        taxasBancos.put("ITAU", 0.08);  // Taxa fixa ajustada para 8%
        taxasBancos.put("AGIBANK", 0.09); // Taxa fixa ajustada para 9%

        bancoBox.setOnAction(event -> atualizarTaxaJuros());
        verificarEstruturaBanco();
    }

    private void atualizarTaxaJuros() {
        String bancoSelecionado = bancoBox.getValue();
        if (bancoSelecionado != null && taxasBancos.containsKey(bancoSelecionado)) {
            taxaJurosLabel.setText(String.format("Taxa de Juros: %.2f%%", taxasBancos.get(bancoSelecionado) * 100));
        } else {
            taxaJurosLabel.setText("Taxa de Juros: -");
        }
    }


    private void verificarEstruturaBanco() {
        statusBancoLabel.setText("Verificando banco de dados...");
        try (Connection conexao = Conexao.conectar()) {
            // Verificar e criar tabelas se necessário
            ClienteDAO clienteDAO = new ClienteDAO();
            clienteDAO.verificarTabela(conexao);

            ImovelDAO imovelDAO = new ImovelDAO();
            imovelDAO.verificarTabela(conexao);

            FinanciamentoDAO financiamentoDAO = new FinanciamentoDAO();
            financiamentoDAO.verificarTabela(conexao);

            ParcelasDAO parcelasDAO = new ParcelasDAO();
            parcelasDAO.verificarTabela(conexao);

            bancoPronto = true;
            statusBancoLabel.setText("Banco de dados pronto!");
        } catch (SQLException e) {
            bancoPronto = false;
            statusBancoLabel.setText("Erro no banco de dados!");
            mostrarAlerta("Erro no Banco", "Não foi possível preparar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void calcularFinanciamento() {
        if (!bancoPronto) {
            mostrarAlerta("Banco não pronto", "Aguarde a inicialização do banco de dados");
            return;
        }

        try {
            // Validação dos campos
            if (!validarCampos()) return;

            // Coletar dados
            DadosFinanciamento dados = coletarDadosFormulario();

            // Validação de negócio
            if (dados.valorEntrada >= dados.valorImovel) {
                mostrarAlerta("Erro", "Valor de entrada não pode ser maior ou igual ao valor do imóvel");
                return;
            }

            // Criar entidades
            cliente = new Cliente(dados.nome, dados.cpf, dados.renda);
            imovel = new Imovel(dados.tipoImovel, dados.valorImovel);

            // Calcular financiamento
            financiamento = calcularFinanciamento(dados, cliente, imovel);

            // Salvar tudo
            salvarFinanciamentoCompleto(cliente, imovel, financiamento);

            // Mostrar resultado
            resultadoLabel.setText(String.format("Financiamento calculado! Total: R$ %.2f",
                    financiamento.getTotalPagar()));

            mostrarTelaResultados();

        } catch (Exception e) {
            mostrarAlerta("Erro", "Falha no cálculo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private DadosFinanciamento coletarDadosFormulario() throws IllegalArgumentException {
        DadosFinanciamento dados = new DadosFinanciamento();
        try {
            dados.nome = nomeClienteField.getText();
            dados.cpf = cpfClienteField.getText();
            dados.renda = Double.parseDouble(rendaClienteField.getText().replace(",", "."));
            dados.valorImovel = Double.parseDouble(valorImovelField.getText().replace(",", "."));
            dados.valorEntrada = Double.parseDouble(valorEntradaField.getText().replace(",", "."));
            dados.prazo = Integer.parseInt(prazoField.getText());

            // Obter o banco selecionado e definir a taxa fixa
            String bancoSelecionado = bancoBox.getValue();
            if (bancoSelecionado == null || !taxasBancos.containsKey(bancoSelecionado)) {
                throw new IllegalArgumentException("Banco não selecionado ou inválido");
            }
            dados.taxaJuros = taxasBancos.get(bancoSelecionado); // Agora pega direto do mapa

            dados.tipoImovel = TipoImovel.valueOf(tipoImovelBox.getValue().toUpperCase());
            dados.tipoAmortizacao = TipoAmortizacao.valueOf(tipoFinanciamentoBox.getValue().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Dados inválidos no formulário");
        }
        return dados;
    }


    private boolean validarCampos() {
        // Validação de campos vazios
        if (nomeClienteField.getText().isEmpty() ||
                cpfClienteField.getText().isEmpty() ||
                rendaClienteField.getText().isEmpty() ||
                valorImovelField.getText().isEmpty() ||
                valorEntradaField.getText().isEmpty() ||
                bancoBox.getValue() == null ||  // Corrigido: Verifica se um banco foi selecionado
                prazoField.getText().isEmpty() ||
                tipoImovelBox.getValue() == null ||
                tipoFinanciamentoBox.getValue() == null) {

            mostrarAlerta("Erro", "Preencha todos os campos obrigatórios");
            return false;
        }

        // Validação de CPF
        try {
            new Cliente("Teste", cpfClienteField.getText(), 1000); // Usa a validação da classe Cliente
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro", "CPF inválido: " + e.getMessage());
            return false;
        }

        // Validação de valores numéricos
        try {
            double renda = Double.parseDouble(rendaClienteField.getText().replace(",", "."));
            double valorImovel = Double.parseDouble(valorImovelField.getText().replace(",", "."));
            double valorEntrada = Double.parseDouble(valorEntradaField.getText().replace(",", "."));
            int prazo = Integer.parseInt(prazoField.getText());

            Double taxaJuros = taxasBancos.get(bancoBox.getValue());
            if (taxaJuros == null) {
                mostrarAlerta("Erro", "Selecione um banco válido");
                return false;
            }

            if (renda <= 0 || valorImovel <= 0 || valorEntrada < 0 || taxaJuros <= 0 || prazo <= 0) {
                mostrarAlerta("Erro", "Valores devem ser maiores que zero");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Valores numéricos inválidos");
            return false;
        }

        return true;
    }


    private Financiamento calcularFinanciamento(DadosFinanciamento dados, Cliente cliente, Imovel imovel) {
        double valorFinanciado = dados.valorImovel - dados.valorEntrada;

        double taxa_juros = dados.taxaJuros / 12;
        parcelas = dados.tipoAmortizacao.calcularParcela(valorFinanciado, taxa_juros, dados.prazo, dados.cpf);
        double totalPagar = parcelas.stream().mapToDouble(Parcela::getValorParcela).sum();

        Financiamento financiamento = new Financiamento(
                dados.prazo, taxa_juros, dados.tipoAmortizacao,
                dados.valorEntrada, valorFinanciado, totalPagar
        );

        financiamento.setParcelas(parcelas);
        return financiamento;
    }

    private void salvarFinanciamentoCompleto(Cliente cliente, Imovel imovel, Financiamento financiamento) {
        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            // 1. Cliente
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente clienteExistente = clienteDAO.buscarClientePorCpf(cliente.getCpf(), conexao);
            if (clienteExistente != null) {
                cliente = clienteExistente;
            } else {
                clienteDAO.adicionarCliente(cliente, conexao);
            }

            // 2. Imóvel
            ImovelDAO imovelDAO = new ImovelDAO();
            imovelDAO.adicionarImovel(imovel, conexao);

            // 3. Financiamento
            financiamento.setClienteId(cliente.getId());
            financiamento.setImovelId(imovel.getId());
            financiamento.setDataSimulacao(LocalDate.now());

            FinanciamentoDAO financiamentoDAO = new FinanciamentoDAO();
            financiamentoDAO.adicionarFinanciamento(financiamento, conexao);

            // 4. Parcelas
            ParcelasDAO parcelasDAO = new ParcelasDAO();
            for (Parcela parcela : financiamento.getParcelas()) {
                parcela.setFinanciamentoId(financiamento.getId());
                parcelasDAO.adicionarParcela(parcela, conexao);
            }

            conexao.commit();
            System.out.println("Dados salvos com sucesso!");

        } catch (SQLException e) {
            mostrarAlerta("Erro no Banco", "Falha ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class DadosFinanciamento {
        String nome;
        String cpf;
        double renda;
        double valorImovel;
        double valorEntrada;
        double taxaJuros;
        int prazo;
        TipoImovel tipoImovel;
        TipoAmortizacao tipoAmortizacao;
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void mostrarTelaResultados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/simulacaoView.fxml"));
            Parent root = loader.load();
            // Para carregar a view de parcelas
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/parcelaView.fxml"));

            ResultPageController controller = loader.getController();
            controller.setDadosFinanciamento(financiamento, cliente, imovel, parcelas);

            Stage stage = (Stage) nomeClienteField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Erro", "Não foi possível carregar a tela de resultados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void buscarParcelasPorCPF() {
        try {
            String cpf = cpfClienteField.getText().replaceAll("[^0-9]", "");

            if (cpf.isEmpty()) {
                mostrarAlerta("Erro", "Informe um CPF para buscar");
                return;
            }

            try (Connection conexao = Conexao.conectar()) {
                ParcelasDAO parcelasDAO = new ParcelasDAO();
                List<Parcela> parcelas = parcelasDAO.buscarParcelasPorCpfCliente(cpf, conexao);

                if (parcelas.isEmpty()) {
                    mostrarAlerta("Informação", "Nenhuma parcela encontrada para este CPF");
                    return;
                }

                // Carrega a tela de visualização de parcelas
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/parcelaView.fxml"));
                URL url = getClass().getResource("/view/parcelaView.fxml");

                if (url == null) {
                    throw new RuntimeException("Arquivo FXML não encontrado. Verifique se o arquivo parcelaView.fxml está em: " +
                            "src/main/resources/view/parcelaView.fxml");

                }

                Parent root = loader.load();

                ParcelaViewController controller = loader.getController();
                controller.setParcelas(parcelas);

                Stage stage = (Stage) cpfClienteField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (SQLException | IOException e) {
            mostrarAlerta("Erro", "Falha ao buscar parcelas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {
        nomeClienteField.clear();
        cpfClienteField.clear();
        rendaClienteField.clear();
        valorImovelField.clear();
        valorEntradaField.clear();
        prazoField.clear();
        tipoImovelBox.setValue(null);
        tipoFinanciamentoBox.setValue(null);
        bancoBox.setValue(null);
        taxaJurosLabel.setText("Taxa de Juros: -");
        resultadoLabel.setText("");
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}







































































