package ux;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class runs background music.
 * // TODO author
 */
public class MusicPlayer {

    private long elapsedTime;   // For how long the song has been playing
    private long songTime;  // How long the song will play in total

    public void play() {
        /*
         * All possible songs; chosen at random. All of them come from opengameart.org except the soviet anthem,
         * but it is also public domain, so we can use it
         */
        String[] filePath = new String[]{"src/assets/cave themeb4.wav", // https://opengameart.org/content/cave-theme
                "src/assets/Soliloquy.wav", // https://opengameart.org/content/soliloquy
                "src/assets/song18.wav",    // https://opengameart.org/content/crystal-cave-song1
                // Little Easter egg: playing the soviet anthem instrumental
                "src/assets/Soviet_National_Anthem.wav",   // https://upload.wikimedia.org/wikipedia/commons/6/6d/Soviet_National_Anthem.ogg
                "src/assets/the_field_of_dreams.wav",   // https://opengameart.org/content/the-field-of-dreams
                "src/assets/мистичная тема.wav"}; // https://opengameart.org/content/mystical-theme
        int randomIndex = ThreadLocalRandom.current().nextInt(0, filePath.length);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath[randomIndex]).getAbsoluteFile());
            Clip song = AudioSystem.getClip();
            song.open(audioInputStream);
            song.start();
            elapsedTime = 0;
            songTime = song.getMicrosecondLength() / 1000000;   // We want it in seconds
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is regularly called by the main game ticker to check whether the current song has ended, If it has
     * a new random song is played.
     */
    public void checkForNewLoop() {
        elapsedTime += 3;
        // True if song ist over -> starting to play new random song
        if(elapsedTime >= songTime) {
            play();
        }
    }
}