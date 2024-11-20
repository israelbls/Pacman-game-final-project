package objects;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class GameObject {
    public BufferedImage image;
    protected GamePanel gp;
    public String name;
    public boolean collision = false;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public GameObject(GamePanel gp) throws IOException {
        this.gp = gp;
    }

    public abstract void draw(Graphics2D g2);
}