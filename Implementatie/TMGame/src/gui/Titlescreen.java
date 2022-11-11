package gui;

import domein.Utility;
import domein.Playlist;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author pieterjan
 */
public class Titlescreen extends Pane {

    private Enum OS = null;
    private MainPanel main;
    private Pane pnlStart, pnlExit;
    private Playlist playlist;

    public Titlescreen(MainPanel main) throws MalformedURLException {
        OS = Utility.getOperatingSystem();
        this.main = main;
        initialize();
    }

    /**
     * initialize the titlescreen
     *
     * @throws MalformedURLException
     */
    private void initialize() throws MalformedURLException {
        playlist = new Playlist(Playlist.Type.TITLE);
        playlist.shufflePlaylist();
        playlist.playMusic();
        main.setPlaylist(playlist);
        
        minWidth(1000);
        minHeight(720);
        getStylesheets().add("css/UI.css");
        getStyleClass().add("titlescreen");

        pnlStart = new Pane();
        pnlStart.setMinSize(185, 40);
        pnlStart.setMaxSize(185, 40);

        pnlStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                playlist.stopMusic();
                try {
                    playlist.disposePlaylist();
                    playlist = new Playlist(Playlist.Type.BACKGROUND);
                    playlist.shufflePlaylist();
                    playlist.playMusic();
                    main.setPlaylist(playlist);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Titlescreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                main.getChildren().remove(Titlescreen.this);
            }
        });

        pnlExit = new Pane();
        pnlExit.setMinSize(185, 40);
        pnlExit.setMinSize(185, 40);

        pnlExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                main.exitGame();
            }
        });

        if (OS == Utility.OS.Macintosh) {
            heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight) {
                    double windowHeight = newHeight.doubleValue();
                    double iStart = windowHeight - 487;
                    double iExit = windowHeight - 536;
                    pnlStart.relocate(403, windowHeight - iStart);
                    pnlExit.relocate(403, windowHeight - iExit);
                }
            });
        } else if (OS == Utility.OS.Windows) {
            heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight) {
                    double windowHeight = newHeight.doubleValue();
                    double iStart = windowHeight - 484;
                    double iExit = windowHeight - 534;
                    pnlStart.relocate(400, windowHeight - iStart);
                    pnlExit.relocate(400, windowHeight - iExit);
                }
            });
        } else if (OS == Utility.OS.Unix) {
            heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight) {
                    double windowHeight = newHeight.doubleValue();
                    double iStart = windowHeight - 487;
                    double iExit = windowHeight - 536;
                    pnlStart.relocate(403, windowHeight - iStart);
                    pnlExit.relocate(403, windowHeight - iExit);
                }
            });
        } else {
            // OS niet ondersteund
            main.getChildren().remove(Titlescreen.this);
        }

        getChildren().addAll(pnlStart, pnlExit);
    }

}
