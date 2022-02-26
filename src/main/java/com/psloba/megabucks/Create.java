package com.psloba.megabucks;

import com.psloba.citra.client.*;
import com.psloba.citra.Client;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.regex.Pattern;

public class Create {

    public TextField name;
    public DatePicker dob;
    public TextField email;
    public TextField mobile;
    public PasswordField password;
    public TextField username;
    public PasswordField repassword;
    public Label message;
    public TextField address1;
    public TextField address2;
    public TextField postal;
    public TextField city;
    public TextField state;

    @FXML
    private void checkingEmail(){
        if(!Pattern.compile("^(?=.{7,150})[a-zA-Z0-9+._-]+@[a-zA-Z0-9.]+$").matcher(email.getText()).matches())
            message.setText("E-mail ID Invalid!");
    }

    @FXML
    private void checkingMobile(){
        if(mobile.getText().length() != 10)
            message.setText("Mobile No. Invalid!");
    }

    @FXML
    private void settingUsername() {
        if(Application.database.searchUser(username.getText()))
            message.setText("Username Unavailable!");
    }

    @FXML
    private void settingPassword(){
        if(!Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(password.getText()).matches())
            System.out.println("Password Invalid!");
    }

    @FXML
    private void matchingPassword(){
        if(username.getText() != repassword.getText())
            System.out.println("Password Mismatch!");
    }

    @FXML
    private void onBack(){
        Application.stage.setScene(Application.Scenes.get("login.fxml"));
    }
}
