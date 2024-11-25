package main;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String HIGH_SCORES_FILE = "highscores.dat";
    private List<HighScoreEntry> highScores;
    private static final int MAX_HIGH_SCORES = 10;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public void addScore(String playerName, int score, LocalDateTime dateTime, long gameDuration) {
        highScores.add(new HighScoreEntry(playerName, score, dateTime, gameDuration));
        sortHighScores();
        
        // Keep only top 10 scores
        if (highScores.size() > MAX_HIGH_SCORES) {
            highScores = highScores.subList(0, MAX_HIGH_SCORES);
        }
        
        saveHighScores();
    }

    private void sortHighScores() {
        Collections.sort(highScores, (a, b) -> b.getScore() - a.getScore());
    }

    public List<HighScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }

    private void loadHighScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORES_FILE))) {
            highScores = (List<HighScoreEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty list
            highScores = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            highScores = new ArrayList<>();
        }
    }

    private void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORES_FILE))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
