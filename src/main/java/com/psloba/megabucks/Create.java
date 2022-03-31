package com.psloba.megabucks;

import citra.Client;
import citra.client.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
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
    public void initialize() {
        email.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) checkingEmail();});
        mobile.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) checkingMobile();});
        username.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) settingUsername();});
        password.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) settingPassword();});
        repassword.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) matchingPassword();});
        passcode.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) settingPassCode();});
        postal.focusedProperty().addListener((ov, oldV, newV) -> {if(!newV) retrievingLocation();});
    }

    @FXML
    private void checkingEmail(){
        String message = "E-mail ID Invalid!\n";
        if(!Pattern.compile("^(?=.{7,150})[a-zA-Z0-9+._-]+@[a-zA-Z0-9.]+$").matcher(email.getText()).matches()) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void checkingMobile(){
        String message = "Mobile No. Invalid!\n";
        if(mobile.getText().length() != 10) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void settingUsername() {
        String message = "Username Unavailable!\n";
        if(AppData.database.searchUser(username.getText())) messagebox.appendText(message);
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
    private void settingPassCode(){
        String message = "PassCode Invalid!\n";
        if(passcode.getText().length() != 7) messagebox.appendText(message);
        else while(messagebox.getText().contains(message)){
            int i = messagebox.getText().indexOf(message);
            int j = i + message.length();
            messagebox.deleteText(i,j);
        }
    }

    @FXML
    private void retrievingLocation(){
        String message = "Postal Code Invalid!\n";
        if(postal.getText().length() != 6) messagebox.appendText(message);
        else {
            Address address = AppData.database.getLocation(postal.getText());
            if(address != null) {
                city.setText(address.city());
                state.setText(address.state());
                country.setText(address.country());
            }
            while(messagebox.getText().contains(message)){
                int i = messagebox.getText().indexOf(message);
                int j = i + message.length();
                messagebox.deleteText(i,j);
            }
        }
    }

    @FXML
    private void onBack(){
        AppData.stage.setScene(AppData.Scenes.get("login"));
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
        passcode.setText("");
        address1.setText("");
        address2.setText("");
        postal.setText("");
        city.setText("");
        state.setText("");
        country.setText("");
    }

    @FXML
    private void onSubmit() {
        boolean check = true;
        if(name.getText().equals("")) {
            name.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(dob.getValue() == null){
            dob.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(email.getText().equals("")) {
            email.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(mobile.getText().equals("")) {
            mobile.setPromptText("FIELD EMPTY!");
            check = false;
        }
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
        if(address1.getText().equals("")) {
            address1.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(postal.getText().equals("")) {
            postal.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(city.getText().equals("")) {
            city.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(state.getText().equals("")) {
            state.setPromptText("FIELD EMPTY!");
            check = false;
        }
        if(country.getText().equals("")) {
            country.setPromptText("FIELD EMPTY!");
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
            User user = new User(username.getText(), name.getText(), dob.getValue());
            Comm comm = new Comm(email.getText(), mobile.getText());
            Security security = new Security(password.getText(), passcode.getText());
            Address address = new Address(address1.getText(), address2.getText(), postal.getText(), city.getText(), state.getText(), country.getText());
            AppData.client = new Client(user, security, comm, address);
            int id = AppData.database.addNewUser(AppData.client);
            Alert alert;
            if(id != -1) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MegaBucks");
                alert.setContentText("Account Created!");
                try {
                    AppData.database.executeUpdate(
                            "insert into player_data value(" +
                                    id + "," +
                                    "0," +
                                    "200" +
                                    ");"
                    );
                    if(AppData.database.searchTable("player_"+id)) {
                        AppData.database.executeUpdate(
                                "delete from player_" + id + ";"
                        );
                    }
                    else {
                        AppData.database.executeUpdate(
                                "create table player_" + id + "(" +
                                        "date_time datetime," +
                                        "sender int," +
                                        "receiver int," +
                                        "message varchar(250)" +
                                        ");"
                        );
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MegaBucks");
                alert.setContentText("Unable to Create Account!");
            }
            alert.show();
        }
    }
}
