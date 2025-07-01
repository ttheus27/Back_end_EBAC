module com.pucpr.sistemacarro {
    requires javafx.controls;
    requires javafx.fxml;    // Mantenha se planeja usar FXML para outras partes
    requires javafx.graphics;


    opens com.pucpr.sistemacarro to javafx.fxml, javafx.graphics;
    exports com.pucpr.sistemacarro;
}