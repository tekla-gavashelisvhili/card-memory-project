package org.example.memorycardgameproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverController {

    @FXML private Label scoreLabel;
    @FXML private Label movesLabel;

    @FXML
    public void initialize() {
        scoreLabel.setText(String.valueOf(GameSettings.getScore()));
        movesLabel.setText(String.valueOf(GameSettings.getMoves()));
    }

    @FXML
    private void tryAgain() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/memorycardgameproject/game.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void goToMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/memorycardgameproject/menu.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
