package entitys.ghosts;

import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GhostsManager {
    private GamePanel gp;
    public final List<Ghost> ghosts;

    public GhostsManager(GamePanel gp) {
        this.gp = gp;
        ghosts = new ArrayList<>();
        ghosts.add(new BlinkyGhost(gp));
        ghosts.add(new PinkyGhost(gp));
        ghosts.add(new InkyGhost(gp, (BlinkyGhost) ghosts.getFirst()));
        ghosts.add(new ClydeGhost(gp));
    }

    public void update() {
        for (Ghost ghost : ghosts) {
            ghost.update();
        }
    }

    public void frightened() {
        for (Ghost ghost : ghosts) {
            ghost.enterMode("Frightened");
            ghost.timeCounter = 0;
        }
    }

    public void draw(Graphics2D g2d) {
        for (Ghost ghost : ghosts) {
            ghost.draw(g2d);
        }
    }

    public void resetAllGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.resetPosition();
        }
    }
}
