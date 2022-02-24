package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class LogInController {

    public TextField username;
    public PasswordField password;
    public TextField showpassword;
    public ToggleButton show;
    public Label forgot;
    public Label create;
    public BorderPane window;

    @FXML
    private void onShow(){
        showpassword.setText( show.isSelected() ? password.getText() : "" );
    }

    @FXML
    private void onClickingForgot(){
        System.out.println("Forgot Clicked");
    }

    @FXML
    private void onClickingCreate(){
        System.out.println("Create Clicked");
    }

    @FXML
    private void onClickingLogIn(){
        assert AppData.database != null;
        AppData.client = AppData.database.validateUser(username.getText(), password.getText());

        if(AppData.client != null) {
            System.out.println("Good!");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Details!");
            alert.setContentText("Username or Password not correct!");
            alert.show();
        }
    }
}
