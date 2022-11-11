/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Hero;
import domein.Playlist;
import domein.Utility;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/**
 * Panel for showing the ten heroes who have the highest score.
 *
 * @author Robin, Pieter-Jan
 */
public class HighscorePanel extends Pane {

    private Pane container;

    private FlowPane highscores;
    private Pane highscore;
    private Label position;
    private Label name;
    private Label score;

    private Pane buttons;
    private Button replay, quit;

    private List<Hero> scoreList;

    private MainPanel main;
    private Enum OS;
    private Playlist playlist;
    
    /**
     * Constructor for a HighscorePanel
     *
     * @param main the panel who owns the highscorepanel. (his parent)
     */
    public HighscorePanel(MainPanel main) throws MalformedURLException {
        this.main = main;
       /* Playlist mainPlaylist = main.getPlaylist();
        mainPlaylist.disposePlaylist();*/
        playlist = new Playlist(Playlist.Type.TITLE);
        playlist.playMusic();
        
        OS = Utility.getOperatingSystem();
        getStylesheets().add("css/UI.css");
        getStyleClass().add("scorescreen");

        scoreList = new ArrayList();
        highscores = new FlowPane(Orientation.VERTICAL, 0, 2);

        initializeContainer();
        initializeButtons();

        getChildren().addAll(container, buttons);
    }

    /**
     * Initializes the container for the HighSocrePanel.
     */
    private void initializeContainer() {
        container = new Pane();
        container.getStyleClass().add("transparent");
        container.getChildren().add(highscores);

        if (OS == Utility.OS.Windows) {
            container.relocate(230, 243);
            container.setMinSize(528, 400);
            container.setMaxSize(528, 400);
        } else if (OS == Utility.OS.Macintosh) {
            container.relocate(233.5, 246);
            container.setMinSize(528, 400);
            container.setMaxSize(528, 400);
        }
    }

    /**
     * Initializes the buttons and builds the pane holding them.
     *
     * @author Robin, Pieter-Jan
     */
    private void initializeButtons() {
        buttons = new Pane();
        replay = new Button();
        quit = new Button();

        replay.setMinSize(145, 30);
        replay.setMaxSize(145, 30);
        quit.setMinSize(145, 30);
        quit.setMaxSize(145, 30);

        buttons.setMinSize(305, 30);
        buttons.setMaxSize(305, 30);

        replay.relocate(0, 0);
        quit.relocate(145 + 15, 0);

        if (OS == Utility.OS.Windows) {
            buttons.relocate(335, 145);
        } else if (OS == Utility.OS.Macintosh) {
            buttons.relocate(346, 162);
        }

        buttons.getChildren().addAll(replay, quit);
        buttons.setOpacity(0);

        replay.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                try {
                    main.restart();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(HighscorePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        quit.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                main.exitGame();
            }
        });

    }

    /**
     * Adds one hero to the list of highscores. This will be used for the hero
     * that just played the game.
     *
     * @param hero the hero who will be aded to the list of highscores
     * @author Robin
     */
    public void addHighscore(Hero hero) {

        if (hero.getAlive() && !hero.finishedGame()) //Als hero gewoon opgeslagen is om te continuen
        {
            return;
        }
        boolean added = false;
        for (int i = 0; i < scoreList.size(); i++) {
            if (hero.getScore() >= scoreList.get(i).getScore()) {
                scoreList.add(i, hero);                                 //De hoogste scores komen eerst
                added = true;                                           //Laatst toegevoegde score komt eerst
                break;
            }
        }
        if (!added) {
            scoreList.add(hero);
        }
    }

    /**
     * Builds and adds highscore-panes for the 10 highest scores.
     *
     * @author Robin
     */
    public void updateScores() {
        int counter = 1;
        for (Hero hero : scoreList) {
            if (counter <= 10) {
                highscore = buildScore(hero, counter);
                highscores.getChildren().addAll(highscore);
                counter++;
            }
        }
    }

    /**
     * Builds one highscore-pane for one hero on a specified position.
     *
     * @param hero the hero whose highscore-pane will be build
     * @param index the position the hero has in the list of highscores
     * @return the highscore-pane that has been built
     */
    private Pane buildScore(Hero hero, int index) {
        highscore = new Pane();
        highscore.getStyleClass().add("scorescreen-item");
        highscore.setMinSize(528, 30);
        highscore.setMaxSize(528, 30);

        name = new Label(hero.getName());
        score = new Label("" + hero.getScore());
        position = new Label("" + index);

        name.getStyleClass().add("scorescreen-font");
        score.getStyleClass().add("scorescreen-font");
        position.getStyleClass().add("scorescreen-font");

        name.setMaxHeight(30);
        name.setMinHeight(30);
        score.setMaxHeight(30);
        score.setMinHeight(30);
        position.setMaxHeight(30);
        position.setMinHeight(30);

        position.relocate(25, 0);
        name.relocate(65, 0);
        score.relocate(435, 0);

        highscore.getChildren().addAll(position, name, score);
        return highscore;
    }
    
    /**
     * Dispose the current playlist.
     */
    public void disposePlaylist()
    {
        playlist.disposePlaylist();
    }
}
