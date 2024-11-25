package entitys;

import entitys.ghosts.Ghost;
import main.panels.GamePanel;
import main.KeyEventsHandler;
import main.HighScoreManager;
import objects.ObjectManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * Represents the player-controlled Pacman character in the game.
 * Handles player movement, animation, collision detection, scoring,
 * and game state management.
 */
public class Player extends Entity {
    // Death animation resources
    private final List<BufferedImage> death = new ArrayList<>();
    private int deathAnimationFrame = 0;
    private int deathAnimationCounter = 0;
    private static final int DEATH_ANIMATION_DELAY = 3;  // At 60 FPS, ~20 frames per second

    // Game state variables
    public int points;  // Current game points
    public int score;   // High score
    public int level;   // Current game level
    public int life;    // Remaining lives

    // Player state flags
    public boolean dead;
    public boolean isPlayingDeathAnimation;
    public boolean gameOver = false;
    public boolean win = false;

    // Game components
    private HighScoreManager highScoreManager;
    GamePanel gp;
    KeyEventsHandler keyEH;

    /**
     * Constructs a new Player instance.
     * Initializes player properties, loads resources, and sets up collision detection.
     *
     * @param gp The GamePanel instance
     * @param keyEH The key event handler for player input
     * @throws IOException If there's an error loading player resources
     */
    public Player(GamePanel gp, KeyEventsHandler keyEH) throws IOException {
        this.gp = gp;
        this.keyEH = keyEH;
        this.score = getHighScore();
        this.highScoreManager = new HighScoreManager();

        // Set the player's solid area (collision box)
        solidArea = new Rectangle();
        solidArea.x = 5;
        solidArea.y = 5;
        solidArea.height = 14;
        solidArea.width = 14;

        points = 0;
        level = 1;
        life = 3;

        setDefaultValues();
        getPlayerImage();
        loadDeathImages();
    }

    /**
     * Sets default values for player position and state.
     * Called at game start and after death.
     */
    public void setDefaultValues() {
        entityX = 12 * gp.tileSize;
        entityY = 21 * gp.tileSize;
        speed = 3;
        direction = "right";

        dead = false;
        isPlayingDeathAnimation = false;
        deathAnimationFrame = 0;
        deathAnimationCounter = 0;
    }

    /**
     * Loads death animation frames from resources.
     */
    private void loadDeathImages() {
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

    /**
     * Loads player animation sprites for different directions.
     */
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(new File("src/assets/images/pacman/pacman_up1.png"));
            up2 = ImageIO.read(new File("src/assets/images/pacman/pacman_up2.png"));
            down1 = ImageIO.read(new File("src/assets/images/pacman/pacman_down1.png"));
            down2 = ImageIO.read(new File("src/assets/images/pacman/pacman_down2.png"));
            right1 = ImageIO.read(new File("src/assets/images/pacman/pacman_right1.png"));
            right2 = ImageIO.read(new File("src/assets/images/pacman/pacman_right2.png"));
            left1 = ImageIO.read(new File("src/assets/images/pacman/pacman_left1.png"));
            left2 = ImageIO.read(new File("src/assets/images/pacman/pacman_left2.png"));
        } catch (IOException _) {
            System.out.println("Error loading images");
        }
    }

    /**
     * Updates player state, handles movement, collisions, and animations.
     * Called every frame during game loop.
     *
     * @throws IOException If there's an error during playback mode
     */
    public void update() throws IOException {
        if (win && !gp.inPlayBackMode) win();
        if (gameOver && !gp.inPlayBackMode) lose();
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

        String previousDirection = direction;

        if (gp.inPlayBackMode) {
            getDirectionInPlayBackMode();
        } else {
            // Check if this is the first move
            boolean isFirstMove = !keyEH.upPressed && !keyEH.downPressed &&
                    !keyEH.leftPressed && !keyEH.rightPressed &&
                    direction.equals("right") &&
                    entityX == 12 * gp.tileSize &&
                    entityY == 21 * gp.tileSize;

            if (isFirstMove) {
                return; // Don't move until player provides input
            }

            // Handle direction changes based on input
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

        // Maintain previous direction if new direction is blocked
        if (!canMove(direction)) {
            direction = previousDirection;
        }

        // Move player if path is clear
        if (canMove(direction)) {
            if (gp.inPlayBackMode) {
                updateLocationInPlayBackMode();
            } else {
                updateLocation();
            }

            snapToGrid(direction);

            // Check collisions with game objects
            gp.collisionChecker.checkObject(this, gp.objectManager.coins.positions);
            gp.collisionChecker.checkObject(this, gp.objectManager.powerPellets.positions);
            gp.collisionChecker.checkObject(this, gp.objectManager.fruit.positions);

            // Update animation
            frameCounter++;
            if (frameCounter > 5) {
                positionNumber = (positionNumber == 1) ? 2 : 1;
                frameCounter = 0;
            }
        }
    }

    /**
     * Updates player position based on current direction.
     * Handles screen wrapping when player moves off-screen.
     */
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
                if (entityX + solidArea.x < 0) {
                    entityX = gp.screenWidth - gp.tileSize;
                }
                break;
            case "right":
                entityX += speed;
                if (entityX + gp.tileSize > gp.screenWidth) {
                    entityX = 0;
                }
                break;
        }
    }

    /**
     * Gets player data from current frame during playback mode.
     */
    private String playerTab() throws IOException {
        return gp.gameRecorder.getCurrentFrame(gp.frameCounter).split("\\|")[0];
    }

    /**
     * Updates player location during playback mode.
     */
    private void updateLocationInPlayBackMode() throws IOException {
        String position = playerTab().split("@")[1];
        entityX = Integer.parseInt(position.split("#")[0]);
        entityY = Integer.parseInt(position.split("#")[1]);
    }

    /**
     * Updates player direction during playback mode.
     */
    public void getDirectionInPlayBackMode() throws IOException {
        this.direction = playerTab().split("@")[0];
    }

    /**
     * Aligns player position to grid based on movement direction.
     * Ensures smooth movement along grid lines.
     *
     * @param direction Current movement direction
     */
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

    /**
     * Checks if movement in a specific direction is possible.
     * Performs collision detection with walls and objects.
     *
     * @param direction Direction to check
     * @return true if movement is possible, false otherwise
     */
    private boolean canMove(String direction) throws IOException {
        int originalX = entityX;
        int originalY = entityY;

        // Test movement
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

        // Check collisions
        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkObject(this, gp.objectManager.coins.positions);
        gp.collisionChecker.checkObject(this, gp.objectManager.powerPellets.positions);

        // Restore position
        entityX = originalX;
        entityY = originalY;

        return !collisionOn;
    }

    /**
     * Updates death animation state.
     * Controls animation timing and frame progression.
     */
    private void updateDeathAnimation() {
        deathAnimationCounter++;
        if (deathAnimationCounter >= DEATH_ANIMATION_DELAY) {
            deathAnimationCounter = 0;
            deathAnimationFrame++;

            if (deathAnimationFrame >= death.size()) {
                isPlayingDeathAnimation = false;
                dead = false;
                deathAnimationFrame = 0;
                deathAnimationCounter = 0;
                setDefaultValues();
            }
        }
    }

    /**
     * Draws the player's sprite based on the current direction and animation frame.
     *
     * @param g2d The Graphics2D object for rendering
     */
    public void draw(Graphics2D g2d) {
        if (dead && isPlayingDeathAnimation) {
            drawDeathAnimation(g2d);
            return;
        }

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

    /**
     * Draws the death animation.
     *
     * @param g2d The Graphics2D object for rendering
     */
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

    /**
     * Handles high score management.
     * Updates high score if current score is higher.
     */
    public void handleHighScore() {
        if (points > getHighScore()) {
            saveHighScore(points);
        }

        String playerName = JOptionPane.showInputDialog(null,
                "Enter your name for the high score:",
                "New High Score!",
                JOptionPane.PLAIN_MESSAGE);

        if (playerName != null && !playerName.trim().isEmpty()) {
            long gameDuration = gp.frameCounter / 60; // Convert from frames to seconds
            highScoreManager.addScore(playerName.trim(), points, LocalDateTime.now(), gameDuration);
        }
    }

    /**
     * Displays a game end message.
     *
     * @param title The message title
     * @param message The message content
     */
    private void showGameEndMessage(String title, String message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException _) {
        }
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sets the game end state.
     *
     * @param isWin true if the game is won, false otherwise
     */
    public void setGameEndState(boolean isWin) {
        gameOver = true;
        win = isWin;
        gp.running = false;
        gp.stopMusic();
    }

    /**
     * Handles game win logic.
     */
    void win() throws IOException {
        setGameEndState(true);
        gp.playSE(1);
        handleHighScore();
        showGameEndMessage("Victory!", "YOU WIN!\nFinal Score: " + points);
        if (points > score) showGameEndMessage("High Score", "HIGH SCORE!\nFinal Score: " + points);
        gp.returnToMenu();
    }

    /**
     * Advances to the next game level.
     */
    public void nextLevel() throws IOException {
        if (level == 3) {
            win = true;
            return;
        }
        level++;
        life++;
        gp.setBackground(level == 2 ? Color.BLUE : Color.RED);
        setDefaultValues();
        for (Ghost ghost : gp.ghostsManager.ghosts) {
            ghost.resetPosition();
            ghost.speed++;
        }
        gp.objectManager = new ObjectManager(gp);
    }

    /**
     * Handles game loss logic.
     */
    void lose() throws IOException {
        setGameEndState(false);
        handleHighScore();
        showGameEndMessage("Game Over", "GAME OVER!\nFinal Score: " + points);
        if (points > score) showGameEndMessage("High Score", "HIGH SCORE!\nFinal Score: " + points);
        gp.returnToMenu();
    }

    /**
     * Retrieves the current high score.
     *
     * @return The high score value
     */
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

    /**
     * Saves the high score to file.
     *
     * @param score The new high score value
     */
    private void saveHighScore(int score) {
        try {
            FileWriter writer = new FileWriter("high_score.txt");
            writer.write(String.valueOf(score));
            writer.close();
        } catch (Exception _) {
        }
    }
}