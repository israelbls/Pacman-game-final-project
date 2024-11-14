package objects;

import main.GamePanel;
import main.Maps;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class OBJ_Coin extends GameObject{
    public OBJ_Coin(GamePanel gp) throws IOException {
        super(gp);
        name = "Coin";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/objects/coin.png")));
            initializeCoinsPositions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCoinsPositions() throws IOException {
        int[][] map = Maps.map1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[j][i] == 0){
                    ObjectManager.positions[j][i] = name;
                }
            }
        }
    }

    public void draw(Graphics2D g2d){
        for (int i = 0; i < ObjectManager.positions.length; i++) {
            for (int j = 0; j < ObjectManager.positions[i].length; j++) {
                if (ObjectManager.positions[j][i] != null && ObjectManager.positions[j][i].equals(name)){
                    int col = i * gp.tileSize + gp.leftRightMargin + gp.tileSize / 4;
                    int row = j * gp.tileSize + gp.topBottomMargin + gp.tileSize / 4;
                    g2d.drawImage(image, col, row, gp.tileSize / 2, gp.tileSize / 2, null);
                }
            }
        }
    }

}
