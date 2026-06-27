package org.example.memorycardgameproject;

public class ScoreManager {

    private int score = 0;
    private int moves = 0;

    public void addMove() { moves++; }
    public void addPoints(int points) { score += points; }

    public int getScore() { return score; }
    public int getMoves() { return moves; }

    public void reset() {
        score = 0;
        moves = 0;
    }
}