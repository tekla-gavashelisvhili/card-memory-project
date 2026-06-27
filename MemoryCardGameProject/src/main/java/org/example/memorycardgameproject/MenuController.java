package org.example.memorycardgameproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class MenuController {

    @FXML private ToggleButton btnEasy;
    @FXML private ToggleButton btnMedium;
    @FXML private ToggleButton btnHard;

    private static final String EASY_SELECTED   = "-fx-background-color: #00ffe740; -fx-background-radius: 10; -fx-text-fill: #00ffe7; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #00ffe7; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;";
    private static final String EASY_UNSELECTED = "-fx-background-color: #00ffe710; -fx-background-radius: 10; -fx-text-fill: #00ffe780; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #00ffe740; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-cursor: hand;";

    private static final String MED_SELECTED    = "-fx-background-color: #ff6b0040; -fx-background-radius: 10; -fx-text-fill: #ff6b00; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #ff6b00; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;";
    private static final String MED_UNSELECTED  = "-fx-background-color: #ff6b0010; -fx-background-radius: 10; -fx-text-fill: #ff6b0080; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #ff6b0040; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-cursor: hand;";

    private static final String HARD_SELECTED   = "-fx-background-color: #ff003c40; -fx-background-radius: 10; -fx-text-fill: #ff003c; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #ff003c; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;";
    private static final String HARD_UNSELECTED = "-fx-background-color: #ff003c10; -fx-background-radius: 10; -fx-text-fill: #ff003c80; -fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #ff003c40; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-cursor: hand;";

    @FXML
    public void initialize() {
        updateStyles();

        btnEasy.setOnAction(e -> updateStyles());
        btnMedium.setOnAction(e -> updateStyles());
        btnHard.setOnAction(e -> updateStyles());
    }

    private void updateStyles() {
        if (btnEasy.isSelected()) {
            btnEasy.setStyle(EASY_SELECTED);
        } else {
            btnEasy.setStyle(EASY_UNSELECTED);
        }

        if (btnMedium.isSelected()) {
            btnMedium.setStyle(MED_SELECTED);
        } else {
            btnMedium.setStyle(MED_UNSELECTED);
        }

        if (btnHard.isSelected()) {
            btnHard.setStyle(HARD_SELECTED);
        } else {
            btnHard.setStyle(HARD_UNSELECTED);
        }
    }

    @FXML
    private void startGame() throws Exception {
        if (btnEasy.isSelected()) {
            GameSettings.setDifficulty(1);
        } else if (btnMedium.isSelected()) {
            GameSettings.setDifficulty(2);
        } else if (btnHard.isSelected()) {
            GameSettings.setDifficulty(3);
        } else {
            GameSettings.setDifficulty(1);
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/memorycardgameproject/game.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) btnEasy.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }
}