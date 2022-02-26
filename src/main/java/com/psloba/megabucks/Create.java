package com.psloba.megabucks;

import com.psloba.citra.client.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Objects;
import java.util.regex.Pattern;

public class Create {

    public TextArea messagebox;
    public TextField name;
    public DatePicker dob;
    public TextField email;
    public TextField mobile;
    public PasswordField password;
    public TextField username;
    public PasswordField repassword;
    public TextField passcode;
    public TextField address1;
    public TextField address2;
    public TextField postal;
    public TextField city;
    public TextField state;
    public TextField country;

    @FXML
    private void checkingEmail(){
        String message = "E-mail ID Invalid!\n";
        if(!Pattern.compile("^(?=.{7,150})[a-zA-Z0-9+._-]+@[a-zA-Z0-9.]+$").matcher(email.getText()).matches())
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message)) {
            int i = messagebox.getText().indexOf(message);
            int l = messagebox.getText().length();
            messagebox.deleteText(i,l);
        }
    }

    @FXML
    private void checkingMobile(){
        String message = "Mobile No. Invalid!\n";
        if(mobile.getText().length() != 10)
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message)) {
            int i = messagebox.getText().indexOf(message);
            int l = messagebox.getText().length();
            messagebox.deleteText(i,l);
        }
    }

    @FXML
    private void settingUsername() {
        String message = "Username Unavailable!\n";
        if(Application.database.searchUser(username.getText()))
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message)) {
            int i = messagebox.getText().indexOf(message);
            int l = messagebox.getText().length();
            messagebox.deleteText(i,l);
        }
    }

    @FXML
    private void settingPassword(){
        String message = "Password Invalid!\n";
        if(!Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(password.getText()).matches())
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message)) {
            int i = messagebox.getText().indexOf(message);
            int l = messagebox.getText().length();
            messagebox.deleteText(i,l);
        }
    }

    @FXML
    private void matchingPassword(){
        String message = "Password Mismatch!\n";
        if(!Objects.equals(username.getText(), repassword.getText()))
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message)) {
            int i = messagebox.getText().indexOf(message);
            int l = messagebox.getText().length();
            messagebox.deleteText(i,l);
        }
    }

    @FXML
    private void settingPassCode(){
        String message = "PassCode Invalid!\n";
        if(passcode.getText().length() != 7)
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message)) {
            int i = messagebox.getText().indexOf(message);
            int l = messagebox.getText().length();
            messagebox.deleteText(i,l);
        }
    }

    @FXML
    private void onBack(){
        Application.stage.setScene(Application.Scenes.get("login.fxml"));
    }

    @FXML
    private void onClear(){
        name.setText("");
        dob.setValue(null);
        email.setText("");
        mobile.setText("");
        username.setText("");
        password.setText("");
        repassword.setText("");
        address1.setText("");
        address2.setText("");
        postal.setText("");
        city.setText("");
        state.setText("");
        country.setText("");
    }

    @FXML
    private void onSubmit() {
        User user = new User(username.getText(), name.getText(), dob.getValue());
        Comm comm = new Comm(email.getText(), mobile.getText());
        Security security = new Security(password.getText(), passcode.getText());
        Address address = new Address(address1.getText(), address2.getText(), postal.getText(), city.getText(), state.getText(), country.getText());
    }
}
