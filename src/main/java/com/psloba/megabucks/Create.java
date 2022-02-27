package com.psloba.megabucks;

import com.psloba.citra.Client;
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
        else while(messagebox.getText().contains(message))
            messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
    }

    @FXML
    private void checkingMobile(){
        String message = "Mobile No. Invalid!\n";
        if(mobile.getText().length() != 10)
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message))
            messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
    }

    @FXML
    private void settingUsername() {
        String message = "Username Unavailable!\n";
        if(Application.database.searchUser(username.getText()))
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message))
            messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
    }

    @FXML
    private void settingPassword(){
        String message = "Password Invalid!\n";
        if(!Pattern.compile("^.*(?=.{8,128})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$").matcher(password.getText()).matches())
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message))
            messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
    }

    @FXML
    private void matchingPassword(){
        String message = "Password Mismatch!\n";
        if(!Objects.equals(password.getText(), repassword.getText()))
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message))
            messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
    }

    @FXML
    private void settingPassCode(){
        String message = "PassCode Invalid!\n";
        if(passcode.getText().length() != 7)
            messagebox.appendText(message);
        else while(messagebox.getText().contains(message))
            messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
    }

    @FXML
    private void retrievingLocation(){
        String message = "Postal Code Invalid!\n";
        if(postal.getText().length() != 6)
            messagebox.appendText(message);
        else {
            Address address = Application.database.getLocation(postal.getText());
            if(address != null) {
                city.setText(address.city());
                state.setText(address.state());
                country.setText(address.country());
            }
            while(messagebox.getText().contains(message))
                messagebox.deleteText(messagebox.getText().indexOf(message), messagebox.getText().length());
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
        if(username.getText()==null) username.setText("FIELD EMPTY!");
        else if(email.getText()==null) email.setText("FIELD EMPTY!");
        else if(mobile.getText()==null) mobile.setText("FIELD EMPTY!");
        else if(password.getText()==null) password.setText("FIELD EMPTY!");
        else if(repassword.getText()==null) repassword.setText("FIELD EMPTY!");
        else if(passcode.getText()==null) passcode.setText("FIELD EMPTY!");
        else if(address1.getText()==null) address1.setText("FIELD EMPTY!");
        else if(postal.getText()==null) postal.setText("FIELD EMPTY!");
        else if(city.getText()==null) city.setText("FIELD EMPTY!");
        else if(state.getText()==null) state.setText("FIELD EMPTY!");
        else if(country.getText()==null) country.setText("FIELD EMPTY!");
        else if(!messagebox.getText().equals("Messages:")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form!");
            alert.setContentText("Resolve all the Problems First!");
            alert.show();
        }
        else {
            User user = new User(username.getText(), name.getText(), dob.getValue());
            Comm comm = new Comm(email.getText(), mobile.getText());
            Security security = new Security(password.getText(), passcode.getText());
            Address address = new Address(address1.getText(), address2.getText(), postal.getText(), city.getText(), state.getText(), country.getText());
            Application.client = new Client(user, security, comm, address);
            int id = Application.database.addNewUser(Application.client);
            Alert alert;
            if(id != -1) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Accounts!");
                alert.setContentText("Account Created!");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Accounts!");
                alert.setContentText("Unable to Create Account!");
            }
            alert.show();
        }
    }
}
