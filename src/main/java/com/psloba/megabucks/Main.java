package com.psloba.megabucks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.util.Pair;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Main {

    public TextArea messagebox;
    public ComboBox<String> recipients;
    public TextArea message;
    public Button send;

    public Label money;
    public Label score;
    public Label profit;
    public Label luck;
    public TextArea scorebox;

    @FXML
    private void initialize() throws SQLException {

        //recipients
        ResultSet resultSet = AppData.database.executeQuery("Select uID, username from user_master;");
        ArrayList<String> users = new ArrayList<>();
        while (resultSet.next()) {
            AppData.users.put(resultSet.getString(1), resultSet.getInt(0));
            users.add(resultSet.getString(1));
        }
        recipients.setItems(FXCollections.observableList(users));
        resultSet.close();

        //messagebox
        resultSet = AppData.database.executeQuery("select * from player_data order by date_time;");
        while (resultSet.next()) {
            messagebox.appendText(
                    resultSet.getString("date_time") +
                            "\nFrom : " + resultSet.getString("sender") +
                            "\nTo : " + resultSet.getString("receiver") +
                            "\nMessage :" +
                            "\n" + resultSet.getString("message") +
                            "\n\n"
            );
        }
        resultSet.close();

        //scorebox
        resultSet = AppData.database.executeQuery("" +
                "select * from player_data where id = " + AppData.client.getKey() + ";"
        );
        if(resultSet.next()) {
            int score = resultSet.getInt("score");
            int money = resultSet.getInt("money");
            this.score.setText(String.valueOf(score));
            this.money.setText(String.valueOf(money));
            scorebox.appendText(
                    LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                            "\nScore: : " + score +
                            "\nMoney: : " + money +
                            "\n\n"
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
    private void onSend() throws SQLException {

        LocalDateTime stamp = LocalDateTime.now();

        if(AppData.users.containsKey(recipients.getValue())) {
            Pair<Integer, String> receiver = new Pair<>(AppData.users.get(recipients.getValue()), recipients.getValue());
            String message = this.message.getText();

            messagebox.appendText(
                    stamp.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                            "\nFrom : " + AppData.client.getValue().user().name() +
                            "\nTo : " + receiver.getValue() +
                            "\nMessage :" +
                            "\n" + message +
                            "\n\n"
            );

            AppData.database.executeUpdate("Start Transaction;");
            AppData.database.executeUpdate(
                    "insert into player_" + AppData.client.getKey() + "(" +
                            stamp.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                            AppData.client.getKey() + "," +
                            receiver.getKey() + "," +
                            "'" + message + "'" +
                            ");"
            );
            AppData.database.executeUpdate(
                    "insert into player_" + receiver.getKey() + "(" +
                            stamp.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                            AppData.client.getKey() + "," +
                            receiver.getKey() + "," +
                            "'" + message + "'" +
                            ");"
            );
            AppData.database.executeUpdate("commit;");
        }
        else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(stamp.toString());
            alert.setContentText(recipients.getValue() + " does not exist!");
            alert.show();
        }
    }

    @FXML
    private void onLogOut(){
        AppData.users = null;
        AppData.client = null;
        AppData.stage.setScene(AppData.Scenes.get("login"));
    }
}
