package main;

import main.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class InfoDisplayed {
    public GamePanel gp;
    BufferedImage hart;

    public InfoDisplayed(GamePanel gp) throws IOException {
        this.gp = gp;

        try {
        hart = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/gameIcons/hart.png")));
        }catch (Exception _){
            System.out.println("Image not found");
        }
    }

    public void displayText(Graphics2D g2d) {
        // Set text color to white
        g2d.setColor(Color.WHITE);

        // Use a classic arcade-style font
        g2d.setFont(customFont());

        int scoreDrawPosition = gp.leftRightMargin + gp.tileSize;
        // Display "SCORE" text
        g2d.drawString("SCORE", scoreDrawPosition, 25);
        // Display score number below
        String pointsText = String.format("%06d", gp.player.points);
        g2d.drawString(pointsText, scoreDrawPosition, 45);

        int highScoreDrawPosition = gp.windowWidth - gp.leftRightMargin - gp.tileSize * 7;
        // Display "HIGH SCORE" text
        g2d.drawString("HIGH SCORE", highScoreDrawPosition, 25);
        // Display high score number below
        String highScoreText = String.format("%06d", gp.player.score);
        g2d.drawString(highScoreText, highScoreDrawPosition + gp.tileSize, 45);

        int levelDrawPosition = gp.windowWidth / 2 - gp.tileSize;
        // Display "HIGH SCORE" text
        g2d.drawString("LEVEL", levelDrawPosition, 25);
        // Display high score number below
        g2d.drawString(String.valueOf(gp.player.level), levelDrawPosition + gp.tileSize / 2, 45);
    }

    private Font customFont() {
        Font customFont;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/fonts/ARCADE_I.TTF")).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        return customFont;
    }

    private void drawPlayerData(Graphics2D g2d) {
        int life = gp.player.life;
        for (int i = 0; i < life; i++) {
            int col = i * gp.tileSize + gp.leftRightMargin + gp.tileSize;
            int row = (int) (gp.windowHeight - gp.tileSize * 1.5);
            g2d.drawImage(hart, col, row, gp.tileSize , gp.tileSize, null);
        }
    }

    public void draw(Graphics2D g2d) {
        if(!gp.inPlayBackMode) drawPlayerData(g2d);
        displayText(g2d);
    }
}
