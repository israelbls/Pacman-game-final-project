package main.panels;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;

public class MenuPanel extends JPanel {
    private final GamePanel gamePanel;
    private final JFrame parentFrame;
    private Image backgroundImage;

    public MenuPanel(GamePanel gamePanel, JFrame parentFrame) {
        this.gamePanel = gamePanel;
        this.parentFrame = parentFrame;

        // Load the background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/assets/images/menu background.png"));
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        setPreferredSize(gamePanel.getPreferredSize());
        setBackground(Color.BLACK); // Fallback color if image fails to load

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weighty = 0;
        gbc.insets = new Insets(100, 0, 0, 0);
        add(Box.createVerticalStrut(1), gbc);

        gbc.insets = new Insets(10, 0, 10, 0);

        JButton startButton = createButton("Start Game");
        startButton.setBackground(Color.orange);

        JButton highScoresButton = createButton("High Scores");
        highScoresButton.setBackground(Color.magenta);

        JButton playbackButton = createButton("Saved Games");
        playbackButton.setBackground(Color.yellow);

        JButton instructionsButton = createButton("Instructions");
        instructionsButton.setBackground(Color.pink);

        JButton aboutButton = createButton("About");
        aboutButton.setBackground(Color.cyan);

        JButton exitButton = createButton("Exit");
        exitButton.setBackground(Color.red);

        startButton.addActionListener(_ -> startGame());
        highScoresButton.addActionListener(_ -> showHighScores());
        playbackButton.addActionListener(_ -> showSavedGames());
        instructionsButton.addActionListener(_ -> showInstructions());
        aboutButton.addActionListener(_ -> showAbout());
        exitButton.addActionListener(_ -> System.exit(0));

        add(startButton, gbc);
        add(highScoresButton, gbc);
        add(playbackButton, gbc);
        add(instructionsButton, gbc);
        add(aboutButton, gbc);
        add(exitButton, gbc);

        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(350, 70));
        button.setFont(customFont());
        button.setBorder(new LineBorder(Color.white,5));
        button.setBackground(Color.BLUE);
        button.setForeground(Color.blue);
        return button;
    }

    public static Font customFont() {
        Font customFont;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(MenuPanel.class.getResourceAsStream("/assets/fonts/ARCADE_I.TTF"))).deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        return customFont;
    }

    private void startGame() {
        parentFrame.getContentPane().removeAll();
        parentFrame.setContentPane(gamePanel);
        gamePanel.setParentFrame(parentFrame);
        gamePanel.startGameThread();
        parentFrame.revalidate();
        parentFrame.repaint();
        gamePanel.requestFocusInWindow();
    }

    private void showHighScores() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new HighScoresPanel(gamePanel, parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void showSavedGames() {
        parentFrame.getContentPane().removeAll();
        gamePanel.setParentFrame(parentFrame);
        parentFrame.add(new SavedGamesPanel(gamePanel, parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(this, """
                Instructions:
                - Use arrow keys to move Pac-Man.
                - Eat all dots to advance.
                - Avoid the ghosts or eat them after a big dot!
                """);
    }

    private void showAbout() {
        parentFrame.getContentPane().removeAll();
        AboutPanel aboutPanel = new AboutPanel(gamePanel, parentFrame, this);
        parentFrame.getContentPane().add(aboutPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
        aboutPanel.requestFocusInWindow();
    }
}