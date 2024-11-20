package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventsHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean escapePressed, pausePressed;  // New keys for menu and pause

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) upPressed = true;
        if (keyCode == KeyEvent.VK_DOWN) downPressed = true;
        if (keyCode == KeyEvent.VK_LEFT) leftPressed = true;
        if (keyCode == KeyEvent.VK_RIGHT) rightPressed = true;
        if (keyCode == KeyEvent.VK_ESCAPE) escapePressed = true;  // ESC for menu
        if (keyCode == KeyEvent.VK_SPACE) {
            pausePressed = true;   // Space for pause
            System.out.println("Space key pressed");  // Debug output
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) upPressed = false;
        if (keyCode == KeyEvent.VK_DOWN) downPressed = false;
        if (keyCode == KeyEvent.VK_LEFT) leftPressed = false;
        if (keyCode == KeyEvent.VK_RIGHT) rightPressed = false;
        if (keyCode == KeyEvent.VK_ESCAPE) escapePressed = false;
        if (keyCode == KeyEvent.VK_SPACE) pausePressed = false;
    }
}