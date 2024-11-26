package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;

/**
 * Represents Clyde, the orange ghost in Pacman.
 * Clyde has a unique behavior - he chases Pacman only when far away,
 * but retreats to his scatter corner when too close to Pacman.
 */
public class ClydeGhost extends Ghost {

    /**
     * Constructs a new Clyde ghost.
     * @param gp GamePanel instance for game state access
     */
    public ClydeGhost(GamePanel gp) {
        super(gp, "clyde");
    }

    /**
     * Sets default values for Clyde's position and behavior.
     * Clyde starts in the center-right of the maze and moves left.
     */
    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 15;
        entityY = gp.tileSize * 10;
        speed = 1;
        direction = "left";

        state = "Scatter";
        scatterModeTarget = new Point(2 * gp.tileSize, (gp.maxScreenCol - 2) * gp.tileSize);
        target = eatenModeTarget;
    }

    /**
     * Gets Clyde's chase mode target.
     * If Pacman is far away (> 8 tiles), Clyde targets Pacman directly.
     * If Pacman is close (≤ 8 tiles), Clyde retreats to his scatter corner.
     * 
     * @return Point representing either Pacman's position or scatter target
     */
    @Override
    protected Point getChaseModeTarget() {
        int playerX = gp.player.entityX;
        int playerY = gp.player.entityY;

        Point playerLocation = new Point(playerX, playerY);
        Point clydeLocation = new Point(entityX, entityY);

        return isPlayerNearClyde(playerLocation, clydeLocation) ?
                new Point(playerX, playerY) : scatterModeTarget;
    }

    /**
     * Checks if Pacman is within Clyde's chase distance.
     * 
     * @param p1 Pacman's position
     * @param p2 Clyde's position
     * @return true if distance is less than 8 tiles
     */
    private boolean isPlayerNearClyde(Point p1, Point p2) {
        int CHASE_DISTANCE = 8 * gp.tileSize;
        return p1.distance(p2) < CHASE_DISTANCE;
    }
}