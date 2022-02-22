package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class LogInController {

    public TextField username;
    public PasswordField password;
    public Label forgot;
    public Label create;
    public BorderPane window;

    @FXML
    private void onClickingForgot(){
        System.out.println("Forgot Clicked");
    }

    @FXML
    private void onClickingCreate(){
        System.out.println("Create Clicked");
    }

}
