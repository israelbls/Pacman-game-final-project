package objects;

import main.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Represents coins in the game that Pacman can collect.
 * Manages coin positions, rendering, and counting of remaining coins.
 */
public class OBJ_Coin extends GameObject {
    public String[][] positions;  // Grid positions of coins
    public int coins = 0;        // Total number of coins in the game

    /**
     * Constructs a new OBJ_Coin object.
     * Initializes coin positions and loads coin image.
     *
     * @param gp The GamePanel instance
     * @throws IOException If there's an error loading the coin image
     */
    public OBJ_Coin(GamePanel gp) throws IOException {
        super(gp);
        positions = new String[gp.maxScreenCol][gp.maxScreenRow];
        name = "Coin";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/objects/coin.png")));
            initializeCoinsPositions();
        } catch (IOException _) {
            // Handle silently
        }
    }

    /**
     * Initializes the positions of coins on the game map.
     * Places coins in valid positions based on the tile map,
     * excluding specific positions marked as empty.
     *
     * @throws IOException If there's an error accessing the tile map
     */
    private void initializeCoinsPositions() throws IOException {
        // Get tile map and place coins in valid positions
        int[][] map = gp.tileManager.mapTileNum;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[j][i] == 1) {
                    positions[j][i] = name;
                    coins++;
                }
            }
        }

        // Define positions where coins should not appear
        int[][] empty = {
                {1, 23}, {23, 1}, {1, 1}, {23, 23},
                {12, 0}, {12, 1}, {12, 2}, {12, 3}, {12, 21}, {12, 22}, {12, 23}, {12, 24},
                {8, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 21}, {8, 22}, {8, 23}, {8, 24},
                {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {10, 14}, {10, 15}, {9, 12},
                {14, 12}, {18, 12}, {19, 12}
        };

        // Remove coins from empty positions
        for (int[] pos : empty) {
            positions[pos[0]][pos[1]] = null;
        }

        coins -= 12;  // Adjust total coin count
    }

    /**
     * Draws all coins on the game panel.
     * Only draws coins that haven't been collected yet.
     *
     * @param g2d The Graphics2D object for rendering
     */
    public void draw(Graphics2D g2d) {
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                if (positions[j][i] != null) {
                    int col = i * gp.tileSize + gp.leftRightMargin + gp.tileSize / 4;
                    int row = j * gp.tileSize + gp.topBottomMargin + gp.tileSize / 4;
                    g2d.drawImage(image, col, row, gp.tileSize / 2, gp.tileSize / 2, null);
                }
            }
        }
    }
}
