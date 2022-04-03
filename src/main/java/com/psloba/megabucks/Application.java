package com.psloba.megabucks;

import java.io.IOException;
import java.sql.SQLException;

import citra.Database;
import citra.util.Source;
import citra.exception.*;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        AppData.stage = stage;
        AppData.currentScene = AppData.getScene("login");
        stage.setTitle("MegaBucks");
        stage.setScene(AppData.currentScene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException, SQLException {
        AppData.database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

        if(!AppData.database.searchTable("player_data")) {
            AppData.database.executeUpdate(
                    "create table player_data(" +
                            "id int primary key," +
                            "score int," +
                            "money int" +
                            ");"
            );
        }

        launch();
    }
}