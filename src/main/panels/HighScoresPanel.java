package main.panels;

import main.HighScoreEntry;
import main.HighScoreManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for displaying high scores in the game.
 * This panel shows a table of high scores with player information including
 * rank, name, score, date, time, and game duration.
 */
public class HighScoresPanel extends JPanel {
    private final HighScoreManager highScoreManager;
    private final JTable table;
    private final JButton backButton;
    private final GamePanel gamePanel;
    private final JFrame parentFrame;
    private static final String[] COLUMN_NAMES = {
            "Rank", "Player Name", "Score", "Date", "Time", "Duration"
    };

    /**
     * Constructs a new HighScoresPanel.
     * 
     * @param gamePanel The main game panel
     * @param parentFrame The parent frame containing this panel
     */
    public HighScoresPanel(GamePanel gamePanel, JFrame parentFrame) {
        this.gamePanel = gamePanel;
        this.parentFrame = parentFrame;
        highScoreManager = new HighScoreManager();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.BLACK);

        // Create and configure the title label
        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        add(titleLabel, BorderLayout.NORTH);

        // Create table with non-editable cells
        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        customizeTable();

        // Configure the scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Create and configure the back button
        backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(Color.YELLOW);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new MenuPanel(gamePanel, parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        // Configure the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load initial high scores
        updateHighScores();
    }

    /**
     * Customizes the appearance of the high scores table.
     * Sets colors, fonts, cell alignment, and column widths.
     */
    private void customizeTable() {
        table.setBackground(Color.BLACK);
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(5, 5));

        // Center align all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Configure column widths for optimal display
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // Rank
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Player Name
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Score
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Date
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Time
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Duration
    }

    /**
     * Updates the high scores table with the latest data.
     * Retrieves high scores from the HighScoreManager and populates the table.
     */
    public void updateHighScores() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        List<HighScoreEntry> highScores = highScoreManager.getHighScores();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (int i = 0; i < highScores.size(); i++) {
            HighScoreEntry entry = highScores.get(i);
            model.addRow(new Object[]{
                    i + 1, // Rank
                    entry.getPlayerName(),
                    entry.getScore(),
                    entry.getDateTime().format(dateFormatter),
                    entry.getDateTime().format(timeFormatter),
                    formatDuration(entry.getGameDuration())
            });
        }
    }

    /**
     * Formats the game duration in minutes and seconds.
     * 
     * @param seconds The game duration in seconds
     * @return The formatted game duration as a string
     */
    private String formatDuration(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
}
