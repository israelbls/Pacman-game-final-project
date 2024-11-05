package entitys;

import main.GamePanel;
import main.KeyEventsHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyEventsHandler keyEH;

    public Player(GamePanel gp, KeyEventsHandler keyEH) {
        this.gp = gp;
        this.keyEH = keyEH;

        // Set the player's solid area (collision box)
        solidArea = new Rectangle();
        solidArea.x = 5;
        solidArea.y = 5;
        solidArea.height = 22;
        solidArea.width = 22;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        // Set the player's initial position and speed
        entityX = gp.screenWidth / 2 - (gp.tileSize / 2);
        entityY = (gp.screenHeight / 4) * 3 + ((gp.tileSize / 4) * 3);
        speed = 4;
        direction = "right";
    }

    public void getPlayerImage() {
        try {
            // Load the player's animation images
            up1 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_up1.png"));
            up2 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_up2.png"));
            down1 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_down1.png"));
            down2 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_down2.png"));
            right1 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_right1.png"));
            right2 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_right2.png"));
            left1 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_left1.png"));
            left2 = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/pacman/pacman_left2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Check for key input and update the player's direction
        if (keyEH.downPressed || keyEH.upPressed || keyEH.leftPressed || keyEH.rightPressed) {
            if (keyEH.upPressed) direction = "up";
            if (keyEH.downPressed) direction = "down";
            if (keyEH.leftPressed) direction = "left";
            if (keyEH.rightPressed) direction = "right";

            // Check for collisions and update the player's position
            collisionOn = false;
            gp.collisionChecker.checkTile(this);
            if (!collisionOn && !outOfScreen(direction)) {
                switch (direction) {
                    case "up":
                        entityY -= speed;
                        break;
                    case "down":
                        entityY += speed;
                        break;
                    case "left":
                        entityX -= speed;
                        break;
                    case "right":
                        entityX += speed;
                        break;
                }
            }

            // Animation frame counter
            frameCounter++;
            if (frameCounter > 5) {
                if (positionNumber == 1) positionNumber = 2;
                else if (positionNumber == 2) positionNumber = 1;
                frameCounter = 0;
            }
        }
    }

    // Check if the player is going off-screen and wrap around
    boolean outOfScreen(String direction) {
        switch (direction) {
            case "left":
                if (entityX - speed < 0) {
                    entityX = gp.screenWidth;
                    return true;
                }
                break;
            case "right":
                if (entityX + speed > gp.screenWidth) {
                    entityX = 0;
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    public void draw(Graphics2D g2d) {
        // Draw the player's sprite based on the current direction and animation frame
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (positionNumber == 1) image = up1;
                if (positionNumber == 2) image = up2;
                break;
            case "down":
                if (positionNumber == 1) image = down1;
                if (positionNumber == 2) image = down2;
                break;
            case "left":
                if (positionNumber == 1) image = left1;
                if (positionNumber == 2) image = left2;
                break;
            case "right":
                if (positionNumber == 1) image = right1;
                if (positionNumber == 2) image = right2;
                break;
        }
        g2d.drawImage(image, entityX, entityY, gp.tileSize, gp.tileSize, null);
    }
}