package com.psloba.megabucks;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Create {

    @FXML
    private void onBack(){
        Application.stage.setScene(Application.Scenes.get("login.fxml"));
    }

}
