package com.psloba.megabucks;

import citra.Database;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class AppData {

    static Scene scene = null;
    static Stage stage = null;

    static Database database = null;
    static int client = -1;

    static HashMap<String, Scene> Scenes = new HashMap<>();
    static {
        try {
            Scenes.put("create", new Scene(new FXMLLoader(Application.class.getResource("create.fxml")).load()));
            Scenes.put("forgot", new Scene(new FXMLLoader(Application.class.getResource("forgot.fxml")).load()));
            Scenes.put("login", new Scene(new FXMLLoader(Application.class.getResource("login.fxml")).load()));
            Scenes.put("main", new Scene(new FXMLLoader(Application.class.getResource("main.fxml")).load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static HashMap<String, Integer> users = new HashMap<>();
}
