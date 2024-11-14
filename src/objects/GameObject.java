package objects;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GameObject {
    protected BufferedImage image;
    protected GamePanel gp;
    protected String name;
    protected boolean collision = false;
    protected Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public GameObject(GamePanel gp) throws IOException {
        this.gp = gp;
    }

    public abstract void draw(Graphics2D g2);
}