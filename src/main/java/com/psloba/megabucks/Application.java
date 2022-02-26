package com.psloba.megabucks;

import com.psloba.citra.Client;
import com.psloba.citra.Database;
import com.psloba.citra.Source;
import com.psloba.citra.exception.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Application extends javafx.application.Application {

    static Scene scene = null;
    static Stage stage = null;

    static HashMap<String, Scene> Scenes = new HashMap<>(4);

    static Database database = null;
    static Client client = null;

    @Override
    public void start(Stage stage) {
        Application.stage = stage;
        scene = Scenes.get("login.fxml");
        stage.setTitle("MegaBucks");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException, IOException {
        database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

        Scenes.put("login.fxml", new Scene(new FXMLLoader(Application.class.getResource("login.fxml")).load()));
        Scenes.put("main.fxml", new Scene(new FXMLLoader(Application.class.getResource("main.fxml")).load()));
        Scenes.put("forgot.fxml", new Scene(new FXMLLoader(Application.class.getResource("forgot.fxml")).load()));
        Scenes.put("create.fxml", new Scene(new FXMLLoader(Application.class.getResource("create.fxml")).load()));

        launch();
    }
}