package entitys;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Base class for all game entities (player and ghosts).
 * Provides common properties and functionality for movement, animation, and collision.
 */
public class Entity {
    // Position and movement
    public int entityX, entityY;  // Current position in the game world
    public int speed;            // Movement speed of the entity
    
    // Animation sprites for different directions
    public BufferedImage up1, up2;      // Upward movement sprites
    public BufferedImage down1, down2;   // Downward movement sprites
    public BufferedImage right1, right2; // Rightward movement sprites
    public BufferedImage left1, left2;   // Leftward movement sprites
    
    // Movement and animation state
    public String direction;     // Current movement direction
    public int frameCounter = 0; // Counter for animation timing
    public int positionNumber = 1; // Current animation frame (1 or 2)
    
    // Collision detection
    public Rectangle solidArea;      // Collision boundary
    public boolean collisionOn = false; // Collision state flag
}