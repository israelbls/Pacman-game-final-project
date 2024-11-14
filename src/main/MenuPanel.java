package main;

import javax.swing.*;
import java.awt.*;

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
        JButton instructionsButton = createButton("Instructions");
        JButton aboutButton = createButton("About");
        JButton exitButton = createButton("Exit");

        startButton.addActionListener(e -> startGame());
        instructionsButton.addActionListener(e -> showInstructions());
        aboutButton.addActionListener(e -> showAbout());
        exitButton.addActionListener(e -> System.exit(0));

        add(startButton, gbc);
        add(instructionsButton, gbc);
        add(aboutButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        return button;
    }

    private void startGame() {
        parentFrame.getContentPane().remove(this);
        parentFrame.getContentPane().add(gamePanel);
        gamePanel.startGameThread();
        parentFrame.revalidate();
        parentFrame.repaint();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
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