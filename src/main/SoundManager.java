package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundManager {
    private Clip musicClip;
    private Clip[] seClips;
    private URL[] soundURL = new URL[30];

    public SoundManager() {
        this.soundURL[0] = this.getClass().getResource("/assets/sound/Pac-man.wav");
        this.soundURL[1] = this.getClass().getResource("/assets/sound/coin.wav");

        try {
            // Load the main music
            AudioInputStream ais = AudioSystem.getAudioInputStream(this.soundURL[0]);
            this.musicClip = AudioSystem.getClip();
            this.musicClip.open(ais);

            // Initialize the sound effect clips
            this.seClips = new Clip[this.soundURL.length - 1];
            for (int i = 1; i < this.soundURL.length; i++) {
                if (soundURL[i] != null) {
                    ais = AudioSystem.getAudioInputStream(this.soundURL[i]);
                    this.seClips[i - 1] = AudioSystem.getClip();
                    this.seClips[i - 1].open(ais);
                }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusic() {
        // Set the music volume to a lower level
        FloatControl gainControl = (FloatControl) this.musicClip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f); // Adjust the volume as needed

        this.musicClip.start();
        this.musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopMusic() {
        this.musicClip.stop();
    }

    public void playSoundEffect(int index) {
        if (!this.seClips[index - 1].isRunning()) {
            // Play the sound effect at full volume
            this.seClips[index - 1].setFramePosition(0);
            this.seClips[index - 1].start();
        }
    }
}