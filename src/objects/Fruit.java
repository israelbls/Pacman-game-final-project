package objects;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Fruit extends GameObject {
    public String[][] positions;
    BufferedImage chery, strawberry, apple, grapes, lemon;
    private int[] fruitTimers;  // Track frames for each fruit
    private int[] fruitDurations;  // How long each fruit should stay
    private int spawnTimer;  // Timer for next fruit spawn
    private int spawnDelay;  // Random delay before next fruit spawn
    private boolean[] fruitActive; // Track if each fruit is currently active
    private final int BASE_MIN_DURATION = 300;  // 5 seconds (60 frames per second)
    private final int BASE_MAX_DURATION = 600;  // 10 seconds
    private final int BASE_MIN_SPAWN_DELAY = 180;  // 3 seconds
    private final int BASE_MAX_SPAWN_DELAY = 420;  // 7 seconds
    public final String[] fruitTypes = {"Cherry", "Strawberry", "Apple", "Lemon", "Grapes"};
    private Random random;

    public Fruit(GamePanel gp) throws IOException {
        super(gp);
        positions = new String[gp.maxScreenRow][gp.maxScreenCol];
        fruitTimers = new int[fruitTypes.length];
        fruitDurations = new int[fruitTypes.length];
        fruitActive = new boolean[fruitTypes.length];
        random = new Random();
        name = "Fruit";
        try {
            chery = ImageIO.read(new File("src/assets/images/fruits/chery.png"));
            strawberry = ImageIO.read(new File("src/assets/images/fruits/Strawberry.png"));
            apple = ImageIO.read(new File("src/assets/images/fruits/apple.png"));
            lemon = ImageIO.read(new File("src/assets/images/fruits/lemon.png"));
            grapes = ImageIO.read(new File("src/assets/images/fruits/grapes.png"));
            
            if (chery == null || strawberry == null || apple == null || lemon == null || grapes == null) {
                System.out.println("Warning: Some fruit images failed to load!");
            } else {
                // Initialize all fruits as inactive
                for (int i = 0; i < fruitTypes.length; i++) {
                    fruitActive[i] = false;
                    fruitTimers[i] = 0;
                }
                // Set initial spawn delay
                spawnTimer = 0;
                spawnDelay = getRandomSpawnDelay();
            }
        } catch (IOException _) {
        }
    }

    private int getRandomDuration() {
        int scoreReduction = gp.player.points / 1000;  // Reduce duration as score increases
        int minDuration = Math.max(BASE_MIN_DURATION - (scoreReduction * 30), 120);  // Minimum 2 seconds
        int maxDuration = Math.max(BASE_MAX_DURATION - (scoreReduction * 30), 240);  // Minimum 4 seconds
        return random.nextInt(maxDuration - minDuration + 1) + minDuration;
    }

    private int getRandomSpawnDelay() {
        int scoreReduction = (gp.player != null) ? gp.player.points / 1000 : 0;  // Reduce delay as score increases
        int minDelay = Math.max(BASE_MIN_SPAWN_DELAY - (scoreReduction * 20), 60);  // Minimum 1 second
        int maxDelay = Math.max(BASE_MAX_SPAWN_DELAY - (scoreReduction * 20), 180);  // Minimum 3 seconds
        return random.nextInt(maxDelay - minDelay + 1) + minDelay;
    }

    public void update() {
        spawnTimer++;
        
        // Check if it's time to spawn a new fruit
        if (!isAnyFruitActive() || spawnTimer >= spawnDelay) {
            spawnNewFruit();
        }

        // Update active fruits
        for (int i = 0; i < fruitTypes.length; i++) {
            if (fruitActive[i]) {
                fruitTimers[i]++;
                if (fruitTimers[i] >= fruitDurations[i]) {
                    deleteFruit(i);
                }
            }
        }
    }

    private boolean isAnyFruitActive() {
        for (boolean active : fruitActive) if (active) return true;
        return false;
    }

    private void spawnNewFruit() {
        // Select random fruit type
        int fruitIndex = random.nextInt(fruitTypes.length);
        
        // Find random empty position
        int row, col;
        do {
            row = random.nextInt(gp.maxScreenRow);
            col = random.nextInt(gp.maxScreenCol);
        } while (!isValidPosition(row, col));

        // Activate fruit
        fruitActive[fruitIndex] = true;
        fruitTimers[fruitIndex] = 0;
        fruitDurations[fruitIndex] = getRandomDuration();
        positions[row][col] = fruitTypes[fruitIndex];

        // Reset spawn timer
        spawnTimer = 0;
        spawnDelay = getRandomSpawnDelay();
    }

    private boolean isValidPosition(int row, int col) {
        // Check if position is empty and not blocked by walls or other objects
        return positions[row][col] == null && !gp.tileManager.tiles[gp.tileManager.mapTileNum[row][col]].collision;
    }

    public void deleteFruit(int index) {
        if (index >= 0 && index < fruitTypes.length) {
            fruitActive[index] = false;
            fruitTimers[index] = 0;
            // Clear fruit from positions array
            for (int row = 0; row < positions.length; row++) {
                for (int col = 0; col < positions[row].length; col++) {
                    if (positions[row][col] != null && positions[row][col].equals(fruitTypes[index])) {
                        positions[row][col] = null;
                    }
                }
            }
        }
    }

    public int getFruitIndex(String fruitName) {
        for (int i = 0; i < fruitTypes.length; i++)
            if (fruitTypes[i].equals(fruitName)) return i;
        return -1;  // Return -1 if fruit not found
    }

    public void playBackMode(String frame){
        String[] cells = frame.split("\\|");
        String[] fruits = Arrays.copyOfRange(cells, 5, cells.length);
        for (int i = 0; i < fruits.length; i++) {
            if (fruits[i].equals("NA")){
                deleteFruit(i);
            }else {
                String[] locations = fruits[i].split("\\$");
                for (int j = 0; j < locations.length; j++) {
                    int row = Integer.parseInt(locations[j].split("#")[0]);
                    int col = Integer.parseInt(locations[j].split("#")[1]);
                    positions[row][col] = fruitTypes[i];
                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        for (int row = 0; row < positions.length; row++) {
            for (int col = 0; col < positions[row].length; col++) {
                if (positions[row][col] != null) {
                    int x = col * gp.tileSize + gp.leftRightMargin;
                    int y = row * gp.tileSize + gp.topBottomMargin;
                    
                    BufferedImage img = switch (positions[row][col]) {
                        case "Cherry" -> chery;
                        case "Strawberry" -> strawberry;
                        case "Apple" -> apple;
                        case "Lemon" -> lemon;
                        case "Grapes" -> grapes;
                        default -> null;
                    };
                    g2d.drawImage(img, x, y, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}
