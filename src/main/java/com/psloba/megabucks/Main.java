package com.psloba.megabucks;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public TextArea messagebox;
    public ComboBox<String> recipients;
    public TextArea message;
    public Button send;

    @FXML
    private void initialize() throws SQLException {

        ResultSet resultSet = AppData.database.executeQuery("Select uID from user_master;");
        ArrayList<String> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(resultSet.getString(0));
        }
        recipients.setItems(FXCollections.observableList(users));
        resultSet.close();

        resultSet = AppData.database.executeQuery("select * from player_data order by date_time;");
        while (resultSet.next()) {
            messagebox.appendText(
                    resultSet.getString("date_time") +
                            "\nFrom : " + resultSet.getString("sender") +
                            "\nTo : " + resultSet.getString("receiver") +
                            "\nMessage :" +
                            "\n" + resultSet.getString("message") +
                            "\n"
            );
        }
    }

    @FXML
    private void whileTypingMessage(){
        if(message.getText().length() >= 250) {
            message.setText(message.getText().substring(0,250));
        }
    }

    @FXML
    private void onSend(){

    }
}
