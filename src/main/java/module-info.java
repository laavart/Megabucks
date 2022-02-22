module com.psloba.megabucks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.psloba.megabucks to javafx.fxml;
    exports com.psloba.megabucks;
    exports com.psloba.megabucks.Application;
    opens com.psloba.megabucks.Application to javafx.fxml;
    exports com.psloba.megabucks.controller;
    opens com.psloba.megabucks.controller to javafx.fxml;
}