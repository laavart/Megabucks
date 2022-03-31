package com.psloba.megabucks;

import citra.Database;
import citra.Source;
import citra.exception.*;

import javafx.stage.Stage;

import java.sql.SQLException;

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

    public static void main(String[] args) throws DBInvalidException, SQLException {
        AppData.database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

        if(!AppData.database.searchTable("player_data")) {
            AppData.database.executeQuery(
                    "create table player_data(" +
                            "id integer primary key," +
                            "score integer," +
                            "money integer" +
                            ");"
            );
        }

        launch();
    }
}