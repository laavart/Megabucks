module com.psloba.megabucks {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.psloba.megabucks to javafx.fxml;
    exports com.psloba.megabucks;
}