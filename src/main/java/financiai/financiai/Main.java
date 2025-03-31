package financiai.financiai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import financiai.financiai.controller.FinanciamentoController;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carrega a tela principal
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("/view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Simulador de Financiamento");
        primaryStage.setScene(scene);

        // Configura o stage no controller
        FinanciamentoController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}