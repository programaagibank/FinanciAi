package financiai.financiai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import financiai.financiai.controller.FinanciamentoController;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carrega o FXML
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view.fxml"));
        Parent root = fxmlLoader.load();

        // Cria a cena antes de mostrar
        Scene scene = new Scene(root);

        // Configura a janela
        primaryStage.setScene(scene);
        primaryStage.setWidth(575);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Simulador de Financiamento");

        // Passa o Stage pro controller
        FinanciamentoController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);

        // Exibe a janela
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}