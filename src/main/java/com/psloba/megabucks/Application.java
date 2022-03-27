package com.psloba.megabucks;

import citra.Database;
import citra.Source;
import citra.exception.*;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        AppData.stage = stage;
        AppData.scene = AppData.Scenes.get("login");
        stage.setTitle("MegaBucks");
        stage.setScene(AppData.scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException {
        AppData.database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");
        launch();
    }
}