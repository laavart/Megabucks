package com.psloba.megabucks;

import citra.Client;
import citra.Database;
import citra.Source;
import citra.exception.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Application extends javafx.application.Application {

    static Scene scene = null;
    static Stage stage = null;

    static Database database = null;
    static Client client = null;

    static Scene getScene(String scene){
        try {
            return new Scene(new FXMLLoader(Application.class.getResource(scene+".fxml")).load());
        }
        catch (IOException e) {
            return null;
        }
    }

    @Override
    public void start(Stage stage) {
        Application.stage = stage;
        scene = getScene("login");
        stage.setTitle("MegaBucks");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException {
        database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");
        launch();
    }
}