package main;

import entitys.Entity;
import main.panels.GamePanel;

import java.awt.Point;
import java.io.IOException;

public class CollisionChecker {
    GamePanel gp;
    int coins;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
        this.coins = gp.objectManager.coins.coins;
    }

    public void checkTile(Entity entity) {
        // Calculate the entity's coordinates in the game world
        int entityLeftWorldX = entity.entityX + entity.solidArea.x;
        int entityRightWorldX = entity.entityX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.entityY + entity.solidArea.y;
        int entityBottomWorldY = entity.entityY + entity.solidArea.y + entity.solidArea.height;

        // Check if the entity is within the game screen bounds
        if (entityLeftWorldX < 0 || entityRightWorldX + entity.speed > gp.screenWidth) {
            return;
        }

        // Calculate the tile indices for the entity's position
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        // Reset the collision state
        entity.collisionOn = false;

        // Detect collisions based on the entity's moving direction
        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 = gp.tileManager.mapTileNum[entityTopRow][entityRightCol];
                if (gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityBottomRow][entityLeftCol];
                tileNum2 = gp.tileManager.mapTileNum[entityBottomRow][entityRightCol];
                if (gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 = gp.tileManager.mapTileNum[entityBottomRow][entityLeftCol];
                if (gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityTopRow][entityRightCol];
                tileNum2 = gp.tileManager.mapTileNum[entityBottomRow][entityRightCol];
                if (gp.tileManager.tiles[tileNum1].collision || gp.tileManager.tiles[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public void checkObject(Entity entity, String[][] positions) throws IOException {
        int entityCol = entity.entityX / gp.tileSize;
        int entityRow = entity.entityY / gp.tileSize;

        // Check if the position is within bounds
        if (entityCol >= 0 && entityCol < gp.maxScreenCol &&
                entityRow >= 0 && entityRow < gp.maxScreenRow) {

            String tile = positions[entityRow][entityCol];
            if (tile != null) {
                // Pass row and col in the correct order
                handleObjectCollision(tile, entityRow, entityCol, positions);
            }
        }
    }

    public void handleObjectCollision(String objName, int row, int col, String[][] positions) throws IOException {
        switch (objName) {
            case "Coin":
                System.out.println("coin collected at row:" + row + " col:" + col);
                gp.playSE(1);
                positions[row][col] = null;
                gp.player.points += 10;
                coins--;
                System.out.println("coins:" + coins);
                if (coins == 0) {
                    gp.player.nextLevel();
                    this.coins = gp.objectManager.coins.coins;
                }
                break;
            case "Cherry":
                System.out.println("Cherry collected at row:" + row + " col:" + col);
                gp.playSE(1);
                positions[row][col] = null;
                gp.player.points += 100;
                gp.objectManager.fruit.deleteFruit(gp.objectManager.fruit.getFruitIndex("Cherry"));
                break;
            case "Strawberry":
                System.out.println("Strawberry collected at row:" + row + " col:" + col);
                gp.playSE(1);
                positions[row][col] = null;
                gp.player.points += 300;
                gp.objectManager.fruit.deleteFruit(gp.objectManager.fruit.getFruitIndex("Strawberry"));
                break;
            case "Apple":
                System.out.println("Apple collected at row:" + row + " col:" + col);
                gp.playSE(1);
                positions[row][col] = null;
                gp.player.points += 500;
                gp.objectManager.fruit.deleteFruit(gp.objectManager.fruit.getFruitIndex("Apple"));
                break;
            case "Lemon":
                System.out.println("Lemon collected at row:" + row + " col:" + col);
                gp.playSE(1);
                positions[row][col] = null;
                gp.player.points += 700;
                gp.objectManager.fruit.deleteFruit(gp.objectManager.fruit.getFruitIndex("Lemon"));
                break;
            case "Grapes":
                System.out.println("Grapes collected at row:" + row + " col:" + col);
                gp.playSE(1);
                positions[row][col] = null;
                gp.player.points += 1000;
                gp.objectManager.fruit.deleteFruit(gp.objectManager.fruit.getFruitIndex("Grapes"));
                break;
            case "PowerPellets":
                System.out.println("powerPellets collected at row:" + row + " col:" + col);
                positions[row][col] = null;
                gp.player.points += 100;
                gp.ghostsManager.frightened();
                break;
        }
    }

    public boolean isTileBlocked(int col, int row) {
        // Check bounds first
        if (row < 0 || col < 0 || row >= gp.maxScreenRow || col >= gp.maxScreenCol) {
            return true; // Consider out of bounds as blocked
        }

        // Get the tile number at the specified position
        int tileNum = gp.tileManager.mapTileNum[row][col];
        return gp.tileManager.tiles[tileNum].collision;
    }

    public boolean ghostHitsPlayer(Entity ghost) {
        int playerLeftX = gp.player.entityX;
        int playerRightX = gp.player.entityX + gp.player.solidArea.width;
        int playerTopY = gp.player.entityY;
        int playerBottomY = gp.player.entityY + gp.player.solidArea.height;

        int ghostLeftX = ghost.entityX;
        int ghostRightX = ghost.entityX + ghost.solidArea.width;
        int ghostTopY = ghost.entityY;
        int ghostBottomY = ghost.entityY + ghost.solidArea.height;

        return playerLeftX < ghostRightX && playerRightX > ghostLeftX
                && playerTopY < ghostBottomY && playerBottomY > ghostTopY;
    }

    public boolean ghostHitsHome(Entity ghost, Point home) {
        int ghostLeftX = ghost.entityX;
        int ghostRightX = ghost.entityX + ghost.solidArea.width;
        int ghostTopY = ghost.entityY;
        int ghostBottomY = ghost.entityY + ghost.solidArea.height;

        int homeLeftX = home.x;
        int homeRightX = home.x + gp.tileSize;
        int homeTopY = home.y;
        int homeBottomY = home.y + gp.tileSize;

        return homeLeftX < ghostRightX && homeRightX > ghostLeftX
                && homeTopY < ghostBottomY && homeBottomY > ghostTopY;
    }
}