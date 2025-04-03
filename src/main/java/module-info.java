module financiai.financiai {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.sql;
    requires java.desktop;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.testfx.junit5;

    opens financiai.financiai to javafx.fxml;
    exports financiai.financiai;
}