/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * 
 * @author pieterjan
 */
public class Playlist {

    private int currentSong;
    private File mp3;
    private Media soundtrack;
    private MediaPlayer player;
    private ArrayList<Media> playlist;
    private Boolean muted = false;
    private Timer timer;
    private TimerTask fadeOut;
    private double volume;

    /**
     * Enum type: decides which music will be played
     */
    public enum Type {
        BACKGROUND, TITLE, VICTORY, DEFEAT
    };

    /**
     * Creates the playlist.
     * @param type Decides the music to be played
     * @throws MalformedURLException When playlist couldn't be build
     */
    public Playlist(Type type) throws MalformedURLException {
        if (type == Type.BACKGROUND) {
            loadFiles(new File("src/sounds/background/"));
        } else if (type == Type.TITLE) {
            loadFiles(new File("src/sounds/titlescreen/"));
        } else if (type == Type.VICTORY) {
            loadFiles(new File("src/sounds/effects/victory"));
        } else if (type == Type.DEFEAT) {
            loadFiles(new File("src/sounds/effects/defeat"));
        }
        volume = 1;
       fadeOut = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (player.getVolume() == 0) {
                                        timer.cancel();
                                        timer.purge();
                                    } else {
                                        volume = volume - 0.75;
                                        player.setVolume(volume);
                                    }
                                    cancel();
                                }
                            });
                        }
                    };
        timer = new Timer();
    }

    /**
     * Load files in the given folder.
     * @param folder the folder that contains the files
     * @throws MalformedURLException When playlist couldn't be build
     */
    private void loadFiles(File folder) throws MalformedURLException {
        playlist = new ArrayList();
        currentSong = 0;
        File[] songs = folder.listFiles();
        for (File song : songs) {
            if (song.isFile()) {
                soundtrack = new Media(song.toURI().toURL().toString());
                playlist.add(soundtrack);
            }
        }
    }

    /**
     * Start to play the music.
     *
     * @throws MalformedURLException If playlist couldn't be build
     */
    public void playMusic() throws MalformedURLException {
        player = new MediaPlayer(playlist.get(currentSong));
        player.play();

        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if (currentSong <= playlist.size()) {
                    currentSong++;
                    player = new MediaPlayer(playlist.get(currentSong));
                    player.play();
                } else {
                    currentSong = 0;
                    if (muted) {
                        player.setMute(true);
                    }
                }
            }
        });
    }
    
    public void playSound() {
        player = new MediaPlayer(playlist.get(currentSong));
        player.play();
    }
    
    /**
     * Stop the music.
     */
    public void stopMusic() {
        player.stop();
    }

    /**
     * Dispose of the playlist.
     */
    public void disposePlaylist() {
        player.dispose();
        removeTimer();
    }

    /**
     * Shuffle the playlist.
     */
    public void shufflePlaylist() {
        long seed = System.nanoTime();
        Collections.shuffle(playlist);
    }

    /**
     * Toggle mute.
     * Calling this will toggle the sound, 
     * once will mute a second time will unmute
     */
    public void toggleMute() {
        if (!player.muteProperty().getValue()) {
            player.setMute(true);
            muted = true;
        } else {
            player.setMute(false);
            muted = false;
        }
    }
    
    /**
     * Let the music fade out.
     */
    public void fadeOut() {
        timer.schedule(fadeOut, 0, 1000);
    }

    /**
     * Remove the timer and its tasks.
     * Should be called before playlist becomes unreachable.
     */
    public void removeTimer()
    {
       fadeOut.cancel();
        timer.cancel();
        timer.purge();
    }
}
