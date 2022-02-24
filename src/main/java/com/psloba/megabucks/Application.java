package com.psloba.megabucks;

import com.psloba.citra.Client;
import com.psloba.citra.Database;
import com.psloba.citra.Source;

import com.psloba.citra.exception.DBInvalidException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    static FXMLLoader fxmlLoader = null;
    static Scene scene = null;
    static Stage stage = null;

    static Database database = null;
    static Client client = null;

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(Application.class.getResource("login-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("MegaBucks");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void showMain(Stage stage) throws IOException{
        fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws DBInvalidException {
        database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");
        launch();
    }
}