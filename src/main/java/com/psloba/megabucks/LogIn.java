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
    private void onClickingForgot() throws IOException {
        AppData.stage.setScene(AppData.getScene("forgot"));
    }

    @FXML
    private void onClickingCreate() throws IOException {
        AppData.stage.setScene(AppData.getScene("create"));
    }

    @FXML
    private void onClickingLogIn() throws IOException {
        assert AppData.database != null;
        AppData.client = AppData.database.validateUser(username.getText(), password.getText());

        if(AppData.client.key() != -1) AppData.stage.setScene(AppData.getScene("main"));
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Details!");
            alert.setContentText("Username or Password not correct!");
            alert.show();
        }
    }
}
