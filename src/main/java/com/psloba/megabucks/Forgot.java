package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Objects;
import java.util.regex.Pattern;

public class Forgot {

    public TextArea messagebox;
    public TextField username;
    public TextField passcode;
    public PasswordField password;
    public PasswordField repassword;

    @FXML
    private void checkingUsername(){
        String message = "User Not Found!\n";
        if(!Application.database.searchUser(username.getText())) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void checkingPassCode(){
        String message = "PassCode Invalid!\n";
        if(passcode.getText().length() != 7) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void settingPassword(){
        String message = "Password Invalid!\n";
        if(!Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(password.getText()).matches()) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void matchingPassword(){
        String message = "Password Mismatch!\n";
        if(!Objects.equals(password.getText(), repassword.getText())) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void onBack(){
        Application.stage.setScene(Application.getScene("login"));
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
        if(!messagebox.getText().equals("Messages:\n")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MegaBucks");
            alert.setContentText("Resolve all the Problems First!");
            alert.show();
            check = false;
        }
        if(check) {
            Alert alert;
            if(Application.database.changePassword(username.getText(),passcode.getText(),password.getText())) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MegaBucks");
                alert.setContentText("Password Changed!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MegaBucks");
                alert.setContentText("Unable to Change Password!");
            }
            alert.show();
        }
    }
}
