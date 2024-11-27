package main;

import entitys.Player;
import entitys.ghosts.Ghost;
import main.panels.GamePanel;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Records and manages game state for playback functionality.
 * This class handles recording game frames, saving recordings to files,
 * and loading recorded games for playback.
 */
public class GameRecorder {
    private final GamePanel gp;
    private List<String> frames;
    private boolean isRecording;
    
    // Constants for data formatting
    private static final String FRAME_SEPARATOR = "|";
    private final String EMPTY_TAB = "NA";
    private final String ROW_SEPARATOR = "#";
    private final String FRUIT_SEPARATOR = "$";
    private final String LOCATION_SEPARATOR = "@";
    public boolean recorded = false;
    private static final String SAVES_DIRECTORY = "saved_games";

    /**
     * Constructs a new GameRecorder.
     * 
     * @param gp The GamePanel instance to record
     */
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

    /**
     * Starts recording game frames.
     * Clears any previously recorded frames and begins recording new ones.
     */
    public void startRecording() {
        frames.clear();
        isRecording = true;
    }

    /**
     * Stops recording and prompts the user to save the recording.
     */
    public void stopRecording() {
        isRecording = false;
        promptSaveGame();
    }

    /**
     * Records the current game state as a frame.
     * Each frame includes player position, ghost states, and fruit positions.
     */
    public void recordFrame() {
        if (!isRecording) return;

        StringBuilder frame = new StringBuilder();

        // Record Player data
        Player player = gp.player;
        frame.append(player.direction)
             .append(LOCATION_SEPARATOR)
             .append(player.entityX)
             .append(ROW_SEPARATOR)
             .append(player.entityY)
             .append(FRAME_SEPARATOR);

        // Record Ghosts data
        for (Ghost ghost : gp.ghostsManager.ghosts) {
            if (ghost.state.equals("Frightened")) {
                frame.append(ghost.direction)
                     .append(LOCATION_SEPARATOR)
                     .append(ghost.entityX)
                     .append(ROW_SEPARATOR)
                     .append(ghost.entityY)
                     .append(FRAME_SEPARATOR);
            } else {
                frame.append(EMPTY_TAB)
                     .append(FRAME_SEPARATOR);
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
                    frame.append(position[0])
                         .append(ROW_SEPARATOR)
                         .append(position[1])
                         .append(FRUIT_SEPARATOR);
                }
            }
            frame.append(FRAME_SEPARATOR);
        }
        
        frame = new StringBuilder(frame.substring(0, frame.length() - 1));
        frames.add(frame.toString());
    }

    /**
     * Gets all positions of a specific fruit type from the positions array.
     * 
     * @param positions The array containing fruit positions
     * @param fruitType The type of fruit to find positions for
     * @return List of position coordinates [row, col] for the specified fruit type
     */
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

    /**
     * Saves the recorded frames to a file.
     * 
     * @param gameName Name of the save file
     */
    private void saveRecording(String gameName) {
        File saveFile = new File(SAVES_DIRECTORY + File.separator + gameName + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (String frame : frames) {
                writer.write(frame);
                writer.newLine();
            }
            recorded = true;
        } catch (IOException _) {
            // Handle silently
        }
    }

    /**
     * Gets a specific frame from the recording.
     * Handles game replay when reaching the end of recorded frames.
     * 
     * @param frameNumber The frame number to retrieve
     * @return The frame data as a string
     * @throws IOException If there's an error during menu transition
     */
    public String getCurrentFrame(int frameNumber) throws IOException {
        if (frameNumber >= frames.size() -1) {
            int choice = JOptionPane.showConfirmDialog(
                    gp, "Game ended do you want to watch again ?",
                    "Game ended", JOptionPane.YES_NO_OPTION
            );
            if (choice != JOptionPane.YES_OPTION) {
                gp.returnToMenu();
            }
            gp.frameCounter = 0;
            frameNumber = 0;
            gp.reset();
        }
        return frames.get(frameNumber);
    }

    /**
     * Loads a recorded game from a file.
     * 
     * @param recordingFile The file containing the recorded game
     */
    public void loadRecording(File recordingFile) {
        List<String> recordedFrames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(recordingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                recordedFrames.add(line);
            }
        } catch (IOException _) {
            // Handle silently
        }
        frames = recordedFrames;
    }

    /**
     * Prompts the user to save the current game recording.
     * Allows the user to enter a name for the save file.
     */
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
