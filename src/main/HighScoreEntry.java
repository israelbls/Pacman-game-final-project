package main;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HighScoreEntry implements Serializable {
    private String playerName;
    private int score;
    private LocalDateTime dateTime;
    private long gameDuration; // in seconds

    public HighScoreEntry(String playerName, int score, LocalDateTime dateTime, long gameDuration) {
        this.playerName = playerName;
        this.score = score;
        this.dateTime = dateTime;
        this.gameDuration = gameDuration;
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public LocalDateTime getDateTime() { return dateTime; }
    public long getGameDuration() { return gameDuration; }
}
