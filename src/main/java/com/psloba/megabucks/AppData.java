package com.psloba.megabucks;

import citra.Client;
import citra.Database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppData {

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
}
