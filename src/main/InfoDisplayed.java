package main;

import java.awt.*;

public class InfoDisplayed {
    GamePanel gp;

    public InfoDisplayed(GamePanel gp){
        this.gp = gp;
    }

    public void displayText(Graphics2D g2d) {
        // Set text color to white
        g2d.setColor(Color.WHITE);

        // Use a classic arcade-style font
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        int scoreDrawPosition = gp.leftRightMargin + gp.tileSize;
        // Display "SCORE" text
        g2d.drawString("SCORE", scoreDrawPosition, 25);
        // Display score number below
        String pointsText = String.format("%06d", gp.player.points);
        g2d.drawString(pointsText, scoreDrawPosition, 45);

        int highScoreDrawPosition = gp.windowWidth - gp.leftRightMargin - gp.tileSize * 4;
        // Display "HIGH SCORE" text
        g2d.drawString("HIGH SCORE", highScoreDrawPosition, 25);
        // Display high score number below
        String highScoreText = String.format("%06d", gp.player.score);
        g2d.drawString(highScoreText, highScoreDrawPosition + gp.tileSize / 2, 45);

        int levelDrawPosition = gp.windowWidth / 2 - gp.tileSize;
        // Display "HIGH SCORE" text
        g2d.drawString("LEVEL", levelDrawPosition, 25);
        // Display high score number below
        g2d.drawString(String.valueOf(gp.player.level), levelDrawPosition + gp.tileSize / 2, 45);
    }
}
