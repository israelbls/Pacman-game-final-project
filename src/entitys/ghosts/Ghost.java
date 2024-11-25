package entitys.ghosts;

import entitys.Entity;
import main.panels.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class Ghost extends Entity {
    public GamePanel gp;
    protected int modeTimer = 600;
    public int timeCounter = 0;
    int frightenedPos = 1;
    protected String ghostName;
    public BufferedImage eatenUp;
    BufferedImage eatenDown;
    BufferedImage eatenLeft;
    BufferedImage eatenRight;
    public BufferedImage frighten1;
    BufferedImage frighten2;

    boolean inHome;

    protected Point target;
    public Point scatterModeTarget;
    protected Point eatenModeTarget;

    public String state;

    public Ghost(GamePanel gp, String ghostName) {
        this.gp = gp;
        this.ghostName = ghostName;

        solidArea = new Rectangle();
        solidArea.x = 2;
        solidArea.y = 2;
        solidArea.height = 28;
        solidArea.width = 28;

        eatenModeTarget = new Point(12 * gp.tileSize, 9 * gp.tileSize);
        inHome = true;

        setDefaultValues();
        getGhostImage();
    }

    protected abstract void setDefaultValues();

    protected abstract Point getChaseModeTarget();

    public void resetPosition() {
        setDefaultValues();
        state = "Scatter";  // Reset to default state
        timeCounter = 0;    // Reset timer
        modeTimer = 600;    // Reset mode timer
    }

    public void getGhostImage() {
        try {
            up1 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/up1.png"));
            up2 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/up2.png"));
            down1 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/down1.png"));
            down2 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/down2.png"));
            right1 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/right1.png"));
            right2 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/right2.png"));
            left1 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/left1.png"));
            left2 = ImageIO.read(new File("src/assets/images/ghosts/" + ghostName + "/left2.png"));

            eatenUp = ImageIO.read(new File("src/assets/images/ghosts/eatan/up.png"));
            eatenDown = ImageIO.read(new File("src/assets/images/ghosts/eatan/down.png"));
            eatenLeft = ImageIO.read(new File("src/assets/images/ghosts/eatan/left.png"));
            eatenRight = ImageIO.read(new File("src/assets/images/ghosts/eatan/right.png"));

            frighten1 = ImageIO.read(new File("src/assets/images/ghosts/frightened/f1.png"));
            frighten2 = ImageIO.read(new File("src/assets/images/ghosts/frightened/f2.png"));
        } catch (IOException _) {
            System.out.println("error loading ghost image");
        }
    }


    public void update() throws IOException {
        modeControl();

        // First get the new direction based on current state and target
        String newDirection = getDirection();

        // Only update direction if the new direction is valid
        if (newDirection != null && !newDirection.isEmpty()) {
            direction = newDirection;
        }

        if (!isPlayback() || !state.equals("Frightened")) {
            switch (direction) {
                case "up":
                    entityY -= speed;
                    break;
                case "down":
                    entityY += speed;
                    break;
                case "left":
                    entityX -= speed;
                    // Handle screen wrapping for left side
                    if (entityX + solidArea.x < 0) {
                        entityX = gp.screenWidth - gp.tileSize;
                    }
                    break;
                case "right":
                    entityX += speed;
                    // Handle screen wrapping for right side
                    if (entityX + gp.tileSize > gp.screenWidth) {
                        entityX = 0;
                    }
                    break;
            }
        }else {
            int[] position = locationInPlaybackMode();
            entityX = position[0];
            entityY = position[1];
        }

        // Animation frame counter
        frameCounter++;
        if (frameCounter % 5 == 0) {
            positionNumber = positionNumber == 1 ? 2 : 1;
        } else if (frameCounter > 30) {
            frightenedPos = frightenedPos == 1 ? 2 : 1;
            frameCounter = 0;
        }
    }

    public void enterMode(String mode) {
        state = mode;
        direction = getOppositeDirection(direction);
    }

    public Point getTarget() {
        if (inHome)return inHomeTarget(ghostName);
        return switch (state) {
            case "Scatter" -> scatterModeTarget;
            case "Chase" -> getChaseModeTarget();
            case "Eaten" -> eatenModeTarget;
            case "Frightened" -> target;
            default -> throw new IllegalStateException("Unexpected value: " + state);
        };
    }

    protected String getDirection() throws IOException {
        if (!isSnappedToGrid()) return direction;
        Random random = new Random();
        String[] availableDirections = getAvailableDirections(direction);
        if (availableDirections.length == 0) return getOppositeDirection(direction);
        else if (availableDirections.length < 2) return availableDirections[0];

        target = getTarget();
        return switch (state) {
            case "Scatter", "Chase", "Eaten" -> bestDirection(target, availableDirections);
            case "Frightened" -> isPlayback() ? directionInPlaybackMode()
                    : availableDirections[random.nextInt(availableDirections.length)];
            default -> "";
        };
    }

    private String ghostTab() throws IOException {
        String frame = gp.gameRecorder.getCurrentFrame(gp.frameCounter);
        return switch (ghostName) {
            case "blinky" -> frame.split("\\|")[1];
            case "pinky" -> frame.split("\\|")[2];
            case "inky" -> frame.split("\\|")[3];
            case "clyde" -> frame.split("\\|")[4];
            default -> null;
        };
    }

    private String directionInPlaybackMode() throws IOException {
        return Objects.requireNonNull(ghostTab()).split("@")[0];
    }

    private int[] locationInPlaybackMode() throws IOException {
        String location = Objects.requireNonNull(ghostTab()).split("@")[1];
        return new int[]{Integer.parseInt(location.split("#")[0]), Integer.parseInt(location.split("#")[1])};
    }

    public boolean isSnappedToGrid() {
        if (Objects.equals(direction, "NA")) {
            return false;
        }
        return switch (direction) {
            case "up", "down" -> entityY % gp.tileSize == 0;
            case "left", "right" -> entityX % gp.tileSize == 0;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    protected String bestDirection(Point target, String[] availableDirections) {
        double minDistance = Double.MAX_VALUE;
        String bestDirection = availableDirections[0];
        for (String direction : availableDirections) {
            Point dir = getPoint(direction);
            double distance = dir.distance(target);
            if (distance < minDistance) {
                minDistance = distance;
                bestDirection = direction;
            }
        }
        return bestDirection;
    }

    private Point getPoint(String direction) {
        return switch (direction) {
            case "up" -> new Point(entityX, entityY - speed);
            case "down" -> new Point(entityX, entityY + speed);
            case "left" -> new Point(entityX - speed, entityY);
            case "right" -> new Point(entityX + speed, entityY);
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    private Point inHomeTarget(String ghostName){
        System.out.println("in home - "+inHome + " " + ghostName);
        return switch (ghostName){
            case "blinky", "inky" -> new Point(14 * gp.tileSize, 8 * gp.tileSize);
            case "pinky", "clyde" -> new Point(10 * gp.tileSize, 8 * gp.tileSize);
            default -> throw new IllegalStateException("Unexpected value: " + ghostName);
        };
    }

    protected String[] getAvailableDirections(String direction) {
        List<String> available = new ArrayList<>();
        int col = entityX / gp.tileSize;
        int row = entityY / gp.tileSize;

        if (!gp.collisionChecker.isTileBlocked(col, row - 1) && !"down".equals(direction)) available.add("up");
        if (!gp.collisionChecker.isTileBlocked(col - 1, row) && !"right".equals(direction)) available.add("left");
        if (!gp.collisionChecker.isTileBlocked(col, row + 1) && !"up".equals(direction)) available.add("down");
        if (!gp.collisionChecker.isTileBlocked(col + 1, row) && !"left".equals(direction)) available.add("right");

        return available.toArray(new String[0]);
    }

    protected String getOppositeDirection(String dir) {
        return switch (dir) {
            case "up" -> "down";
            case "down" -> "up";
            case "left" -> "right";
            case "right" -> "left";
            default -> "";
        };
    }

    public void modeControl() {
        timeCounter++;
        if (timeCounter > modeTimer) {
            timeCounter = 0;
            switch (state) {
                case "Eaten" -> {break;}
                case "Frightened" -> enterMode("Scatter");
                case "Scatter" -> enterMode("Chase");
                default -> enterMode("Scatter");
            }
        }

        if (gp.collisionChecker.ghostHitsPlayer(this)) {
            System.out.println("ghost hits: " + ghostName);
            switch (state) {
                case "Scatter", "Chase":
                    if (!gp.player.dead) gp.player.life--;
                    gp.player.dead = true;
                    gp.ghostsManager.resetAllGhosts();// Reset all ghosts, not just this one
                    if (gp.player.life == 0) gp.player.gameOver = true;
                    break;
                case "Frightened":
                    gp.player.points += 200;
                    enterMode("Eaten");
                case "Eaten":
                    break;
            }
        }

        if (inHome && gp.collisionChecker.ghostHitsHome(this, inHomeTarget(ghostName))) {
            inHome = false;
        }
        if (state.equals("Eaten") && gp.collisionChecker.ghostHitsHome(this, eatenModeTarget)) {
            inHome = true;
            enterMode("Scatter");
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image;
        image = switch (state) {

            case "Scatter", "Chase" -> switch (direction) {
                case "up" -> positionNumber == 1 ? up1 : up2;
                case "down" -> positionNumber == 1 ? down1 : down2;
                case "left" -> positionNumber == 1 ? left1 : left2;
                case "right" -> positionNumber == 1 ? right1 : right2;
                default -> null;
            };

            case "Frightened" -> frightenedPos == 1 ? frighten1 : frighten2;

            case "Eaten" -> switch (direction) {
                case "up" -> eatenUp;
                case "down" -> eatenDown;
                case "left" -> eatenLeft;
                case "right" -> eatenRight;
                default -> null;
            };

            default -> null;
        };

        int screenX = entityX + gp.leftRightMargin;
        int screenY = entityY + gp.topBottomMargin;
        g2d.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    private boolean isPlayback() {
        return gp.inPlayBackMode;
    }
}