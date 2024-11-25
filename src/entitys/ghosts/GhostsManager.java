package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GhostsManager {
    public final List<Ghost> ghosts;

    public GhostsManager(GamePanel gp) {
        ghosts = new ArrayList<>();
        ghosts.add(new BlinkyGhost(gp));
        ghosts.add(new PinkyGhost(gp));
        ghosts.add(new InkyGhost(gp, (BlinkyGhost) ghosts.getFirst()));
        ghosts.add(new ClydeGhost(gp));
    }

    public void update() throws IOException {
        for (Ghost ghost : ghosts) {
            ghost.update();
        }
    }

    public void frightened() {
        for (Ghost ghost : ghosts) {
            if (!ghost.state.equals("Eaten"))ghost.enterMode("Frightened");
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
            ghost.inHome = true;
        }
    }
}
