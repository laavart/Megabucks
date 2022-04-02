package com.psloba.megabucks;

import java.io.IOException;
import java.util.HashMap;


import citra.Client;
import citra.Database;
import citra.util.Pair;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppData {

    static Stage stage = null;
    static Scene currentScene = null;

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

    static Database database = null;
    static Pair<Integer, Client> client = null;

    static HashMap<String, Integer> users = new HashMap<>();
}
