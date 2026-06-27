package org.example.memorycardgameproject;

public class GameSettings {

    private static int difficulty = 1;
    private static int score = 0;
    private static int moves = 0;
    private static int timeUsed = 0;
    private static int bestCombo = 0;

    public static int getDifficulty() { return difficulty; }
    public static void setDifficulty(int d) { difficulty = d; }

    public static int getScore() { return score; }
    public static void setScore(int s) { score = s; }

    public static int getMoves() { return moves; }
    public static void setMoves(int m) { moves = m; }

    public static int getTimeUsed() { return timeUsed; }
    public static void setTimeUsed(int t) { timeUsed = t; }

    public static int getBestCombo() { return bestCombo; }
    public static void setBestCombo(int c) { bestCombo = c; }

    public static int getGridSize() {
        if (difficulty == 1) return 4;
        if (difficulty == 2) return 6;
        return 8;
    }

    public static double getCardSize() {
        if (difficulty == 1) return 110.0;
        if (difficulty == 2) return 80.0;
        return 66.0;
    }

    public static int getTimeLimit() {
        if (difficulty == 1) return 60;
        if (difficulty == 2) return 90;
        return 120;
    }

    public static String getDifficultyName() {
        if (difficulty == 1) return "Easy";
        if (difficulty == 2) return "Medium";
        return "Hard";
    }
}

