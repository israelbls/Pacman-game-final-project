package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;

/**
 * Represents Pinky, the pink ghost in Pacman.
 * Pinky's strategy is to ambush Pacman by targeting 4 tiles ahead
 * of Pacman's current position and direction. When Pacman moves up,
 * Pinky targets 4 tiles up and 4 tiles left due to the original game's behavior.
 */
public class PinkyGhost extends Ghost {
    
    /**
     * Constructs a new Pinky ghost.
     * @param gp GamePanel instance for game state access
     */
    public PinkyGhost(GamePanel gp) {
        super(gp, "pinky");
    }

    /**
     * Sets default values for Pinky's position and behavior.
     * Pinky starts in the center of the maze and moves down.
     */
    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 11;
        entityY = gp.tileSize * 10;
        speed = 1;
        direction = "down";

        state = "Scatter";
        scatterModeTarget = new Point(2 * gp.tileSize, 2 * gp.tileSize);
        target = eatenModeTarget;
    }

    /**
     * Gets Pinky's chase mode target.
     * Target is 4 tiles ahead of Pacman in his current direction.
     * When Pacman moves up, target is 4 tiles up and 4 tiles left
     * (replicating the original game's targeting behavior).
     * 
     * @return Point representing target position
     * @throws IllegalStateException if Pacman's direction is invalid
     */
    @Override
    protected Point getChaseModeTarget() {
        String playerDir = gp.player.direction;
        int playerX = gp.player.entityX;
        int playerY = gp.player.entityY;
        return switch (playerDir) {
            case "up" -> new Point(playerX - (gp.tileSize * 4), playerY - (gp.tileSize * 4));
            case "down" -> new Point(playerX, playerY + (gp.tileSize * 4));
            case "left" -> new Point(playerX - (gp.tileSize * 4), playerY);
            case "right" -> new Point(playerX + (gp.tileSize * 4), playerY);
            default -> throw new IllegalStateException("Unexpected value: " + gp.player.direction);
        };
    }
}