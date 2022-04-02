package com.psloba.megabucks;

import java.sql.SQLException;

import citra.Database;
import citra.util.Source;
import citra.exception.*;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        AppData.stage = stage;
        AppData.currentScene = AppData.Scenes.get("login");
        stage.setTitle("MegaBucks");
        stage.setScene(AppData.currentScene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException, SQLException {
        AppData.database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

        launch();

        assert AppData.database != null : "Database Connection Error in @main";
        if(!AppData.database.searchTable("player_data")) {
            AppData.database.executeQuery(
                    "create table player_data(" +
                            "id integer primary key," +
                            "score int," +
                            "money int" +
                            ");"
            );
        }
    }
}