package main;

import entitys.Player;
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

    // Frame rate in frames per second
    final int frameFerSeconds = 60;

    // Game objects
    Thread gameThread;
    KeyEventsHandler keyEH = new KeyEventsHandler();
    public Player player = new Player(this, keyEH);
    public TileManager tileManager = new TileManager(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);

    public GamePanel() throws IOException {
        // Set panel size and other properties
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyEH);
        this.setFocusable(true);
    }

    // Method to start the game thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
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
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the background and game entities
        g2d.setColor(Color.BLUE);
        tileManager.draw(g2d);
        player.draw(g2d);

        g2d.dispose();
    }
}