package main;

import entitys.Player;
import entitys.ghosts.Ghost;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {
    private GamePanel gp;
    private List<String> frames;
    private boolean isRecording;
    private static final String FRAME_SEPARATOR = "|";
    private final String EMPTY_TAB = "NA";
    private final String ROW_SEPARATOR = "#";
    private final String FRUIT_SEPARATOR = "$";
    private static String RECORDING_FILE = "game_recording.txt";
    public boolean recorded = false;
    private static final String SAVES_DIRECTORY = "saved_games";

    public GameRecorder(GamePanel gp) {
        this.gp = gp;
        this.frames = new ArrayList<>();
        this.isRecording = false;

        // Create saves directory if it doesn't exist
        File savesDir = new File(SAVES_DIRECTORY);
        if (!savesDir.exists()) {
            savesDir.mkdir();
        }
    }

    public void startRecording() {
        frames.clear();
        isRecording = true;
    }

    public void stopRecording() {
        isRecording = false;
        promptSaveGame();
    }

    public void recordFrame() {
        if (!isRecording) return;

        StringBuilder frame = new StringBuilder();

        // Record Player data
        Player player = gp.player;
        frame.append(player.direction);

        frame.append(FRAME_SEPARATOR);

        // Record Ghosts data
        for (Ghost ghost : gp.ghostsManager.ghosts) {
            if (ghost.state.equals("Frightened")) {
                frame.append(ghost.direction).append(FRAME_SEPARATOR);
            } else {
                frame.append(EMPTY_TAB).append(FRAME_SEPARATOR);
            }
        }

        // Record Fruits data
        for (String fruitType : gp.objectManager.fruit.fruitTypes) {
            String[][] positions = gp.objectManager.fruit.positions;
            List<int[]> positionsList = positions(positions, fruitType);
            if (positionsList.isEmpty()) {
                frame.append(EMPTY_TAB);
            } else {
                for (int[] position : positionsList) {
                    frame.append(position[0]).append(ROW_SEPARATOR)
                            .append(position[1]).append(FRUIT_SEPARATOR);
                }
            }
            frame.append(FRAME_SEPARATOR);
        }
        frame = new StringBuilder(frame.substring(0, frame.length() - 1));
        frames.add(frame.toString());
    }

    private List<int[]> positions(String[][] positions, String fruitType) {
        List<int[]> positionsList = new ArrayList<>();
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                if (positions[i][j] == null) continue;
                if (positions[i][j].equals(fruitType)) {
                    positionsList.add(new int[]{i, j});
                }
            }
        }
        return positionsList;
    }

    private void saveRecording(String gameName) {
        File saveFile = new File(SAVES_DIRECTORY + File.separator + gameName + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (String frame : frames) {
                writer.write(frame);
                writer.newLine();
                System.out.println("writing");
            }
            recorded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentFrame(int frameNumber) {
        if (frameNumber >= frames.size()){
            int choice = JOptionPane.showConfirmDialog(
                    gp, "Game ended do you want to watch again ?",
                    "Game ended", JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                gp.frameCounter = 0;
            }else {
                gp.returnToMenu();
                return null;
            }
        }
        return frames.get(frameNumber);
    }

    public void loadRecording(File recordingFile) {
        List<String> recordedFrames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(recordingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                recordedFrames.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        frames = recordedFrames;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void promptSaveGame() {
        int choice = JOptionPane.showConfirmDialog(
            gp,
            "Would you like to save the game?",
            "Save Game",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            String gameName = JOptionPane.showInputDialog(
                gp,
                "Enter a name for your saved game:",
                "Save Game",
                JOptionPane.PLAIN_MESSAGE
            );

            if (gameName != null && !gameName.trim().isEmpty()) {
                saveRecording(gameName);
            }
        }
    }
}
