package entitys;

import main.GamePanel;
import main.KeyEventsHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {
    public int points;
    public int score;
    public int level;

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

        points = 0;
        score = 0;
        level = 1;
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
        // Check if this is the first move
        boolean isFirstMove = !keyEH.upPressed && !keyEH.downPressed &&
                !keyEH.leftPressed && !keyEH.rightPressed &&
                direction.equals("right") &&
                entityX == gp.screenWidth / 2 - (gp.tileSize / 2) &&
                entityY == (gp.screenHeight / 4) * 3 + ((gp.tileSize / 4) * 3);

        if (isFirstMove) {
            return; // Don't move until player provides input
        }

        // Store the current direction before checking for new input
        String previousDirection = direction;

        // Check if any new direction is pressed
        if (keyEH.upPressed && canMove("up")) {
            direction = "up";
        } else if (keyEH.downPressed && canMove("down")) {
            direction = "down";
        } else if (keyEH.leftPressed && canMove("left")) {
            direction = "left";
        } else if (keyEH.rightPressed && canMove("right")) {
            direction = "right";
        }

        // If we can't move in the new direction, keep the previous direction
        if (!canMove(direction)) {
            direction = previousDirection;
        }

        // Continue moving in the current direction if possible
        if (canMove(direction)) {
            // Move according to direction
            switch (direction) {
                case "up":
                    entityY -= speed;
                    break;
                case "down":
                    entityY += speed;
                    break;
                case "left":
                    entityX -= speed;
                    // Handle screen wrapping for left side
                    if (entityX + solidArea.x < 0) {
                        entityX = gp.screenWidth - gp.tileSize;
                    }
                    break;
                case "right":
                    entityX += speed;
                    // Handle screen wrapping for right side
                    if (entityX + gp.tileSize > gp.screenWidth) {
                        entityX = 0;
                    }
                    break;
            }

            snapToGrid(direction);

            // Update animation frame
            frameCounter++;
            if (frameCounter > 5) {
                positionNumber = (positionNumber == 1) ? 2 : 1;
                frameCounter = 0;
            }
        }
    }

    private void snapToGrid(String direction) {
        switch (direction) {
            case "up", "down":
                entityX = Math.round((float)entityX / gp.tileSize) * gp.tileSize;
                break;
            case "right", "left":
                entityY = Math.round((float)entityY / gp.tileSize) * gp.tileSize;
                break;
        }
    }

    // Helper method to check if movement in a direction is possible
    private boolean canMove(String direction) {
        // Save current position
        int originalX = entityX;
        int originalY = entityY;

        // Temporarily update position to check collision
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

        // Check collision
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkObject(this);

        // Restore original position
        entityX = originalX;
        entityY = originalY;

        return !collisionOn;
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
        int col = entityX + gp.leftRightMargin;
        int row = entityY + gp.topBottomMargin;
        g2d.drawImage(image, col, row, gp.tileSize, gp.tileSize, null);
    }
}