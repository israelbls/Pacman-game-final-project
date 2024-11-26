package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;

/**
 * Represents Inky, the cyan ghost in Pacman.
 * Inky has a complex behavior that depends on both Pacman's and Blinky's positions.
 * He uses a "midpoint" based on Pacman's position and direction, then doubles the
 * vector from Blinky to this midpoint to determine his target.
 */
public class InkyGhost extends Ghost {
    /** Reference to Blinky ghost for position calculations */
    private final BlinkyGhost blinky;

    /**
     * Constructs a new Inky ghost.
     * @param gp GamePanel instance for game state access
     * @param blinky Reference to Blinky ghost for target calculations
     */
    public InkyGhost(GamePanel gp, BlinkyGhost blinky) {
        super(gp, "inky");
        this.blinky = blinky;
    }

    /**
     * Sets default values for Inky's position and behavior.
     * Inky starts in the center-right of the maze and moves left.
     */
    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 13;
        entityY = gp.tileSize * 10;
        speed = 1;
        direction = "left";

        state = "Scatter";
        scatterModeTarget = new Point((gp.maxScreenRow - 2) * gp.tileSize, (gp.maxScreenCol - 2) * gp.tileSize);
        target = eatenModeTarget;
    }

    /**
     * Gets Inky's chase mode target.
     * Target is calculated by:
     * 1. Finding a midpoint based on Pacman's position and direction
     * 2. Doubling the vector from Blinky to this midpoint
     * 
     * @return Point representing Inky's target position
     */
    @Override
    protected Point getChaseModeTarget() {
        Point blinkyPosition = new Point(blinky.entityX, blinky.entityY);
        return calculateInkyTarget(blinkyPosition, getMidPoint());
    }

    /**
     * Calculates Inky's target by doubling the vector from Blinky to midpoint.
     * 
     * @param blinky Blinky's current position
     * @param midpoint Reference point based on Pacman's position
     * @return Point representing Inky's target
     */
    private Point calculateInkyTarget(Point blinky, Point midpoint) {
        int dx = midpoint.x - blinky.x;
        int dy = midpoint.y - blinky.y;

        int inkyTargetX = midpoint.x + dx;
        int inkyTargetY = midpoint.y + dy;

        return new Point(inkyTargetX, inkyTargetY);
    }

    /**
     * Gets the midpoint based on Pacman's position and direction.
     * The midpoint is 2 tiles ahead of Pacman in his current direction,
     * except when moving up (2 tiles up and 2 tiles left).
     * 
     * @return Point representing the midpoint position
     * @throws IllegalStateException if Pacman's direction is invalid
     */
    private Point getMidPoint() {
        String playerDir = gp.player.direction;
        int playerX = gp.player.entityX;
        int playerY = gp.player.entityY;
        return switch (playerDir) {
            case "up" -> new Point(playerX - (gp.tileSize * 2), playerY - (gp.tileSize * 2));
            case "down" -> new Point(playerX, playerY + (gp.tileSize * 2));
            case "left" -> new Point(playerX - (gp.tileSize * 2), playerY);
            case "right" -> new Point(playerX + (gp.tileSize * 2), playerY);
            default -> throw new IllegalStateException("Unexpected value: " + gp.player.direction);
        };
    }
}