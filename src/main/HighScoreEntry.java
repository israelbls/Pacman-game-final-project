package main;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HighScoreEntry implements Serializable {
    private static final long serialVersionUID = 7986827621735444395L;
    
    private final String playerName;
    private final int score;
    private final LocalDateTime dateTime;
    private final long gameDuration; // in seconds

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
