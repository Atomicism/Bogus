package bogus.assets;

import javax.sound.sampled.*;
import java.io.*;

public class AudioAsset extends Asset {
    private Clip clip;

    public AudioAsset(String name, String location) {
        super(name, "assets/" + location);
    }

    @Override
    public void load() {
        try {
            // Try loading from filesystem
            File file = new File(location);
            if (file.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                logger.log("Loaded audio from filesystem: " + location);
                return;
            }

            // Try loading from JAR
            InputStream is = getClass().getResourceAsStream(location.startsWith("/") ? location : "/" + location);
            if (is == null) {
                System.err.println("Sound not found: " + location);
                return;
            }

            // Convert InputStream to AudioInputStream
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 AudioInputStream audioIn = AudioSystem.getAudioInputStream(bis)) {
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                System.out.println("Loaded audio from JAR: " + location);
            }

        } catch (Exception e) {
            System.err.println("Failed to load audio: " + location);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // rewind to the start
            clip.start();
        }
    }
}
