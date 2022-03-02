package com.psloba.megabucks;

import citra.Client;
import citra.client.Address;
import citra.client.Comm;
import citra.client.Security;
import citra.client.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;

import java.util.Objects;
import java.util.regex.Pattern;

public class Forgot {

    public TextField username;
    public TextField passcode;
    public PasswordField password;
    public PasswordField repassword;

    @FXML
    private void settingUsername(){
        if(!Application.database.searchUser(username.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Forgot Password!");
            alert.setContentText("User not Found!");
            alert.show();
        }
    }

    @FXML
    private void checkingPassCode(){
        if(passcode.getText().length() != 7) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Forgot Password!");
            alert.setContentText("Incorrect Passcode!");
            alert.show();
        }
    }

    @FXML
    private void settingPassword(){
        if(!Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(password.getText()).matches()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Forgot Password!");
            alert.setContentText("Invalid Password!");
            alert.show();
        }
    }

    @FXML
    private void matchingPassword(){
        if(!Objects.equals(password.getText(), repassword.getText())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Forgot Password!");
            alert.setContentText("Mismatching Password!");
            alert.show();
        }
    }

    @FXML
    private void onBack(){
        Application.stage.setScene(Application.Scenes.get("login.fxml"));
    }

    @FXML
    private void onClear(){
        username.setText("");
        password.setText("");
        repassword.setText("");
        passcode.setText("");
    }

    @FXML
    private void onSubmit() {
        boolean check = true;
        if(username.getText().equals("")) {
            username.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(password.getText().equals("")) {
            password.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(repassword.getText().equals("")) {
            repassword.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(passcode.getText().equals("")) {
            passcode.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(check) {

        }
    }
}
