package main;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class SavedGamesPanel extends JPanel {
    private final GamePanel gamePanel;
    private final JFrame parentFrame;
    private final JPanel buttonPanel;
    public static final String SAVES_DIRECTORY = "saved_games";

    public SavedGamesPanel(GamePanel gamePanel, JFrame parentFrame) {
        this.gamePanel = gamePanel;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Saved Games", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.setBackground(Color.RED);
        backButton.addActionListener(e -> returnToMenu());
        add(backButton, BorderLayout.SOUTH);

        loadSavedGames();
    }

    private void loadSavedGames() {
        buttonPanel.removeAll();
        File savesDir = new File(SAVES_DIRECTORY);
        if (!savesDir.exists()) {
            savesDir.mkdir();
        }

        File[] savedGames = savesDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (savedGames != null) {
            for (File game : savedGames) {
                JPanel gamePanel = createGamePanel(game);
                buttonPanel.add(gamePanel);
                buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createGamePanel(File game) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

        String gameName = game.getName().replace(".txt", "");
        JLabel nameLabel = new JLabel(gameName);
        nameLabel.setForeground(Color.WHITE);

        JButton playButton = new JButton("Play");
        playButton.setBackground(Color.GREEN);
        playButton.addActionListener(e -> loadGame(game));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(e -> deleteGame(game));

        panel.add(nameLabel);
        panel.add(playButton);
        panel.add(deleteButton);

        return panel;
    }

    private void loadGame(File game) {
        gamePanel.gameRecorder.loadRecording(game);
        gamePanel.inPlayBackMode = true;
        parentFrame.getContentPane().removeAll();
        parentFrame.setContentPane(gamePanel);
        parentFrame.revalidate();
        parentFrame.repaint();
        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();
    }

    private void deleteGame(File game) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this saved game?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (game.delete()) {
                loadSavedGames();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete the game.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void returnToMenu() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new MenuPanel(gamePanel, parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
