package objects;

import main.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents power pellets in the game.
 * Power pellets are special items that allow Pacman to eat ghosts.
 * They are placed in the four corners of the maze and flash periodically.
 */
public class PowerPellets extends GameObject {
    /** Grid positions of power pellets */
    public String[][] positions;
    
    /** Counter for controlling pellet animation */
    int animationCounter = 0;
    
    /** Flag controlling pellet visibility for flashing effect */
    boolean draw = true;

    /**
     * Constructs a new PowerPellets object.
     * Initializes pellet positions and loads pellet image.
     * 
     * @param gp GamePanel instance for game state access
     * @throws IOException If there's an error loading the pellet image
     */
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

    /**
     * Initializes power pellet positions.
     * Places pellets in the four corners of the maze.
     * 
     * @throws IOException If there's an error accessing game resources
     */
    private void initializeCoinsPositions() throws IOException {
        positions[1][23] = "PowerPellets";
        positions[23][1] = "PowerPellets";
        positions[1][1] = "PowerPellets";
        positions[23][23] = "PowerPellets";
    }

    /**
     * Draws power pellets on the game screen.
     * Implements a flashing effect by toggling pellet visibility
     * every 60 frames (approximately once per second).
     * 
     * @param g2d Graphics2D object for rendering
     */
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
