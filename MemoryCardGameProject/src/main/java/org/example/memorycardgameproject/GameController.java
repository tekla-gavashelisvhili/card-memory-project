package org.example.memorycardgameproject;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;import javafx.collections.ObservableList;
import javafx.scene.Node;

public class GameController {

    @FXML private GridPane gridPane;
    @FXML private Label timeLabel;
    @FXML private Label scoreLabel;
    @FXML private Label movesLabel;
    @FXML private Label comboLabel;
    @FXML private Button hintButton;

    private final ScoreManager scoreManager = new ScoreManager();
    private final Set<Button> matchedButtons = new HashSet<>();

    private Button firstButton = null;
    private Button secondButton = null;
    private boolean canClick = true;
    private int matchedPairs = 0;
    private int totalPairs;
    private int timeLeft;
    private Timeline countdown;
    private double imageWidth;
    private double imageHeight;

    private int combo = 0;
    private int bestCombo = 0;
    private boolean hintUsed = false;

    @FXML
    public void initialize() {
        SoundManager.load();
        setupGame();
    }

    private void setupGame() {
        int gridSize = GameSettings.getGridSize();
        totalPairs = (gridSize * gridSize) / 2;
        timeLeft = GameSettings.getTimeLimit();

        scoreManager.reset();
        matchedPairs = 0;
        firstButton = null;
        secondButton = null;
        canClick = true;
        matchedButtons.clear();
        combo = 0;
        bestCombo = 0;
        hintUsed = false;

        updateUI();
        hintButton.setDisable(false);
        hintButton.setStyle("-fx-background-color: #ffcc0020; -fx-background-radius: 8; -fx-text-fill: #ffcc00; -fx-font-size: 13px; -fx-border-color: #ffcc00; -fx-border-radius: 8; -fx-border-width: 1; -fx-cursor: hand;");
        timeLabel.setText(String.valueOf(timeLeft));
        timeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #ff6b00; -fx-font-weight: bold;");

        List<String> deck = new ArrayList<>();
        for (int i = 1; i <= totalPairs; i++) {
            deck.add(String.format("animal_%02d.png", i));
            deck.add(String.format("animal_%02d.png", i));
        }
        Collections.shuffle(deck);

        ObservableList<Node> children = gridPane.getChildren();
        children.clear();
        double cardSize = GameSettings.getCardSize();
        double horizontalPadding = 8.0;
        double verticalPadding = 8.0;
        imageWidth = cardSize - (horizontalPadding * 2);
        imageHeight = cardSize - (verticalPadding * 2);

        int index = 0;
        for (int row = 0; row < GameSettings.getGridSize(); row++) {
            for (int col = 0; col < GameSettings.getGridSize(); col++) {
                String imageName = deck.get(index++);
                Button btn = new Button();
                btn.setPrefSize(cardSize, cardSize);
                btn.setUserData(imageName);
                btn.setStyle(hiddenStyle());
                btn.setOnAction(e -> handleClick(btn, imageWidth,imageHeight));
                gridPane.add(btn, col, row);
            }
        }

        startCountdown();
    }

    private void handleClick(Button btn, double imageSize, double imageHeight) {
        if (!canClick) return;
        if (matchedButtons.contains(btn)) return;
        if (btn == firstButton) return;

        showCard(btn, imageWidth, imageHeight);

        if (firstButton == null) {
            firstButton = btn;
        } else {
            secondButton = btn;
            canClick = false;
            scoreManager.addMove();
            updateUI();
            checkMatch();
        }
    }

    private void checkMatch() {
        String first = (String) firstButton.getUserData();
        String second = (String) secondButton.getUserData();

        if (first.equals(second)) {
            SoundManager.playMatch();
            combo++;
            if (combo > bestCombo) bestCombo = combo;

            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(e -> {
                firstButton.setStyle(matchedStyle());
                secondButton.setStyle(matchedStyle());
                firstButton.setUserData(null);
                secondButton.setUserData(null);
                matchedButtons.add(firstButton);
                matchedButtons.add(secondButton);

                int points = 10 * combo;
                scoreManager.addPoints(points);

                matchedPairs++;
                updateUI();
                resetTurn();

                if (matchedPairs == totalPairs) {
                    countdown.stop();
                    SoundManager.playWin();
                    PauseTransition win = new PauseTransition(Duration.millis(500));
                    win.setOnFinished(ev -> winGame());
                    win.play();
                }
            });
            pause.play();
        } else {
            SoundManager.playWrong();
            combo = 0;

            firstButton.setStyle(wrongStyle());
            secondButton.setStyle(wrongStyle());

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                firstButton.setGraphic(null);
                secondButton.setGraphic(null);
                firstButton.setStyle(hiddenStyle());
                secondButton.setStyle(hiddenStyle());
                resetTurn();
            });
            pause.play();

            updateUI();
        }
    }

    @FXML
    private void useHint() {
        if (hintUsed || !canClick) return;
        hintUsed = true;
        hintButton.setDisable(true);
        hintButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 8; -fx-text-fill: #444455; -fx-font-size: 13px; -fx-border-color: #333344; -fx-border-radius: 8; -fx-border-width: 1;");

        canClick = false;

        final List<Button> hidden = new ArrayList<>();
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            Button btn = (Button) gridPane.getChildren().get(i);
            if (!matchedButtons.contains(btn) && btn.getGraphic() == null) {
                hidden.add(btn);
            }
        }

        for (int i = 0; i < hidden.size(); i++) {
            Button btn = hidden.get(i);
            showCardSilent(btn, imageWidth, imageHeight);
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            for (int i = 0; i < hidden.size(); i++) {
                Button btn = hidden.get(i);
                if (!matchedButtons.contains(btn)) {
                    btn.setGraphic(null);
                    btn.setStyle(hiddenStyle());
                }
            }
            canClick = true;
        });
        pause.play();
    }

    private void showCard(Button btn, double imageWidth,double imageHeight) {
        SoundManager.playFlip();
        RotateTransition rotateOut = new RotateTransition(Duration.millis(150), btn);
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(90);
        rotateOut.setAxis(new Point3D(0, 1, 0));
        rotateOut.setOnFinished(e -> {
            loadImage(btn, imageWidth,imageHeight);
            btn.setStyle(flippedStyle());
            RotateTransition rotateIn = new RotateTransition(Duration.millis(150), btn);
            rotateIn.setFromAngle(90);
            rotateIn.setToAngle(0);
            rotateIn.setAxis(new Point3D(0, 1, 0));
            rotateIn.play();
        });
        rotateOut.play();
    }

    private void showCardSilent(Button btn, double imageSize, double imageHeight) {
        loadImage(btn, imageWidth, this.imageHeight);
        btn.setStyle(flippedStyle());
    }

    private void loadImage(Button btn, double imageWidth, double imageHeight) {
        String imageName = (String) btn.getUserData();
        if (imageName == null) return;
        Image image = new Image(getClass().getResourceAsStream("/" + imageName));
        ImageView view = new ImageView(image);
        view.setFitWidth(imageWidth);
        view.setFitHeight(imageHeight);
        btn.setGraphic(view);
    }

    private void resetTurn() {
        firstButton = null;
        secondButton = null;
        canClick = true;
    }

    private void updateUI() {
        scoreLabel.setText(String.valueOf(scoreManager.getScore()));
        movesLabel.setText(String.valueOf(scoreManager.getMoves()));
        comboLabel.setText("x" + combo);
    }

    private void startCountdown() {
        if (countdown != null) countdown.stop();
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timeLabel.setText(String.valueOf(timeLeft));
            if (timeLeft <= 10) {
                timeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #ff003c; -fx-font-weight: bold;");
            } else if (timeLeft <= 20) {
                timeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #ffcc00; -fx-font-weight: bold;");
            }
            if (timeLeft <= 0) {
                countdown.stop();
                gameOver();
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    private String hiddenStyle() {
        return "-fx-background-color: #1a1a2e; -fx-background-radius: 10; -fx-border-color: #2a2a4a; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-cursor: hand;";
    }

    private String flippedStyle() {
        return "-fx-background-color: #0d2a2a; -fx-background-radius: 10; -fx-border-color: #00ffe7; -fx-border-radius: 10; -fx-border-width: 2; -fx-cursor: hand;";
    }

    private String matchedStyle() {
        return "-fx-background-color: #0a2a1a; -fx-background-radius: 10; -fx-border-color: #00cc66; -fx-border-radius: 10; -fx-border-width: 2;";
    }

    private String wrongStyle() {
        return "-fx-background-color: #2a0d0d; -fx-background-radius: 10; -fx-border-color: #ff003c; -fx-border-radius: 10; -fx-border-width: 2;";
    }

    private void winGame() {
        GameSettings.setScore(scoreManager.getScore());
        GameSettings.setMoves(scoreManager.getMoves());
        GameSettings.setTimeUsed(timeLeft);
        GameSettings.setBestCombo(bestCombo);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/memorycardgameproject/winner.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void gameOver() {
        GameSettings.setScore(scoreManager.getScore());
        GameSettings.setMoves(scoreManager.getMoves());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/memorycardgameproject/gameover.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void restartGame() {
        if (countdown != null) countdown.stop();
        setupGame();
    }

    @FXML
    private void goToMenu() {
        if (countdown != null) countdown.stop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/memorycardgameproject/menu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }
}