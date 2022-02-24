package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LogInController {

    public TextField username;
    public PasswordField password;
    public TextField showpassword;
    public ToggleButton show;

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
    private void onClickingLogIn() throws IOException{
        assert Application.database != null;
        Application.client = Application.database.validateUser(username.getText(), password.getText());

        if(Application.client != null) {
            Application.stage.close();
            Application.showMain(Application.stage);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Details!");
            alert.setContentText("Username or Password not correct!");
            alert.show();
        }
    }
}
