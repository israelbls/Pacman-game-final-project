package objects;

import main.panels.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Abstract base class for all game objects (coins, power pellets, fruits).
 * Provides common properties and functionality for rendering and collision detection.
 */
public abstract class GameObject {
    /** The object's sprite image */
    public BufferedImage image;
    
    /** Reference to the game panel */
    protected GamePanel gp;
    
    /** Name identifier for the object */
    public String name;
    
    /** Flag indicating if this object can be collided with */
    public boolean collision = false;
    
    /** Collision boundary for the object */
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    /**
     * Constructs a new game object.
     * 
     * @param gp GamePanel instance for game state access
     * @throws IOException If there's an error loading object resources
     */
    public GameObject(GamePanel gp) throws IOException {
        this.gp = gp;
    }

    /**
     * Draws the object on the game screen.
     * 
     * @param g2 Graphics2D object for rendering
     */
    public abstract void draw(Graphics2D g2);
}