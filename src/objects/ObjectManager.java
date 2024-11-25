package objects;

import main.panels.GamePanel;

import java.awt.*;
import java.io.IOException;


public class ObjectManager {
    GamePanel gp;
    public OBJ_Coin coins;
    public PowerPellets powerPellets;
    public Fruit fruit;


    public ObjectManager(GamePanel gp) throws IOException {
        this.gp = gp;
        this.coins = new OBJ_Coin(gp);
        this.powerPellets = new PowerPellets(gp);
        this.fruit = new Fruit(gp);
    }

    public void draw(Graphics2D g2) {
        coins.draw(g2);
        powerPellets.draw(g2);
        fruit.draw(g2);
    }

    public void update() throws IOException {
        if (gp.inPlayBackMode) {
            String frame = gp.gameRecorder.getCurrentFrame(gp.frameCounter);
            fruit.playBackMode(frame);
        } else {
            fruit.update();
        }
    }

}
