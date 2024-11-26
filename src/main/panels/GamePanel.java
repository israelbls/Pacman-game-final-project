package main.panels;

import entitys.Player;
import entitys.ghosts.GhostsManager;
import main.*;
import objects.ObjectManager;
import tils.TileManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

/**
 * Main game panel that handles the game loop, rendering, and user input.
 * This panel contains the core game logic, manages game entities, and controls the game state.
 */
public class GamePanel extends JPanel implements Runnable {

    // Screen and window size constants
    public final int tileSize = 24;
    public final int maxScreenCol = 25;
    public final int maxScreenRow = 25;
    public int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int leftRightMargin = tileSize * 10;
    public final int topBottomMargin = tileSize * 3;
    public final int windowWidth = screenWidth + (leftRightMargin * 2);
    public final int windowHeight = screenHeight + (topBottomMargin * 2);

    // Game state variables
    public int frameCounter;
    private final int baseFrameRate = 60;
    public boolean running = false;
    public boolean isPaused = false;
    public int frameFerSeconds = baseFrameRate;
    public boolean inPlayBackMode = false;

    // Game components and managers
    private JFrame parentFrame;
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
    private final JLabel timeLabel;
    PlaybackSlider ps = new PlaybackSlider();

    /**
     * Constructs a new GamePanel and initializes all game components.
     * Sets up the UI elements, input handlers, and starts game recording.
     *
     * @throws IOException If there's an error loading game resources
     */
    public GamePanel() throws IOException {
        gameRecorder = new GameRecorder(this);
        
        // Initialize panel properties
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(Color.green);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyEH);
        this.setFocusable(true);
        this.setLayout(null);

        // Create and add UI components
        JButton colorButton = colorButton();
        this.add(colorButton);

        // Initialize and configure time display
        timeLabel = new JLabel("Time: 0:00");
        timeLabel.setBounds(windowWidth - 220, 20, 200, 70);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(MenuPanel.customFont().deriveFont(18f));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBorder(new LineBorder(Color.WHITE, 1));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.BLUE);
        this.add(timeLabel);

        // Set up mouse event handlers
        this.addMouseListener(new MouseAdapter() {});
        this.addMouseMotionListener(new MouseMotionAdapter() {});

        // Initialize game state
        frameCounter = 0;
        gameRecorder.startRecording();
    }

    /**
     * Adds a playback slider to the panel when in playback mode.
     */
    public void addPlaybackSlider() {
        ps.setBounds(leftRightMargin, windowHeight - topBottomMargin - tileSize, screenWidth, topBottomMargin);
        if (this.inPlayBackMode) this.add(ps);
    }

    /**
     * Creates and configures the color chooser button for wall colors.
     *
     * @return Configured JButton for color selection
     */
    private JButton colorButton() {
        JButton colorButton = new JButton("Choose Walls Color");
        colorButton.setFont(MenuPanel.customFont().deriveFont(9f));
        colorButton.setBounds(20, 20, 200, 70);
        colorButton.setBorder(new LineBorder(Color.WHITE, 1));
        colorButton.setFocusable(false);
        
        colorButton.addActionListener(_ -> {
            JColorChooser chooser = new JColorChooser(this.getBackground());
            
            // Configure color chooser to show only HSV panel
            AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
            for (AbstractColorChooserPanel panel : panels) {
                if (!panel.getDisplayName().equals("HSV")) {
                    chooser.removeChooserPanel(panel);
                }
            }

            JDialog dialog = JColorChooser.createDialog(this,
                "Choose a Background Color", true, chooser,
                e -> this.setBackground(chooser.getColor()), null
            );
            dialog.setVisible(true);
        });
        
        colorButton.setBackground(Color.blue);
        colorButton.setForeground(Color.white);
        colorButton.setOpaque(true);
        return colorButton;
    }

    /**
     * Sets the parent frame for this panel.
     *
     * @param frame The JFrame containing this panel
     */
    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
    }

    /**
     * Starts the game thread if it's not already running.
     * This method:
     * - Sets the running flag
     * - Creates and starts a new game thread
     * - Initializes game music
     * - Ensures keyboard focus is on the game panel
     */
    public void startGameThread() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
            playMusic(0);
            requestFocusInWindow();
        }
    }

    /**
     * Main game loop implementation.
     * Handles timing, updates, and rendering of the game.
     * This method:
     * - Maintains consistent game speed using delta timing
     * - Processes game updates when not paused
     * - Handles input regardless of pause state
     * - Manages frame rendering
     * The loop continues until the game thread is interrupted or an error occurs.
     */
    @Override
    public void run() {
        double drawInterval;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (running) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            drawInterval = 1000000000.0 / frameFerSeconds;
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                try {
                    if (!isPaused) {
                        checkInput();
                        updateGame();
                    } else {
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
        stopMusic();
        System.out.println("Game thread stopped");
    }

    /**
     * Updates game state, including time display and all game entities.
     * This method is called every frame when the game is not paused.
     * It updates the frame counter, game speed, time display, and all game entities.
     * If not in playback mode, it also records the current frame.
     *
     * @throws IOException If there's an error during game recording
     */
    private void updateGame() throws IOException {
        frameCounter++;
        frameFerSeconds = (int)(baseFrameRate * ps.pfs);
        
        // Update time display
        int seconds = frameCounter / 60;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        timeLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
        
        // Update game entities
        player.update();
        ghostsManager.update();
        objectManager.update();
        if (!inPlayBackMode) gameRecorder.recordFrame();
    }

    /**
     * Handles user input for game control (pause and menu navigation).
     * This method processes key events for:
     * - Pause/Resume game (using pause key)
     * - Return to menu (using escape key)
     * The method is called every frame regardless of pause state.
     *
     * @throws IOException If there's an error during menu transition
     */
    public void checkInput() throws IOException {
        if (keyEH.pausePressed) {
            isPaused = !isPaused;
            keyEH.pausePressed = false;
            System.out.println("Pause state: " + isPaused);
        }

        if (keyEH.escapePressed) {
            returnToMenu();
            keyEH.escapePressed = false;
        }
    }

    /**
     * Resets the game state to its initial configuration.
     * This includes:
     * - Resetting pause state
     * - Creating new input handler
     * - Reinitializing all game components (player, ghosts, objects, etc.)
     * - Restoring keyboard focus
     */
    public void reset() {
        isPaused = false;

        // Reset input handling
        keyEH = new KeyEventsHandler();
        this.removeKeyListener(this.getKeyListeners()[0]);
        this.addKeyListener(keyEH);

        // Reset game components
        try {
            player = new Player(this, keyEH);
            ghostsManager = new GhostsManager(this);
            objectManager = new ObjectManager(this);
            tileManager = new TileManager(this);
            infoDisplayed = new InfoDisplayed(this);
            collisionChecker = new CollisionChecker(this);
        } catch (IOException _) {
        }

        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Switches to the menu panel.
     *
     * @param frame The JFrame containing this panel
     * @throws IOException If there's an error during menu transition
     */
    private void switchToMenuPanel(JFrame frame) throws IOException {
        GamePanel gp = new GamePanel();
        MenuPanel menu = new MenuPanel(gp, frame);

        frame.setContentPane(menu);
        frame.revalidate();
        frame.repaint();
        menu.requestFocusInWindow();
    }

    /**
     * Returns to the menu panel and stops the game thread.
     *
     * @throws IOException If there's an error during menu transition
     */
    public void returnToMenu() throws IOException {
        if (!inPlayBackMode) gameRecorder.stopRecording();

        stopMusic();

        if (parentFrame != null) {
            running = false;
            gameThread.interrupt();
            switchToMenuPanel(parentFrame);
        } else {
            System.out.println("Parent frame is not set. Cannot switch to menu.");
        }
    }

    /**
     * Paints the game panel, including the background, game entities, and UI elements.
     *
     * @param g The Graphics object for painting
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        blackBackground(g2d);

        tileManager.draw(g2d);
        objectManager.draw(g2d);
        player.draw(g2d);
        ghostsManager.draw(g2d);
        infoDisplayed.draw(g2d);

        if (isPaused) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, windowWidth, windowHeight);

            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            String pauseText = "PAUSED";

            int textX = (windowWidth) / 2 - g2d.getFontMetrics().stringWidth(pauseText) / 2;
            int textY = (windowHeight) / 2;

            g2d.setColor(Color.BLACK);
            g2d.drawString(pauseText, textX - 2, textY - 2);
            g2d.drawString(pauseText, textX + 2, textY + 2);

            g2d.setColor(Color.YELLOW);
            g2d.drawString(pauseText, textX, textY);

            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String continueText = "Press SPACE to continue";
            String menuText = "Press ESC for menu";

            textX = (windowWidth) / 2 - g2d.getFontMetrics().stringWidth(continueText) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(continueText, textX - 1, textY + 50 - 1);
            g2d.setColor(Color.WHITE);
            g2d.drawString(continueText, textX, textY + 50);

            textX = (windowWidth) / 2 - g2d.getFontMetrics().stringWidth(menuText) / 2;
            g2d.setColor(Color.BLACK);
            g2d.drawString(menuText, textX - 1, textY + 90 - 1);
            g2d.setColor(Color.WHITE);
            g2d.drawString(menuText, textX, textY + 90);
        }

        paintChildren(g2d);

        g2d.dispose();
    }

    /**
     * Plays the game music.
     *
     * @param i The music index (not used)
     */
    public void playMusic(int i) {
        this.music.playMusic();
    }

    /**
     * Stops the game music and sound effects.
     */
    public void stopMusic() {
        music.stopMusic();
        se.stopMusic();
    }

    /**
     * Plays a sound effect.
     *
     * @param i The sound effect index (not used)
     */
    public void playSE(int i) {
        this.se.playSoundEffect(i);
    }

    /**
     * Draws the black background for the game panel.
     *
     * @param g2d The Graphics2D object for painting
     */
    private void blackBackground(Graphics2D g2d) {
        g2d.setColor(Color.black);

        g2d.fillRect(0, 0, leftRightMargin, windowHeight);

        g2d.fillRect(leftRightMargin, 0,
                windowWidth - leftRightMargin, topBottomMargin);

        g2d.fillRect(windowWidth - leftRightMargin, topBottomMargin,
                leftRightMargin, windowHeight - topBottomMargin);

        g2d.fillRect(leftRightMargin,windowHeight - topBottomMargin,
                screenWidth,topBottomMargin);
    }
}