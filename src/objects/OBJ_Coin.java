package objects;

import main.GamePanel;
import main.Maps;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Coin extends GameObject{
    public String[][] positions;
    public int coins = 0;
    public OBJ_Coin(GamePanel gp) throws IOException {
        super(gp);
        positions = new String[gp.maxScreenCol][gp.maxScreenRow];
        name = "Coin";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/objects/coin.png")));
            initializeCoinsPositions();
        } catch (IOException _) {
        }
    }

    private void initializeCoinsPositions() throws IOException {
        int[][] map = Maps.map1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[j][i] == 0){
                    positions[j][i] = name;
                    coins ++;
                }
            }
        }
        positions[1][17] = null;
        positions[17][1] = null;
        positions[1][1] = null;
        positions[17][17] = null;

        positions[11][17] = null;
        positions[11][18] = null;
        positions[11][1] = null;
        positions[11][0] = null;

        positions[7][18] = null;
        positions[7][17] = null;
        positions[7][1] = null;
        positions[7][0] = null;
        coins -= 12;
    }

    public void draw(Graphics2D g2d){
        for (int i = 0; i <positions.length; i++) {
            for (int j = 0; j <positions[i].length; j++) {
                if (positions[j][i] != null){
                    int col = i * gp.tileSize + gp.leftRightMargin + gp.tileSize / 4;
                    int row = j * gp.tileSize + gp.topBottomMargin + gp.tileSize / 4;
                    g2d.drawImage(image, col, row, gp.tileSize / 2, gp.tileSize / 2, null);
                }
            }
        }
    }

}
