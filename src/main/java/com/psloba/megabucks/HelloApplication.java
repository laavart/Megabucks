package com.psloba.megabucks;

import com.psloba.citra.Database;
import com.psloba.citra.Source;
import com.psloba.citra.exception.DBInvalidException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, DBInvalidException {
        Database db = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}