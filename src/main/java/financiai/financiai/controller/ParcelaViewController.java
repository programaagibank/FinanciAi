package financiai.financiai.controller;

import financiai.financiai.model.Parcela;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ParcelaViewController {
    @FXML private TableView<Parcela> tabelaParcelas;
    @FXML private TableColumn<Parcela, Integer> colunaNumero;
    @FXML private TableColumn<Parcela, Double> colunaValor;
    @FXML private TableColumn<Parcela, Double> colunaAmortizacao;
    @FXML private TableColumn<Parcela, Double> colunaJuros;
    @FXML private TableColumn<Parcela, Double> colunaSaldo;

    @FXML
    public void initialize() {
        // Configura as colunas da tabela
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numeroParcela"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valorParcela"));
        colunaAmortizacao.setCellValueFactory(new PropertyValueFactory<>("valorAmortizacao"));
        colunaJuros.setCellValueFactory(new PropertyValueFactory<>("valorJuros"));
        colunaSaldo.setCellValueFactory(new PropertyValueFactory<>("saldoDevedor"));

        colunaNumero.setPrefWidth(30);
        colunaValor.setPrefWidth(100);
        colunaAmortizacao.setPrefWidth(100);
        colunaJuros.setPrefWidth(100);
        colunaSaldo.setPrefWidth(100);
    }

    public void setParcelas(List<Parcela> parcelas) {
        tabelaParcelas.getItems().setAll(parcelas);
    }
    @FXML
    private void voltarParaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) tabelaParcelas.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

