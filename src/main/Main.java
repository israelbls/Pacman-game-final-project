package main;

import main.panels.GamePanel;
import main.panels.MenuPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Main entry point for the Pacman game.
 * Sets up the game window, initializes the game and menu panels,
 * and configures basic window properties.
 */
public class Main {
    /**
     * Main method that starts the Pacman game.
     * Creates and configures the main game window, sets up the game
     * and menu panels, and displays the window to the user.
     *
     * @param args Command line arguments (not used)
     * @throws IOException If game resources cannot be loaded
     */
    public static void main(String[] args) throws IOException {
        // Create the JFrame window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Pacman Game");

        // Set the window icon
        try {
            Image image = ImageIO.read(new File("src/assets/images/Pacman_icon.png"));
            window.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the GamePanel and add it to the window
        GamePanel panel = new GamePanel();
        MenuPanel menu = new MenuPanel(panel, window);
        window.add(menu);
        window.pack();

        // Center the window on the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}