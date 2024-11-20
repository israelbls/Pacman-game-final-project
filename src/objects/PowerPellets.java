package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class PowerPellets extends GameObject {
    public String[][] positions;
    int animationCounter = 0;
    boolean draw = true;

    public PowerPellets(GamePanel gp) throws IOException {
        super(gp);
        positions = new String[gp.maxScreenCol][gp.maxScreenRow];
        name = "PowerPellets";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/objects/coin.png")));
            initializeCoinsPositions();
        } catch (IOException _) {
            System.out.println("Error loading image" + getClass());
        }
    }

    private void initializeCoinsPositions() throws IOException {
        positions[1][17] = "PowerPellets";
        positions[17][1] = "PowerPellets";
        positions[1][1] = "PowerPellets";
        positions[17][17] = "PowerPellets";
    }

    public void draw(Graphics2D g2d) {
        animationCounter++;
        if (animationCounter > 60) {
            animationCounter = 0;
            draw = !draw;
        }
        if (!draw) return;
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                if (positions[j][i] != null) {
                    int col = i * gp.tileSize + gp.leftRightMargin;
                    int row = j * gp.tileSize + gp.topBottomMargin;
                    g2d.drawImage(image, col, row, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}
