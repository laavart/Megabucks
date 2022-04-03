package com.psloba.megabucks;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import citra.util.Pair;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Main {

    private int selection = 1;
    private String mode = "Easy";

    public TextArea messagebox;
    public ComboBox<String> recipients;
    public TextArea message;
    public Button send;

    public Label money;
    public Label score;
    public Label profit;
    public Label luck;
    public TextArea scorebox;

    public Label name;
    public ChoiceBox<String> difficulty;
    public Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;

    @FXML
    private void initialize() throws SQLException {

        //recipients
        ResultSet resultSet = AppData.database.executeQuery("Select uID, username from user_master;");
        ArrayList<String> users = new ArrayList<>();
        while (resultSet.next()) {
            AppData.users.put(resultSet.getString("username"), resultSet.getInt("uID"));
            users.add(resultSet.getString(1));
        }
        recipients.setItems(FXCollections.observableList(users));
        resultSet.close();

        //messagebox
        resultSet = AppData.database.executeQuery("select * from player_" + AppData.client.getKey() + " order by date_time;");
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

        //name
        String name = AppData.client.getValue().user().name().trim();
        this.name.setText(name.substring(0,name.indexOf(' ')));

        //difficulty
        difficulty.setItems(FXCollections.observableList(List.of("Easy", "Medium", "Hard")));
        difficulty.setOnAction(e -> onSelection());

        //b...
        b1.setOnAction(e -> selection = 1);
        b2.setOnAction(e -> selection = 2);
        b3.setOnAction(e -> selection = 3);
        b4.setOnAction(e -> selection = 4);
        b5.setOnAction(e -> selection = 5);
        b6.setOnAction(e -> selection = 6);
        b7.setOnAction(e -> selection = 7);
        b8.setOnAction(e -> selection = 8);
        b9.setOnAction(e -> selection = 9);
        b10.setOnAction(e -> selection = 10);
        b11.setOnAction(e -> selection = 11);
        b12.setOnAction(e -> selection = 12);
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
    private void onClear(){
        scorebox.setText("");
    }

    @FXML
    private void onLogOut() throws IOException {
        AppData.users = null;
        AppData.client = null;
        AppData.stage.setScene(AppData.getScene("login"));
    }

    @FXML
    private void onSelection(){
        mode = difficulty.getValue();

        switch (mode) {
            case "Easy" -> {
                b5.setDisable(true);
                b6.setDisable(true);
                b7.setDisable(true);
                b8.setDisable(true);
                b9.setDisable(true);
                b10.setDisable(true);
                b11.setDisable(true);
                b12.setDisable(true);
                profit.setText("5%");
                luck.setText("25%");
            }
            case "Medium" -> {
                b5.setDisable(false);
                b6.setDisable(false);
                b7.setDisable(false);
                b8.setDisable(false);
                b9.setDisable(true);
                b10.setDisable(true);
                b11.setDisable(true);
                b12.setDisable(true);
                profit.setText("15%");
                luck.setText("12.5%");
            }
            case "Hard" -> {
                b5.setDisable(false);
                b6.setDisable(false);
                b7.setDisable(false);
                b8.setDisable(false);
                b9.setDisable(false);
                b10.setDisable(false);
                b11.setDisable(false);
                b12.setDisable(false);
                profit.setText("25%");
                luck.setText("8.33%");
            }
            default -> {
                difficulty.setValue("Easy");
                onSelection();
            }
        }
    }

    @FXML
    private void onStart() {
        int win = 0;
        switch (mode){
            case "Easy" -> win = (int) (Math.random() * 4 +1);
            case "Medium" -> win = (int) (Math.random() * 8 +1);
            case "Hard" -> win = (int) (Math.random() * 12 +1);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int money = Integer.parseInt(this.money.getText());
        int score = Integer.parseInt(this.score.getText());

        if (win == selection) {
            switch (mode) {
                case "Easy" -> {
                    money = money * 21 / 20;
                    score += 5;
                }
                case "Medium" -> {
                    money = money * 23 / 20;
                    score += 10;
                }
                case "Hard" -> {
                    money = money * 5 / 4;
                    score += 20;
                }
            }
            alert.setContentText("You Win!");
        }
        else if (win!=0) {
            switch (mode) {
                case "Easy" -> {
                    money = money * 19 / 20;
                    score -= 5;
                }
                case "Medium" -> {
                    money = money * 17 / 20;
                    score -= 10;
                }
                case "Hard" -> {
                    money = money * 3 / 4;
                    score -= 20;
                }
            }
            alert.setContentText("You Lose!");
        }
        else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Error! Please restart the program...");
        }

        try {
            boolean check = AppData.database.executeUpdate(
                    "update player_data where id = " + AppData.client.getKey() + "set " +
                            "money = " + money + "," +
                            "score = " + score +
                            ";"
            );
            if(check) {
                scorebox.appendText(
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                                "\n" + alert.getContentText() +
                                "\nScore: : " + score +
                                "\nMoney: : " + money +
                                "\n\n"
                );
            }
            else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("Error! Please restart the program...");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        alert.show();
    }
}
