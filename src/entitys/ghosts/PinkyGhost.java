package entitys.ghosts;

import main.panels.GamePanel;

import java.awt.*;

public class PinkyGhost extends Ghost {
    public PinkyGhost(GamePanel gp) {
        super(gp, "pinky");
    }

    @Override
    protected void setDefaultValues() {
        entityX = gp.tileSize * 11;
        entityY = gp.tileSize * 10;
        speed = 1;
        direction = "down";

        state = "Scatter";
        scatterModeTarget = new Point(2 * gp.tileSize, 2 * gp.tileSize);
        target = eatenModeTarget;
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