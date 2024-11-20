package entitys.ghosts;

import main.GamePanel;

import java.awt.*;

public class InkyGhost extends Ghost {
    private BlinkyGhost blinky;

    public InkyGhost(GamePanel gp, BlinkyGhost blinky) {
        super(gp, "inky");
        this.blinky = blinky;
    }

    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 10;
        entityY = gp.tileSize * 9;
        speed = 2;
        direction = "left";

        state = "Scatter";
        scatterModeTarget = new Point((gp.maxScreenRow - 2) * gp.tileSize, (gp.maxScreenCol - 2) * gp.tileSize);
        eatenModeTarget = new Point(entityX, entityY);
        target = scatterModeTarget;
    }

    @Override
    protected Point getChaseModeTarget() {
        Point blinkyPosition = new Point(blinky.entityX, blinky.entityY);
        return calculateInkyTarget(blinkyPosition,getMidPoint());
    }

    private Point calculateInkyTarget(Point blinky, Point midpoint) {
        int dx = midpoint.x - blinky.x;
        int dy = midpoint.y - blinky.y;

        int inkyTargetX = midpoint.x + dx;
        int inkyTargetY = midpoint.y + dy;

        return new Point(inkyTargetX, inkyTargetY);
    }

    private Point getMidPoint() {
        String playerDir = gp.player.direction;
        int playerX = gp.player.entityX;
        int playerY = gp.player.entityY;
        return switch (playerDir) {
            case "up" -> new Point(playerX - (gp.tileSize * 2), playerY - (gp.tileSize * 2));
            case "down" -> new Point(playerX, playerY + (gp.tileSize * 2));
            case "left" -> new Point(playerX - (gp.tileSize * 2), playerY);
            case "right" -> new Point(playerX + (gp.tileSize * 2), playerY);
            default -> throw new IllegalStateException("Unexpected value: " + gp.player.direction);
        };
    }

}