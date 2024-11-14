package main;

import entitys.Player;
import entitys.ghosts.GhostsManager;
import objects.ObjectManager;
import tils.TileManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    // Constants for screen size and resolution
    public final int tileSize = 32;
    public final int maxScreenCol = 19;
    public final int maxScreenRow = 19;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int leftRightMargin = tileSize * 8;
    public final int topBottomMargin = tileSize * 2;
    public final int windowWidth = screenWidth + (leftRightMargin * 2);
    public final int windowHeight = screenHeight + (topBottomMargin * 2);

    private boolean running = false;

    // Frame rate in frames per second
    final int frameFerSeconds = 60;

    // Game objects
    Thread gameThread;
    KeyEventsHandler keyEH = new KeyEventsHandler();
    SoundManager music = new SoundManager();
    SoundManager se = new SoundManager();
    public Player player = new Player(this, keyEH);
    public TileManager tileManager = new TileManager(this);
    ObjectManager objectManager = new ObjectManager(this);
    InfoDisplayed infoDisplayed = new InfoDisplayed(this);
    public GhostsManager ghostsManager = new GhostsManager(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);

    public GamePanel() throws IOException {
        // Set panel size and other properties
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyEH);
        this.setFocusable(true);
    }

    // Method to start the game thread
    public void startGameThread() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            playMusic(0);
        }
    }

    @Override
    public void run() {
        // Calculate the time needed between frames
        int billion = 1_000_000_000;
        double waitTime = (double) billion / frameFerSeconds;
        double nextDrawTime = System.nanoTime() + waitTime;

        while (gameThread != null) {
            update();
            repaint();

            try {
                // Wait for the appropriate time for the next frame
                double timeTillNextDraw = nextDrawTime - System.nanoTime();
                timeTillNextDraw = timeTillNextDraw / 1_000_000;
                if (timeTillNextDraw < 0) timeTillNextDraw = 0;
                Thread.sleep((long) timeTillNextDraw);
                nextDrawTime += waitTime;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        player.update();
        ghostsManager.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the background and game entities
        g2d.setColor(Color.BLUE);
        tileManager.draw(g2d);
        objectManager.draw(g2d);
        ghostsManager.draw(g2d);
        player.draw(g2d);
        infoDisplayed.displayText(g2d);

        g2d.dispose();
    }

    public void playMusic(int i) {
        this.music.playMusic();
    }

    public void stopMusic() {
        this.music.stopMusic();
    }

    public void playSE(int i) {
        this.se.playSoundEffect(i);
    }
}