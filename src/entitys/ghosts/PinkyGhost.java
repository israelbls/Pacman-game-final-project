package entitys.ghosts;

import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class PinkyGhost extends Ghost {
    public PinkyGhost(GamePanel gp) {
        super(gp, "pinky");
    }

    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 9;
        entityY = gp.tileSize * 8;
        speed = 2;
        direction = "down";

        state = "Scatter";
        scatterModeTarget = new Point(2 * gp.tileSize, 2 * gp.tileSize);
        target = scatterModeTarget;
    }

    @Override
    protected Point getChaseModeTarget() {
        String playerDir = gp.player.direction;
        int playerX = gp.player.entityX;
        int playerY = gp.player.entityY;
        return switch (playerDir) {
            case "up" -> new Point(playerX - (gp.tileSize * 4), playerY - (gp.tileSize * 4));
            case "down" -> new Point(playerX, playerY + (gp.tileSize * 4));
            case "left" -> new Point(playerX - (gp.tileSize * 4), playerY);
            case "right" -> new Point(playerX + (gp.tileSize * 4), playerY);
            default -> throw new IllegalStateException("Unexpected value: " + gp.player.direction);
        };
    }

}