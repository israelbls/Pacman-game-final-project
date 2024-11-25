package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;


public class BlinkyGhost extends Ghost {

    public BlinkyGhost(GamePanel gp) {
        super(gp, "blinky");
    }

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

    @Override
    protected Point getChaseModeTarget() {
        return new Point(gp.player.entityX, gp.player.entityY);
    }


}