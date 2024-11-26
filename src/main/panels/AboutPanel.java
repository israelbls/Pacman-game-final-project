package main.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class AboutPanel extends JPanel implements KeyListener {
    private final GamePanel gamePanel;
    private final JFrame parentFrame;
    private MenuPanel menuPanel;
    private Image currentImage;
    private int currentImageIndex = 0;
    private final int totalImages = 11;
    private final String imagePath = "src/assets/images/presentation/";
    private final JLabel pageIndicator = new JLabel(String.format("%d/%d", currentImageIndex, totalImages));


    public AboutPanel(GamePanel gamePanel, JFrame parentFrame, MenuPanel menuPanel) {
        this.gamePanel = gamePanel;
        this.parentFrame = parentFrame;
        this.menuPanel = menuPanel;

        setPreferredSize(gamePanel.getPreferredSize());
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        // Create navigation panel with back button and page indicator
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(Color.BLACK);
        navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

        // Create back button
        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(Color.BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> returnToMenu());

        // Create page indicator label
        pageIndicator.setFont(new Font("Arial", Font.BOLD, 16));
        pageIndicator.setForeground(Color.WHITE);
        pageIndicator.setBackground(Color.BLUE);
        pageIndicator.setOpaque(true);
        pageIndicator.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add components to navigation panel
        navigationPanel.add(backButton);
        navigationPanel.add(pageIndicator);

        // Add navigation panel to the bottom of the panel
        add(navigationPanel, BorderLayout.SOUTH);

        // Load first image
        loadImage(currentImageIndex);

        // Add key listener
        setFocusable(true);
        addKeyListener(this);
    }


    private void loadImage(int index) {
        try {
            String imageName = String.format("about_p%d.png", index);
            File file = new File(imagePath + imageName);
            Image originalImage = ImageIO.read(file);

            // Scale image to fit the panel while maintaining aspect ratio
            currentImage = scaleImage(originalImage);

            // Update page indicator
            pageIndicator.setText(String.format("%d/%d", currentImageIndex, totalImages));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage());
        }
        repaint();
    }

    private Image scaleImage(Image originalImage) {
        // Get the original image dimensions
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // Get the available space (accounting for navigation panel)
        int availableWidth = gamePanel.windowWidth - 40; // 20px margin on each side
        int availableHeight = gamePanel.windowHeight - 100; // Space for navigation panel and margin

        // Calculate scaling factors
        double widthScale = (double) availableWidth / originalWidth;
        double heightScale = (double) availableHeight / originalHeight;

        // Use the smaller scaling factor to maintain aspect ratio
        double scale = Math.min(widthScale, heightScale);

        // Calculate new dimensions
        int newWidth = (int) (originalWidth * scale);
        int newHeight = (int) (originalHeight * scale);

        // Create and return scaled image
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            // Calculate position to center the image
            int imageWidth = currentImage.getWidth(null);
            int imageHeight = currentImage.getHeight(null);
            int x = (getWidth() - imageWidth) / 2;
            int y = (getHeight() - imageHeight) / 2;

            // Draw black background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw the scaled image
            g.drawImage(currentImage, x, y, null);
        }
    }

    private void returnToMenu() {
        parentFrame.getContentPane().removeAll();
        menuPanel = new MenuPanel(gamePanel,parentFrame);
        parentFrame.getContentPane().add(menuPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
        menuPanel.requestFocusInWindow();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (currentImageIndex > 1) {
                    currentImageIndex--;
                    loadImage(currentImageIndex);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (currentImageIndex < totalImages) {
                    currentImageIndex++;
                    loadImage(currentImageIndex);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}