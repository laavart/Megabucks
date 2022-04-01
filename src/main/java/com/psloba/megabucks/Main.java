package com.psloba.megabucks;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
                "select * from player_data where id = " + AppData.clientID + ";"
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

        int sender = AppData.clientID, receiver = AppData.users.get(recipients.getValue());
        LocalDateTime stamp = LocalDateTime.now();
        String message = this.message.getText();
        messagebox.appendText(
                stamp.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                        "\nFrom : " + sender +
                        "\nTo : " + receiver +
                        "\nMessage :" +
                        "\n" + message +
                        "\n\n"
        );

        //database
        AppData.database.executeUpdate("Start Transaction;");
        AppData.database.executeUpdate(
                "insert into player_" + sender + "(" +
                        stamp.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                        sender + "," +
                        receiver + "," +
                        "'" + message + "'" +
                        ");"
        );
        AppData.database.executeUpdate(
                "insert into player_" + receiver + "(" +
                        stamp.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                        sender + "," +
                        receiver + "," +
                        "'" + message + "'" +
                        ");"
        );
        AppData.database.executeUpdate("commit;");
    }
}
