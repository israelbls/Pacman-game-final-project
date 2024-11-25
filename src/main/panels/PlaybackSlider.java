package main.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlaybackSlider extends JPanel {
    private final int sliderY = 50;
    private final int lineStart = 50, lineEnd = 550;
    private int sliderX = 200;
    public double pfs = 1.0;

    public PlaybackSlider() {
        this.setPreferredSize(new Dimension(1000, 700));
        this.setOpaque(false);  // Make panel transparent
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isInsideCircle(e.getX(), e.getY())) {
                    sliderX = e.getX();
                    updatePFS();
                    repaint();
                }
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getX() >= lineStart && e.getX() <= lineEnd) {
                    sliderX = e.getX();
                    updatePFS();
                    repaint();
                }
            }
        });
    }

    private boolean isInsideCircle(int x, int y) {
        int circleRadius = 10;
        return Math.sqrt(Math.pow(x - sliderX, 2) + Math.pow(y - sliderY, 2)) <= circleRadius;
    }

    private void updatePFS() {
        double percentage = (double) (sliderX - lineStart) / (lineEnd - lineStart);
        if (percentage <= 0.142) pfs = 0.8;
        else if (percentage <= 0.284) pfs = 1.0;
        else if (percentage <= 0.426) pfs = 1.2;
        else if (percentage <= 0.568) pfs = 1.5;
        else if (percentage <= 0.710) pfs = 2.0;
        else if (percentage <= 0.852) pfs = 4.0;
        else pfs = 8.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Set up anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw semi-transparent white line
        g2.setColor(new Color(255, 255, 255, 128));  // White with 50% transparency
        g2.setStroke(new BasicStroke(2.0f));  // Make line slightly thicker
        g2.drawLine(lineStart, sliderY, lineEnd, sliderY);


        String[] labels = {"x0.8","x1","x1.2", "x1.5", "x2", "x4", "x8"};
        g2.setColor(new Color(255, 255, 255, 180));  // White with 70% opacity for text
        for (int i = 0; i < labels.length; i++) {
            int x = lineStart + (i * (lineEnd - lineStart) / (labels.length - 1));
            g2.drawLine(x, sliderY - 10, x, sliderY + 10);
            g2.drawString(labels[i], x - 15, sliderY - 15);
        }

        // Draw slider circle with a semi-transparent blue fill
        g2.setColor(new Color(0, 0, 255, 180));  // Blue with 70% opacity
        g2.fillOval(sliderX - 10, sliderY - 10, 20, 20);
        g2.setColor(Color.WHITE);  // White border for contrast
        g2.drawOval(sliderX - 10, sliderY - 10, 20, 20);

        // Draw PFS value with semi-transparent white
        g2.setColor(new Color(255, 255, 255, 200));  // White with 80% opacity
        g2.drawString(pfs + "X", lineEnd, sliderY);
    }
}
