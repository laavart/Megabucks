module com.psloba.megabucks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.psloba.megabucks to javafx.fxml;
    exports com.psloba.megabucks;
}