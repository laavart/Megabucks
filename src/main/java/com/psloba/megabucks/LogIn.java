package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LogIn {

    public TextField username;
    public PasswordField password;
    public TextField showpassword;
    public ToggleButton show;

    @FXML
    private void onShow(){
        showpassword.setText( show.isSelected() ? password.getText() : "" );
    }

    @FXML
    private void onClickingForgot() {
        Application.showForgot(Application.stage);
    }

    @FXML
    private void onClickingCreate() {
        Application.showCreate(Application.stage);
    }

    @FXML
    private void onClickingLogIn() {
        assert Application.database != null;
        Application.client = Application.database.validateUser(username.getText(), password.getText());

        if(Application.client != null) Application.showMain(Application.stage);
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Details!");
            alert.setContentText("Username or Password not correct!");
            alert.show();
        }
    }
}
