package entitys.ghosts;

import main.GamePanel;

import java.awt.*;

public class ClydeGhost extends Ghost {

    public ClydeGhost(GamePanel gp) {
        super(gp, "clyde");
    }

    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 8;
        entityY = gp.tileSize * 9;
        speed = 2;
        direction = "left";

        state = "Scatter";
        scatterModeTarget = new Point(2 * gp.tileSize, (gp.maxScreenCol - 2) * gp.tileSize);
        target = scatterModeTarget;
    }

    @Override
    protected Point getChaseModeTarget() {
        int playerX = gp.player.entityX;
        int playerY = gp.player.entityY;

        Point playerLocation = new Point(playerX, playerY);
        Point clydeLocation = new Point(entityX, entityY);

        return isPlayerNearClyde(playerLocation, clydeLocation) ?
                new Point(playerX, playerY) : scatterModeTarget;
    }

    private boolean isPlayerNearClyde(Point p1, Point p2) {
        int CHASE_DISTANCE = 8 * gp.tileSize;
        return p1.distance(p2) < CHASE_DISTANCE;
    }
}