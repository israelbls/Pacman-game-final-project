package main;

import entitys.Entity;
import objects.ObjectManager;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
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

    public void checkObject(Entity entity) {
        int entityCol = entity.entityX / gp.tileSize;
        int entityRow = entity.entityY / gp.tileSize;

        // Check if the position is within bounds
        if (entityCol >= 0 && entityCol < gp.maxScreenCol &&
                entityRow >= 0 && entityRow < gp.maxScreenRow) {

            String tile = ObjectManager.positions[entityRow][entityCol];
            if (tile != null) {
                // Pass row and col in the correct order
                handleObjectCollision(tile, entityRow, entityCol);
            }
        }
    }

    public void handleObjectCollision(String objName, int row, int col) {
        switch (objName) {
            case "Coin":
                System.out.println("coin collected at row:" + row + " col:" + col);
                gp.playSE(1);
                ObjectManager.positions[row][col] = null;
                gp.player.points += 10;
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
}