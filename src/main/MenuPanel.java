package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private final GamePanel gamePanel;
    private final JFrame parentFrame;

    public MenuPanel(GamePanel gamePanel, JFrame parentFrame) {
        this.gamePanel = gamePanel;
        this.parentFrame = parentFrame;

        setPreferredSize(gamePanel.getPreferredSize());
        setBackground(Color.BLACK);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton startButton = createButton("Start Game");
        startButton.setBackground(Color.orange);

        JButton playbackButton = createButton("Saved Games");
        playbackButton.setBackground(Color.yellow);

        JButton instructionsButton = createButton("Instructions");
        instructionsButton.setBackground(Color.pink);

        JButton aboutButton = createButton("About");
        aboutButton.setBackground(Color.cyan);

        JButton exitButton = createButton("Exit");
        exitButton.setBackground(Color.red);

        startButton.addActionListener(e -> startGame());
        playbackButton.addActionListener(e -> showSavedGames());
        instructionsButton.addActionListener(e -> showInstructions());
        aboutButton.addActionListener(e -> showAbout());
        exitButton.addActionListener(e -> System.exit(0));

        add(startButton, gbc);
        add(playbackButton, gbc);
        add(instructionsButton, gbc);
        add(aboutButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(350, 70));
        button.setFont(customFont());
        button.setBackground(Color.BLUE);
        button.setForeground(Color.blue);
        return button;
    }

    private Font customFont() {
        Font customFont;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\User\\IdeaProjects\\Pacman game - final project\\src\\assets\\fonts\\ARCADE_I.TTF")).deriveFont(24f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        return customFont;
    }

    private void startGame() {
        // Remove all components first
        parentFrame.getContentPane().removeAll();

        // Set the game panel as the content
        parentFrame.setContentPane(gamePanel);

        // Start the game and refresh the frame
        gamePanel.startGameThread();
        parentFrame.revalidate();
        parentFrame.repaint();
        gamePanel.requestFocusInWindow();
    }

    private void showSavedGames() {
        parentFrame.getContentPane().removeAll();
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
        parentFrame.getContentPane().remove(this);
        AboutPanel aboutPanel = new AboutPanel(gamePanel, parentFrame, this);
        parentFrame.getContentPane().add(aboutPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
        aboutPanel.requestFocusInWindow();
    }
}