package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all ghost entities in the Pacman game.
 * Responsible for creating, updating, and controlling the behavior
 * of all ghost characters (Blinky, Pinky, Inky, and Clyde).
 */
public class GhostsManager {
    /** List containing all ghost instances */
    public final List<Ghost> ghosts;

    /**
     * Constructs a new GhostsManager and initializes all ghosts.
     * Creates Blinky, Pinky, Inky, and Clyde in their default positions.
     * Note: Inky requires a reference to Blinky for his movement calculations.
     * 
     * @param gp GamePanel instance for game state access
     */
    public GhostsManager(GamePanel gp) {
        ghosts = new ArrayList<>();
        ghosts.add(new BlinkyGhost(gp));
        ghosts.add(new PinkyGhost(gp));
        ghosts.add(new InkyGhost(gp, (BlinkyGhost) ghosts.getFirst()));
        ghosts.add(new ClydeGhost(gp));
    }

    /**
     * Updates the state and position of all ghosts.
     * Called each game tick to move and update ghost behavior.
     * 
     * @throws IOException If there's an error during playback mode
     */
    public void update() throws IOException {
        for (Ghost ghost : ghosts) {
            ghost.update();
        }
    }

    /**
     * Puts all non-eaten ghosts into frightened mode.
     * Called when Pacman eats a power pellet.
     * Resets the mode timer for all ghosts.
     */
    public void frightened() {
        for (Ghost ghost : ghosts) {
            if (!ghost.state.equals("Eaten")) ghost.enterMode("Frightened");
            ghost.timeCounter = 0;
        }
    }

    /**
     * Draws all ghosts on the game screen.
     * 
     * @param g2d Graphics2D object for rendering
     */
    public void draw(Graphics2D g2d) {
        for (Ghost ghost : ghosts) {
            ghost.draw(g2d);
        }
    }

    /**
     * Resets all ghosts to their starting positions.
     * Called when Pacman loses a life or at game start.
     * Also sets all ghosts to be in their home area.
     */
    public void resetAllGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
            ghost.inHome = true;
        }
    }
}
