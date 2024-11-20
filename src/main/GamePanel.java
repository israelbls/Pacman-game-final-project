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
    public int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int leftRightMargin = tileSize * 8;
    public final int topBottomMargin = tileSize * 2;
    public final int windowWidth = screenWidth + (leftRightMargin * 2);
    public final int windowHeight = screenHeight + (topBottomMargin * 2);

    public int frameCounter;

    public boolean running = false;
    public boolean isPaused = false;  // Track pause state

    // Frame rate in frames per second
    final int frameFerSeconds = 60;

    public boolean inPlayBackMode = false;

    // Game objects
    public Thread gameThread;
    public KeyEventsHandler keyEH = new KeyEventsHandler();
    SoundManager music = new SoundManager();
    SoundManager se = new SoundManager();
    public Player player = new Player(this, keyEH);
    public TileManager tileManager = new TileManager(this);
    public ObjectManager objectManager = new ObjectManager(this);
    InfoDisplayed infoDisplayed = new InfoDisplayed(this);
    public GhostsManager ghostsManager = new GhostsManager(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public GameRecorder gameRecorder;

    public GamePanel() throws IOException {
        gameRecorder  = new GameRecorder(this);
        // Set panel size and other properties
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyEH);
        this.setFocusable(true);

        frameCounter = 0;
        gameRecorder.startRecording();
    }

    // Method to start the game thread
    public void startGameThread() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            playMusic(0);
            requestFocusInWindow();  // Request focus when game starts
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / frameFerSeconds;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (running) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                try {
                    if (!isPaused) {
                        checkInput();
                        updateGame();
                    } else {
                        if (!gameRecorder.recorded)gameRecorder.stopRecording();
                        checkInput();
                    }
                    repaint();
                } catch (IOException _) {
                    running = false;
                    break;
                }
                delta--;
            }
        }
        // Clean up when thread stops
        stopMusic();
        System.out.println("Game thread stopped");
    }

    private void updateGame() throws IOException {
        frameCounter++;
        player.update();
        ghostsManager.update();
        objectManager.update();
        if (!inPlayBackMode) gameRecorder.recordFrame();
    }

    public void checkInput() {
        // Check for pause toggle
        if (keyEH.pausePressed) {
            isPaused = !isPaused;
            keyEH.pausePressed = false;
            System.out.println("Pause state: " + isPaused);
        }

        // Check for return to menu
        if (keyEH.escapePressed) {
            returnToMenu();
            keyEH.escapePressed = false;
        }
    }

    public void reset() {
        running = false;
        isPaused = false;
        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join(1000);
            } catch (InterruptedException ignored) {}
            gameThread = null;
        }

        // Reset key handler
        keyEH = new KeyEventsHandler();
        this.removeKeyListener(this.getKeyListeners()[0]);
        this.addKeyListener(keyEH);

        // Reset game objects
        try {
            player = new Player(this, keyEH);
            ghostsManager = new GhostsManager(this);
            objectManager = new ObjectManager(this);
            tileManager = new TileManager(this);
            infoDisplayed = new InfoDisplayed(this);
            collisionChecker = new CollisionChecker(this);
        } catch (IOException _) {
        }

        // Reset focus
        setFocusable(true);
        requestFocusInWindow();
    }

    private void stopGameThread() {
        running = false;
        stopMusic();

        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join(1000);
            } catch (InterruptedException ignored) {}
            gameThread = null;
        }
    }

    private void switchToMenuPanel(JFrame frame) throws IOException {
        // Create new GamePanel and MenuPanel
        GamePanel gp = new GamePanel();
        MenuPanel menu = new MenuPanel(gp, frame);

        // Set the menu panel as the content
        frame.setContentPane(menu);

        // Refresh the frame
        frame.revalidate();
        frame.repaint();
        menu.requestFocusInWindow();
    }

    public void returnToMenu() {
        if (!inPlayBackMode)gameRecorder.stopRecording();
        // Stop game thread and reset state
        stopGameThread();
        reset();

        // Switch to menu on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof JFrame frame) {
                    switchToMenuPanel(frame);
                }
            } catch (IOException _) {
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        tileManager.draw(g2d);
        objectManager.draw(g2d);
        player.draw(g2d);
        ghostsManager.draw(g2d);
        infoDisplayed.draw(g2d);

        if (isPaused) {
            // Enable antialiasing for smoother text
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Draw semi-transparent overlay
            g2d.setColor(new Color(0, 0, 0, 180));  // More opaque background
            g2d.fillRect(0, 0, windowWidth, windowHeight);
            
            // Draw pause menu text with outline for better visibility
            g2d.setFont(new Font("Arial", Font.BOLD, 48));  // Larger font
            String pauseText = "PAUSED";
            
            // Calculate center position including margins
            int textX = (windowWidth) / 2 - g2d.getFontMetrics().stringWidth(pauseText) / 2;
            int textY = (windowHeight) / 2;
            
            // Draw text outline
            g2d.setColor(Color.BLACK);
            g2d.drawString(pauseText, textX-2, textY-2);
            g2d.drawString(pauseText, textX+2, textY+2);
            
            // Draw main text
            g2d.setColor(Color.YELLOW);  // More visible color
            g2d.drawString(pauseText, textX, textY);
            
            // Draw instructions with outline
            g2d.setFont(new Font("Arial", Font.BOLD, 24));  // Larger instruction text
            String continueText = "Press SPACE to continue";
            String menuText = "Press ESC for menu";
            
            // Draw continue text
            textX = (windowWidth) / 2 - g2d.getFontMetrics().stringWidth(continueText) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(continueText, textX-1, textY + 50-1);
            g2d.setColor(Color.WHITE);
            g2d.drawString(continueText, textX, textY + 50);
            
            // Draw menu text
            textX = (windowWidth) / 2 - g2d.getFontMetrics().stringWidth(menuText) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(menuText, textX-1, textY + 90-1);
            g2d.setColor(Color.WHITE);
            g2d.drawString(menuText, textX, textY + 90);
        }

        g2d.dispose();
    }

    public void playMusic(int i) {
        this.music.playMusic();
    }

    public void stopMusic() {
        music.stopMusic();
        se.stopMusic();
    }

    public void playSE(int i) {
        this.se.playSoundEffect(i);
    }
}