package entitys;

import main.GamePanel;
import main.KeyEventsHandler;
import main.MenuPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Player extends Entity {
    private final List<BufferedImage> death = new ArrayList<>();
    private int deathAnimationFrame = 0;
    private int deathAnimationCounter = 0;
    private static final int DEATH_ANIMATION_DELAY = 3;  // At 60 FPS, this means ~20 frames per second for death animation

    public int points;
    public int score;
    public int level;
    public int life;

    public boolean dead;
    public boolean isPlayingDeathAnimation;

    public boolean gameOver = false;
    public boolean win = false;


    GamePanel gp;
    KeyEventsHandler keyEH;

    public Player(GamePanel gp, KeyEventsHandler keyEH) throws IOException {
        this.gp = gp;
        this.keyEH = keyEH;

        // Set the player's solid area (collision box)
        solidArea = new Rectangle();
        solidArea.x = 5;
        solidArea.y = 5;
        solidArea.height = 22;
        solidArea.width = 22;

        points = 0;
        score = getHighScore();
        level = 1;
        life = 6;

        setDefaultValues();
        getPlayerImage();
        loadDeathImages();
    }

    public void setDefaultValues() {
        // Set the player's initial position and speed
        entityX = gp.screenWidth / 2 - (gp.tileSize / 2);
        entityY = (gp.screenHeight / 4) * 3 + ((gp.tileSize / 4) * 3);
        speed = 4;
        direction = "right";

        dead = false;
        isPlayingDeathAnimation = false;
        deathAnimationFrame = 0;
        deathAnimationCounter = 0;
    }

    private void loadDeathImages() throws IOException {
        for (int i = 0; i < 26; i++) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/pacman/death/death_" + i + ".png")));
            } catch (Exception _) {
                System.out.println("Error loading death image " + i + ".png");
            }
            death.add(image);
        }
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
        } catch (IOException _) {
            System.out.println("Error loading images");
        }
    }


    public void update() throws IOException {
        if (win) win();
        if (gameOver) lose();
        if (dead) {
            if (!isPlayingDeathAnimation) {
                // Start death animation
                isPlayingDeathAnimation = true;
                deathAnimationFrame = 0;
                deathAnimationCounter = 0;
            } else {
                updateDeathAnimation();
            }
            return;
        }

        // Store the current direction before checking for new input
        String previousDirection = direction;

        if (gp.inPlayBackMode){
            getDirectionInPlayBackMode(gp.gameRecorder.getCurrentFrame(gp.frameCounter));
        }else {
            // Check if this is the first move
            boolean isFirstMove = !keyEH.upPressed && !keyEH.downPressed &&
                    !keyEH.leftPressed && !keyEH.rightPressed &&
                    direction.equals("right") &&
                    entityX == gp.screenWidth / 2 - (gp.tileSize / 2) &&
                    entityY == gp.screenHeight * 0.75 + (gp.tileSize * 0.75);

            if (isFirstMove) {
                return; // Don't move until player provides input
            }

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

        }

        // If we can't move in the new direction, keep the previous direction
        if (!canMove(direction)) {
            direction = previousDirection;
        }

        // Continue moving in the current direction if possible
        if (canMove(direction)) {
            // Move according to direction
            updateLocation();

            snapToGrid(direction);

            // Check for collisions with coins, power pellets, and fruits
            gp.collisionChecker.checkObject(this, gp.objectManager.coins.positions);
            gp.collisionChecker.checkObject(this, gp.objectManager.powerPellets.positions);
            gp.collisionChecker.checkObject(this, gp.objectManager.fruit.positions);

            // Update animation frame
            frameCounter++;
            if (frameCounter > 5) {
                positionNumber = (positionNumber == 1) ? 2 : 1;
                frameCounter = 0;
            }
        }
    }

    public void updateLocation() {
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
    }

    private void snapToGrid(String direction) {
        switch (direction) {
            case "up", "down":
                entityX = Math.round((float) entityX / gp.tileSize) * gp.tileSize;
                break;
            case "right", "left":
                entityY = Math.round((float) entityY / gp.tileSize) * gp.tileSize;
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

        gp.collisionChecker.checkObject(this, gp.objectManager.coins.positions);
        gp.collisionChecker.checkObject(this, gp.objectManager.powerPellets.positions);


        // Restore original position
        entityX = originalX;
        entityY = originalY;

        return !collisionOn;
    }

    private void updateDeathAnimation() {
        deathAnimationCounter++;
        if (deathAnimationCounter >= DEATH_ANIMATION_DELAY) {
            deathAnimationCounter = 0;
            deathAnimationFrame++;

            // Check if animation is complete
            if (deathAnimationFrame >= death.size()) {
                // Reset animation
                isPlayingDeathAnimation = false;
                dead = false;
                deathAnimationFrame = 0;
                deathAnimationCounter = 0;

                // Reset player position
                setDefaultValues();
            }
        }
    }

    public void getDirectionInPlayBackMode(String frame){
        this.direction = frame.split("\\|")[0];
    }

    public void draw(Graphics2D g2d) {
        if (dead && isPlayingDeathAnimation) {
            drawDeathAnimation(g2d);
            return;
        }
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

    private void drawDeathAnimation(Graphics2D g2d) {
        if (deathAnimationFrame < death.size()) {
            BufferedImage currentFrame = death.get(deathAnimationFrame);
            if (currentFrame != null) {
                int col = entityX + gp.leftRightMargin - gp.tileSize / 3;
                int row = entityY + gp.topBottomMargin - gp.tileSize / 3;
                int width = (int) (gp.tileSize * 1.4);
                int height = (int) (gp.tileSize * 1.4);
                g2d.drawImage(currentFrame, col, row, width, height, null);
            }
        }
    }

    public void handleHighScore() throws IOException {
        if (points > getHighScore()) {
            saveHighScore(points);
        }
    }

    private void showGameEndMessage(String title, String message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException _) {}
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void setGameEndState(boolean isWin) {
        gameOver = true;
        win = isWin;
        gp.running = false;
        gp.stopMusic();
    }

    void win() throws IOException {
        setGameEndState(true);
        gp.playSE(1);
        handleHighScore();
        showGameEndMessage("Victory!", "YOU WIN!\nFinal Score: " + points);
        if (points > score)showGameEndMessage("High Score" , "HIGH SCORE!\nFinal Score: " + points);
        SwingUtilities.invokeLater(gp::returnToMenu);
    }

    void lose() throws IOException {
        setGameEndState(false);
        handleHighScore();
        showGameEndMessage("Game Over", "GAME OVER!\nFinal Score: " + points);
        if (points > score)showGameEndMessage("High Score" , "HIGH SCORE!\nFinal Score: " + points);
        SwingUtilities.invokeLater(gp::returnToMenu);
    }

    private void returnToMenu() throws IOException {
        // Use GamePanel's returnToMenu method
        SwingUtilities.invokeLater(() -> gp.returnToMenu());
    }

    public int getHighScore() {
        try {
            File file = new File("high_score.txt");
            Scanner scanner = new Scanner(file);
            int highScore = scanner.nextInt();
            scanner.close();
            return highScore;
        } catch (Exception e) {
            return 0;
        }
    }

    private void saveHighScore(int score) {
        try {
            FileWriter writer = new FileWriter("high_score.txt");
            writer.write(String.valueOf(score));
            writer.close();
        } catch (Exception _) {
        }
    }
}