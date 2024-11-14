package objects;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class ObjectManager {
    GamePanel gp;
    public static String[][] positions;
    OBJ_Coin coins;


    public ObjectManager(GamePanel gp) throws IOException {
        this.gp = gp;
        positions = new String[gp.maxScreenRow][gp.maxScreenCol];
        this.coins = new OBJ_Coin(gp);
    }

    public void draw(Graphics2D g2) {
        coins.draw(g2);
    }

}
