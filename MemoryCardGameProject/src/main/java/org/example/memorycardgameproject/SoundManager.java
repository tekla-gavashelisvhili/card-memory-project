package org.example.memorycardgameproject;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static AudioClip flipSound;
    private static AudioClip matchSound;
    private static AudioClip wrongSound;
    private static AudioClip winSound;

    public static void load() {
        try {
            flipSound  = new AudioClip(SoundManager.class.getResource("/org/example/memorycardgameproject/sounds/flip.wav").toString());
            matchSound = new AudioClip(SoundManager.class.getResource("/org/example/memorycardgameproject/sounds/match.wav").toString());
            wrongSound = new AudioClip(SoundManager.class.getResource("/org/example/memorycardgameproject/sounds/wrong.wav").toString());
            winSound   = new AudioClip(SoundManager.class.getResource("/org/example/memorycardgameproject/sounds/win.wav").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playFlip()  { if (flipSound  != null) flipSound.play(); }
    public static void playMatch() { if (matchSound != null) matchSound.play(); }
    public static void playWrong() { if (wrongSound != null) wrongSound.play(); }
    public static void playWin()   { if (winSound   != null) winSound.play(); }
}
