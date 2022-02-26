package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        Application.stage.setScene(Application.Scenes.get("forgot.fxml"));
    }

    @FXML
    private void onClickingCreate() {
        Application.stage.setScene(Application.Scenes.get("create.fxml"));
    }

    @FXML
    private void onClickingLogIn() {
        assert Application.database != null;
        Application.client = Application.database.validateUser(username.getText(), password.getText());

        if(Application.client != null) Application.stage.setScene(Application.Scenes.get("main.fxml"));
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Details!");
            alert.setContentText("Username or Password not correct!");
            alert.show();
        }
    }
}
