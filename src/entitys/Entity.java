package entitys;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int entityX, entityY, speed;
    public BufferedImage up1, up2, down1, down2, right1, right2, left1, left2;
    public String direction;

    public int frameCounter = 0;
    public int positionNumber = 1;
    public Rectangle solidArea;
    public boolean collisionOn = false;


}