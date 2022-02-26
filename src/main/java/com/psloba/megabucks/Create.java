package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class Create {

    public TextField name;
    public DatePicker dob;
    public TextField email;
    public TextField mobile;
    public PasswordField password;
    public TextField username;
    public PasswordField repassword;
    public HBox message;
    public TextField address1;
    public TextField address2;
    public TextField postal;
    public TextField city;
    public TextField state;

    @FXML
    private void onBack(){
        Application.stage.setScene(Application.Scenes.get("login.fxml"));
    }

}
