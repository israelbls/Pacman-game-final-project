package tils;


import main.panels.GamePanel;
import main.Maps;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[16];
        loadTileImages();
        mapTileNum = Maps.map3;
    }

    public void loadTileImages() {
        try {
            String path = "src/assets/images/walls/%s.png";

            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(new File(String.format(path, "redWall")));

            for (int i = 1; i < 16; i++) {
                tiles[i] = new Tile();
                tiles[i].image = ImageIO.read(new File(String.format(path, "new_walls/" + i)));
                if (i > 1) tiles[i].collision = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (int i = 0; i < mapTileNum.length; i++) {
            for (int j = 0; j < mapTileNum[i].length; j++) {
                int tileNum = mapTileNum[j][i];
                if (tileNum == 0) continue;
                int col = i * gp.tileSize + gp.leftRightMargin;
                int row = j * gp.tileSize + gp.topBottomMargin;
                g2.drawImage(tiles[tileNum].image, col, row, gp.tileSize, gp.tileSize, null);
            }
        }
    }
}