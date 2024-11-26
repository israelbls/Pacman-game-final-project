package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;

/**
 * Represents Blinky, the red ghost in Pacman.
 * Blinky's behavior is the most aggressive - he directly targets Pacman's current position.
 */
public class BlinkyGhost extends Ghost {

    /**
     * Constructs a new Blinky ghost.
     * @param gp GamePanel instance for game state access
     */
    public BlinkyGhost(GamePanel gp) {
        super(gp, "blinky");
    }

    /**
     * Sets default values for Blinky's position and behavior.
     * Blinky starts near the center of the maze and moves right.
     */
    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 9;
        entityY = gp.tileSize * 10;
        speed = 1;
        direction = "right";

        state = "Scatter";
        scatterModeTarget = new Point((gp.maxScreenRow - 2) * gp.tileSize, 2 * gp.tileSize);
        target = eatenModeTarget;
    }

    /**
     * Gets Blinky's chase mode target.
     * In chase mode, Blinky directly targets Pacman's current position.
     * @return Point representing Pacman's current position
     */
    @Override
    protected Point getChaseModeTarget() {
        return new Point(gp.player.entityX, gp.player.entityY);
    }
}