package com.psloba.megabucks;

import com.psloba.citra.Client;
import com.psloba.citra.Database;
import com.psloba.citra.Source;

import com.psloba.citra.client.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class LogInApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(LogInApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Database database = Database.connect(Source.MYSQL, "localhost", "megabucks", "root", "1234");

        Button login = (Button) fxmlLoader.getNamespace().get("login");
        login.isDefaultButton();

        TextField username = (TextField) fxmlLoader.getNamespace().get("username");
        PasswordField password = (PasswordField) fxmlLoader.getNamespace().get("password");

        login.setOnAction(e -> {
            Client client = database.validateUser(username.getText(), password.getText());

            if(client != null) System.out.println("Found!");
            else System.out.println("Not Found!");
        });
    }

    public static void main(String[] args) {
        launch();
    }
}