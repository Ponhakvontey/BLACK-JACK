package application_audio;

import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class AudioManager {

    private static MediaPlayer backgroundMusicPlayer;

    private static boolean musicEnabled = true;
    private static double musicVolume = 0.5; // 50% default
    private static double sfxVolume = 0.5;   // 50% default

    private static final String BACKGROUND_MUSIC_PATH = "audio/videoplayback.mp3";

    // Call once when app starts
    public static void initializeAudio() {
        try {
            // Avoid reinitializing if already loaded
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.setVolume(musicVolume);
                if (musicEnabled) {
                    backgroundMusicPlayer.play();
                } else {
                    backgroundMusicPlayer.pause();
                }
                return;
            }

            System.out.println("Looking for audio file at: " + BACKGROUND_MUSIC_PATH);

            File audioFile = new File(BACKGROUND_MUSIC_PATH);

            if (audioFile.exists()) {
                System.out.println("Audio file found! Loading...");

                Media sound = new Media(audioFile.toURI().toString());
                backgroundMusicPlayer = new MediaPlayer(sound);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop forever
                backgroundMusicPlayer.setVolume(musicVolume);

                if (musicEnabled) {
                    backgroundMusicPlayer.play();
                    System.out.println("Background music started playing (looping)");
                    System.out.println("Initial volume: " + (int) (musicVolume * 100) + "%");
                }
            } else {
                System.out.println("ERROR: Audio file not found at: " + BACKGROUND_MUSIC_PATH);
                System.out.println("Current path: " + audioFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Error initializing audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;

        if (backgroundMusicPlayer != null) {
            if (enabled) {
                backgroundMusicPlayer.play();
                System.out.println("Music enabled - playing");
            } else {
                backgroundMusicPlayer.pause();
                System.out.println("Music disabled - paused");
            }
        }
    }

    public static void setMusicVolume(double volume) {
        musicVolume = volume;

        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(volume);
            System.out.println("Music volume set to: " + (int) (volume * 100) + "%");
        }
    }

    public static void setSfxVolume(double volume) {
        sfxVolume = volume;
        System.out.println("SFX volume set to: " + (int) (volume * 100) + "%");
    }

    public static void playSfx(String fileName) {
        try {
            if (sfxVolume <= 0) return;

            File f = new File("audio/" + fileName);
            if (!f.exists()) {
                System.out.println("SFX not found: " + f.getAbsolutePath());
                return;
            }

            Media media = new Media(f.toURI().toString());
            MediaPlayer sfx = new MediaPlayer(media);
            sfx.setVolume(sfxVolume);

            sfx.setOnEndOfMedia(() -> {
                sfx.stop();
                sfx.dispose();
            });

            sfx.setOnError(() -> {
                System.out.println("SFX error: " + sfx.getError());
                sfx.dispose();
            });

            sfx.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetToDefaults() {
        musicEnabled = true;
        musicVolume = 0.5;
        sfxVolume = 0.5;

        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(musicVolume);
            backgroundMusicPlayer.play();
        }

        System.out.println("Reset to defaults: Music=50%, SFX=50%");
    }

    public static void shutdown() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
            System.out.println("Audio system shut down");
        }
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static double getSfxVolume() {
        return sfxVolume;
    }
    private static Parent previousRoot;

    public static void setPreviousRoot(Parent root) {
        previousRoot = root;
    }

    public static Parent getPreviousRoot() {
        return previousRoot;
    }

}
