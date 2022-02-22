package com.psloba.megabucks;

import com.psloba.citra.Database;
import com.psloba.citra.Source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogInApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("LogIn-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Database db = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

    }

    public static void main(String[] args) {
        launch();
    }
}