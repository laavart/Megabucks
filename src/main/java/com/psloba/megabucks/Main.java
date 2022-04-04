package com.psloba.megabucks;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Label gp;
    public Label luck;
    public TextArea scorebox;

    public Label name;
    public ChoiceBox<String> difficulty;
    public Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12;

    @FXML
    private void initialize() throws SQLException {

        //recipients
        AppData.users = AppData.database.getAllUsers();
        List<String> users = new ArrayList<>(AppData.users.size());
        for(var user : AppData.users.keySet()) users.add(AppData.users.get(user));
        recipients.setItems(FXCollections.observableList(users));

        //messagebox
        ResultSet resultSet = AppData.database.executeQuery("select * from player_" + AppData.client.key() + " order by date_time;");
        while (resultSet.next()) {
            String sender = AppData.users.get(resultSet.getInt("sender"));
            String receiver = AppData.users.get(resultSet.getInt("receiver"));
            messagebox.appendText(
                    resultSet.getString("date_time") +
                            "\nFrom : " + (sender.equals(AppData.client.value().user().username()) ? "YOU" : sender) +
                            "\nTo : " + (receiver.equals(AppData.client.value().user().username()) ? "YOU" : receiver) +
                            "\nMessage :" +
                            "\n" + resultSet.getString("message") +
                            "\n\n"
            );
        }
        resultSet.close();

        //scorebox
        resultSet = AppData.database.executeQuery("" +
                "select * from player_data where id = " + AppData.client.key() + ";"
        );
        if(resultSet.next()) {
            int score = resultSet.getInt("score");
            int money = resultSet.getInt("money");
            this.score.setText(String.valueOf(score));
            this.money.setText(String.valueOf(money));
            scorebox.appendText(
                    Timestamp.valueOf(LocalDateTime.now()).toString().substring(0,19) +
                            "\nScore: : " + score +
                            "\nMoney: : " + money +
                            "\n\n"
            );
        }

        //name
        String name = AppData.client.value().user().name().trim();
        int index = name.indexOf(' ');
        if(index != -1) this.name.setText(name.substring(0, index));
        else this.name.setText(name);

        //difficulty
        difficulty.setItems(FXCollections.observableList(List.of("Easy", "Medium", "Hard")));
        difficulty.setValue("Easy");
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
    private void onSend() {

        String timestamp = Timestamp.valueOf(LocalDateTime.now()).toString().substring(0,19);
        String username = recipients.getValue();

        if(AppData.users.containsValue(username)) {

            int uID = -1;
            for(var id : AppData.users.keySet()) if(Objects.equals(AppData.users.get(id), username)) {
                uID = id;
                break;
            }
            String message = this.message.getText();

            try {
                AppData.database.executeUpdate("Start Transaction;");
                AppData.database.executeUpdate(
                        "insert into player_" + AppData.client.key() + " value(" +
                                "'" + timestamp + "'," +
                                AppData.client.key() + "," +
                                uID + "," +
                                "'" + message + "'" +
                                ");"
                );
                AppData.database.executeUpdate(
                        "insert into player_" + uID + " value(" +
                                "'" + timestamp + "'," +
                                AppData.client.key() + "," +
                                uID + "," +
                                "'" + message + "'" +
                                ");"
                );
                AppData.database.executeUpdate("commit;");

                messagebox.appendText(
                        timestamp +
                                "\nFrom : YOU" +
                                "\nTo : " + username +
                                "\nMessage :" +
                                "\n" + message +
                                "\n\n"
                );

                this.message.setText("");
            }
            catch (SQLException e) {
                try {
                    AppData.database.executeUpdate("rollback;");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(timestamp);
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
                gp.setText("5%");
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
                gp.setText("15%");
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
                gp.setText("25%");
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
        alert.setTitle(Timestamp.valueOf(LocalDateTime.now()).toString().substring(0,19));

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
                    "update player_data where id = " + AppData.client.key() + "set " +
                            "money = " + money + "," +
                            "score = " + score +
                            ";"
            );
            if(check) {
                scorebox.appendText(
                        Timestamp.valueOf(LocalDateTime.now()).toString().substring(0,19) +
                                "\n" + alert.getContentText() +
                                "\nScore: : " + score +
                                "\nMoney: : " + money +
                                "\n\n"
                );
                this.score.setText(String.valueOf(score));
                this.money.setText(String.valueOf(money));
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
