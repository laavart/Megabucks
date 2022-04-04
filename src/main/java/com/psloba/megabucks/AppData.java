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

    static Scene getScene(String scene) throws IOException {
        return new Scene(new FXMLLoader(Application.class.getResource(scene+".fxml")).load());
    }

    static Database database = null;
    static Pair<Integer, Client> client = null;
    static HashMap<Integer, String> users = new HashMap<>();
}
