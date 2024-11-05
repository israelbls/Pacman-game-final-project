package tils;


import main.GamePanel;
import main.Maps;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        loadTileImages();
        mapTileNum = Maps.map1;
    }

    public void loadTileImages() {
        try {
            String path = "C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/walls/%sWall.png";

            tiles[0] = new Tile();
            tiles[0].image = ImageIO.read(new File(String.format(path, "black")));

            tiles[1] = new Tile();
            tiles[1].image = ImageIO.read(new File(String.format(path, "blue")));
            tiles[1].collision = true;

            tiles[2] = new Tile();
            tiles[2].image = ImageIO.read(new File(String.format(path, "red")));

            tiles[3] = new Tile();
            tiles[3].image = ImageIO.read(new File(String.format(path, "yellow")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        for (int i = 0; i < Maps.map1.length; i++){
            for (int j = 0; j < Maps.map1[i].length; j++){
                int tileNum = mapTileNum[j][i];
                g2.drawImage(tiles[tileNum].image,i * gp.tileSize, j * gp.tileSize, gp.tileSize, gp.tileSize,null);
            }
        }
    }

}