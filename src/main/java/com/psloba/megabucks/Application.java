package com.psloba.megabucks;

import com.psloba.citra.Database;
import com.psloba.citra.Source;

import com.psloba.citra.exception.DBInvalidException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("MegaBucks");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException {
        AppData.database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");
        launch();
    }
}