package entitys.ghosts;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class BlinkyGhost extends Ghost {

    public BlinkyGhost(GamePanel gp) {
        super(gp, "blinky");
    }

    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 9;
        entityY = gp.tileSize * 9;
        speed = 2;
        direction = "right";

        eatenModeTarget = new Point(entityX, entityY);
        state = "Scatter";
        scatterModeTarget = new Point((gp.maxScreenRow - 2) * gp.tileSize, 2 * gp.tileSize);
        target = scatterModeTarget;
    }

    @Override
    protected Point getChaseModeTarget() {
        return new Point(gp.player.entityX, gp.player.entityY);
    }


}