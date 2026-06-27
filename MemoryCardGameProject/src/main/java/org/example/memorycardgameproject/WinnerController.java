package org.example.memorycardgameproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WinnerController {

    @FXML private Label diffLabel;
    @FXML private Label finalScoreLabel;
    @FXML private Label finalMovesLabel;
    @FXML private Label finalTimeLabel;
    @FXML private Label finalComboLabel;

    @FXML
    public void initialize() {
        diffLabel.setText(GameSettings.getDifficultyName());
        finalScoreLabel.setText(String.valueOf(GameSettings.getScore()));
        finalMovesLabel.setText(String.valueOf(GameSettings.getMoves()));
        finalTimeLabel.setText(GameSettings.getTimeUsed() + "s");
        finalComboLabel.setText(String.valueOf(GameSettings.getBestCombo()));
    }

    @FXML
    private void playAgain() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/memorycardgameproject/game.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) diffLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void goToMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/memorycardgameproject/menu.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) diffLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
