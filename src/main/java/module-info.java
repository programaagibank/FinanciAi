module financiai.financiai {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.sql;


    opens financiai.financiai to javafx.fxml;
    exports financiai.financiai;
}