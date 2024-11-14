package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create the JFrame window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Pacman Game");

        // Set the window icon
        try {
            Image image = ImageIO.read(new File("C:/Users/User/IdeaProjects/Pacman game - final project/src/assets/images/Pacman_icon.png"));
            window.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the GamePanel and add it to the window
        GamePanel panel = new GamePanel();
        MenuPanel menu = new MenuPanel(panel,window);
        window.add(menu);
        window.pack();

        // Center the window on the screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}