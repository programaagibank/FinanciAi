package financiai.financiai.controller;

import financiai.financiai.model.*;
import financiai.financiai.util.GeradorPDF;
import financiai.financiai.util.ContratoFinanciamentoPDF;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ResultPageController {
    @FXML private Label simulacaoResultLabel;
    @FXML private TextArea tabelaParcelasArea;
    @FXML private Label resumoNomeCliente;
    @FXML private Label resumoValorFinanciado;
    @FXML private Label resumoParcelas;
    @FXML private Label resumoValorParcela;
    @FXML private Label resumoTotalPagar;

    private Financiamento financiamento;
    private Cliente cliente;
    private Imovel imovel;
    private List<Parcela> parcelas;

    public void setDadosFinanciamento(Financiamento financiamento, Cliente cliente, Imovel imovel,
                                      List<Parcela> parcelas) {
        this.financiamento = financiamento;
        this.cliente = cliente;
        this.imovel = imovel;
        this.parcelas = parcelas;
        exibirResultados();
    }

    private void exibirResultados() {
        try {
            // Atualiza os labels do resumo
            resumoNomeCliente.setText(cliente.getNome());
            resumoValorFinanciado.setText(String.format("R$ %.2f", financiamento.getValorFinanciado()));
            resumoParcelas.setText(String.valueOf(financiamento.getPrazo()));
            resumoValorParcela.setText(String.format("R$ %.2f", parcelas.get(0).getValorParcela()));
            resumoTotalPagar.setText(String.format("R$ %.2f", financiamento.getTotalPagar()));

            // Exibe status da simulação
            simulacaoResultLabel.setText("Simulação concluída com sucesso!");

            // Preenche a tabela de parcelas no TextArea com colunas alinhadas
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s %-18s %-20s %-20s %-20s%n",
                    "Parcela", "Valor (R$)", "Amortização (R$)", "Juros (R$)", "Déficit (R$)"));

            for (Parcela parcela : parcelas) {
                sb.append(String.format("%-10d %18.2f %20.2f %20.2f %20.2f%n",
                        parcela.getNumeroParcela(),
                        parcela.getValorParcela(),
                        parcela.getValorAmortizacao(),
                        parcela.getValorJuros(),
                        parcela.getSaldoDevedor()));
            }

            System.out.println(sb.toString());
            
            tabelaParcelasArea.setText(sb.toString());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao exibir resultados: " + e.getMessage());
        }
    }


    @FXML
    private void gerarPDF() {
        try {
            if (financiamento == null || cliente == null || imovel == null || parcelas == null) {
                throw new IllegalStateException("Dados de financiamento não foram carregados corretamente");
            }

            GeradorPDF.gerarPDF(financiamento, cliente, imovel, parcelas);
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Relatório PDF gerado com sucesso!\nO arquivo foi salvo na pasta de documentos.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro",
                    "Falha ao gerar PDF:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void gerarContrato() {
        try {

            ContratoFinanciamentoPDF.gerarContratoPDF(financiamento, cliente, imovel);
            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Contrato gerado com sucesso!\nO arquivo foi salvo na pasta de documentos.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro",
                    "Falha ao gerar contrato:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void voltar() {
        try {
            URL fxmlUrl = getClass().getResource("/view.fxml");
            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado. Verifique o caminho: /financiai/financiai/view/view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = (Stage) simulacaoResultLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Financiai - Simulação de Financiamento");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Erro",
                    "Não foi possível voltar à tela inicial:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}