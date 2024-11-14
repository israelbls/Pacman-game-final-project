package entitys.ghosts;

import main.GamePanel;

import java.awt.*;

public class GhostsManager {
    GamePanel gp;
    BlinkyGhost blinky;
    PinkyGhost pinky;
    InkyGhost inky;
    ClydeGhost clyde;

    public GhostsManager(GamePanel gp) {
        this.gp = gp;
        blinky = new BlinkyGhost(gp);
        pinky = new PinkyGhost(gp);
        inky = new InkyGhost(gp,this.blinky);
        clyde = new ClydeGhost(gp);

        chase();
    }

    public void update() {
        blinky.update();
        pinky.update();
        inky.update();
        clyde.update();
    }

    public void chase(){
        blinky.enterMode("Chase");
        inky.enterMode("Chase");
    }

    public void draw(Graphics2D g2d) {
        blinky.draw(g2d);
        pinky.draw(g2d);
        inky.draw(g2d);
        clyde.draw(g2d);
    }
}
