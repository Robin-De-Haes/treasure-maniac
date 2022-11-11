/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Chamber;
import domein.DomeinController;
import domein.GameProcesses;
import domein.Hero;
import domein.Monster;
import domein.Playlist;
import domein.Treasure;
import java.net.MalformedURLException;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main panel holding all the other panels of the game application.
 *
 * @author Robin
 */
public class MainPanel extends StackPane {

    private DomeinController dc;

    private HeroPanel heroPanel;
    private Frame frame;
    private Game game;
    private Controls controls;

    private HighscorePanel highscores;

    private Hero hero;
    private DetailHero detailHero;
    private DetailMonster detailMonster;
    private Playlist playlist;

    private boolean restarted;          //Houdt bij of game net heropgestart is

    private static Image logo = new Image(Main.class.getResourceAsStream("/images/icons/Logo_icon.png"));

    /**
     * Constructor for MainPanel.
     *
     * @param dc DomeinController used for communication with the database
     * @throws MalformedURLException if url to playlist is incorrect
     */
    public MainPanel(DomeinController dc) throws MalformedURLException {
        this.dc = dc;

        hero = null;
        detailHero = null;
        detailMonster = null;
        highscores=null;

        restarted = false;

        final HeroView player = new HeroView();
        controls = new Controls(this);
        game = new Game(player, controls, this);
        frame = new Frame(game, controls, this);

        heroPanel = new HeroPanel(new DetailHero(), this, dc);
        heroPanel.setAlignment(Pos.CENTER);

        Titlescreen titlescreen = new Titlescreen(this);

        getChildren().addAll(frame, heroPanel, titlescreen);
        game.setFocusTraversable(false);                //Er kan nog niet gespeeld worden zolang hero niet aangemaakt is
    }

    /**
     * Initialize the hero of the game and add him, combined with his
     * DetailHero-panel and some extra resources, to the game. An InventoryPanel
     * based on the hero's inventory will also added to the Frame of the game.
     *
     * @param hero the hero to be initialized and added
     * @author Robin
     */
    public void initializeHero(Hero hero) {
        this.hero = hero;

        Hero lastHero = dc.getLastHero();               //Laatste hero bekijken om zijn id te weten te komen en de nieuwe hero zijn id correct te initialiseren (nodig voor update-operaties)
        if (hero.getId() < 0) {
            if (dc.getLastHero() == null) {         //Als er geen hero voor hem was, moet je hem adden en removen om zijn id te kennen
                dc.addHero(hero);
                hero.setId(dc.getLastHero().getId());
                dc.deleteHero(hero);
                //Niet erg efficiënt, maar zal heel zelden voorvallen (normaal slechts hooguit eenmaal)
                //en zo moet rest van code niet aangepast worden
            } else {
                hero.setId(dc.getLastHero().getId() + 1);           //Het id van de nieuwe hero is 1 meer dan die van de laatste
            }
        }

        if (heroPanel.continueWasPossible() && lastHero.getId() != hero.getId()) {
            dc.removeAllTreasuresFromHero(dc.getLastHero());
            //Informatie over schatten van heroes die niet kunnen continuen zijn niet nodig
            //Als er niet gecontinued werd en een nieuw game startte kan er met die hero niet meer worden voortgedaan en heeft hij dus zijn schatten niet meer nodig
        }

        dc.removePreviousHeroes(hero);          //Doodt alle voorgaande heroes die nog konden continuen

        //DetailHero van hero aanmaken
        detailHero = new DetailHero(hero);
        detailHero.getStylesheets().add("/css/UI.css");
        detailHero.getStyleClass().add("playerName");

        //Game krijgt hero, detailhero en enkel handige, veel gebruikte resources toegevoegd
        game.addResources(hero, detailHero, frame, new GameProcesses(hero, dc));       //Noodzakelijke en/of veelgebruikte panelen in game
        //Met de inventory van de geïnitialiseerde hero wordt een inventorypanel aangemaakt en aan het frame toegevoegd
        frame.addInventory();
    }

    /**
     * Start a game with the specified hero.
     *
     * @param hero the hero to be played with
     * @author Robin
     */
    public void continueHero(Hero hero) {
        initializeHero(hero);           //Alle initialisatieacties die voor een nieuwe hero zou gebeuren

        //Zijn gevonden schatten toevoegen aan zijn inventory
        List<Treasure> treasures = dc.searchAllTreasuresFromHero(hero);         //Is leeg indien niets opgeraapt
        for (Treasure treasure : treasures) {
            this.hero.pickUpTreasure(treasure);             //De hero neemt de schatten op en voegt ze toe aan zijn inventory
        }

        game.updateDetailHero();            //DetailHero in het spel wordt op de hero zijn stats + zijn inventory stats gezet
        frame.updateInv();                  //Alle schatten in zijn inventory worden aan het inventorypanel toegevoegd

        //De avatar van de hero is zijn avatar zonder idle en zonder .gif
        setHeroView(hero.getAvatar().substring(0, (int) (hero.getAvatar().length() - 6 - 4)));

        //De kamer die hij laatst betreden heeft, moet opnieuw betreden worden (en dus eerst hermaakt worden)
        //Daarom 1 ronde verlagen zodat de hero de ronde na de beginkamer terug in zijn oude ronde zou zitten
        Chamber.setRound(hero.getRound() - 1);
    }

    /**
     * Change the player of the game to another class.
     *
     * @param className the class the player will be changed to
     * @author Robin
     */
    public void setHeroView(String className) {
        game.setClass(className);
    }

    /**
     * Perform the necessary actions for initializing a fight. Monster with his
     * details will be shown, and fight controlls will also be shown and
     * focused.
     *
     * @param monster
     */
    public void initializeFight(Monster monster) {
        game.initializeMonster(monster);         //detailmonster toevoegen
        //knopjes tonen en focussen voor gebruik
        frame.focusControls();
        controls.showButtons();
    }

    /**
     * Continue the fight by giving either the hero or the monster a turn to
     * perform an action.
     *
     * @param turn either 1 for the hero's turn or 2 for the monster's turn
     */
    public void continueFight(int turn) {
        game.fight(hero, getMonster(), turn);
    }

    /**
     * End the game with the demise of the hero. The hero will be added to the
     * highscores and the highscores will be shown.
     *
     * @throws MalformedURLException if url to playlist is incorrect
     */
    public void gameOver() throws MalformedURLException {
        dc.addHero(hero);
        showHighscores();         //endscreen oproepen
        dc.removeAllTreasuresFromHero(hero);
    }

    /**
     * End the game with the hero's victory. The hero will be added to the
     * highscores and the highscores will be shown.
     *
     * @throws MalformedURLException if url to playlist is incorrect
     */
    public void gameWon() throws MalformedURLException {
        //Zijn ronde omhoog doen zodat je kan bijhouden dat hij het spel heeft uitgespeeld (voor eventueel later in highscores nog te tonen)
        hero.setRound(GameProcesses.getMaxRound() + 1);
        dc.addHero(hero);
        showHighscores();
        dc.removeAllTreasuresFromHero(hero);
    }

    /**
     * Initialize and show a HighscorePanel with the latest highscores.
     *
     * @throws MalformedURLException if url to playlist is incorrect
     */
    public void showHighscores() throws MalformedURLException {
        highscores = new HighscorePanel(this);
        List<Hero> heroes = dc.searchAllHeroes();
        //Alle heroes toevoegen aan de highscores
        for (Hero h : heroes) {
            highscores.addHighscore(h);
        }

        //Alles mag verwijderd worden. Highscores zijn het enige overblijvende scherm.
        getChildren().clear();
        getChildren().addAll(highscores);

        //Highscores berekenen, sorteren en invullen voor de toegevoegde heroes
        highscores.updateScores();
    }

    /**
     * Change the bottom part of the Frame of the game. The floortiles of the
     * game will change.
     *
     * @param platformType the new kind of floortiles that will be used
     */
    public void changePlatform(String platformType) {
        controls.setPlatform(platformType);
    }

    /**
     * Bring the hero's displayed stats up-to-date. Will be used in the
     * InventoryPanel.
     */
    public void updateDetailHero() {
        game.updateDetailHero();
    }

    /**
     * Update the DetailGame shown in the game. Will update the score to the
     * given score. Will be called when dropping treasures from your inventory.
     */
    public void updateDetailGame() {
        game.updateDetailGame("" + hero.getScore());
    }

    /**
     * Show or hide the InventoryPanel. Based on whether it's already showing or
     * hiding, it will respectively hide or show it.
     */
    public void toggleInventory() {
        frame.toggleInventory();
    }

    /**
     * Perform the actions linked to pressing the attack-button. The
     * attackanimation and -logic will be started.
     */
    public void heroAttacks() {
        game.animateHeroAtk();
    }

    /**
     * Perform the actions linked to opening the chest in a gameround.
     */
    public void openChest() {
        game.openChest();
    }

    /**
     * Focus the game and enable movement control of the hero. Will be used
     * after creating a hero and closing certain panels.
     *
     * @author Robin
     */
    public void focusGame() {
        frame.focusGame();
    }

    /**
     * Give the current hero
     *
     * @return the current hero, will be null if hero hasn't been initialized
     * yet
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Give the current monster of the game.
     *
     * @return the current monster
     */
    public Monster getMonster() {
        return game.getMonster();
    }

    /**
     * Exit the game-application.
     */
    public void exitGame() {
        ((Stage) getScene().getWindow()).close();
        reset();               //Onclose-request werkt enkel voor externe aanvragen dus zal hier niet plaatsvinden
        playlist.disposePlaylist();
        System.exit(0);
    }

    /**
     * Perform the necessary actions for closing the previous game and starting
     * and new one. Animations and music of the old game will be stopped. The
     * old game's information will be reset and then the MainPanel will be
     * closed after which a new MainPanel with a new game will be opened.
     *
     * @throws MalformedURLException
     * @author Robin
     */
    public void restart() throws MalformedURLException {
        reset();               //Onclose-request werkt enkel voor externe aanvragen dus zal hier niet plaatsvinden
        //de animaties en muziek dienen hier dus zelf nog gestopt te worden

        final MainPanel main = new MainPanel(dc);       //New mainpanel voor het nieuwe spel

        //Zelfde doen als wanneer applicatie voor de eerste keer gelaunched werd
        Stage stage = (Stage) getScene().getWindow();           //Venster waarin het nieuwe MainPanel zal geplaatst worden (is zelfde venster als vroeger)
        Scene gameScene = new Scene(main);

        stage.setScene(gameScene);
        stage.setTitle("Treasure Maniac");
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.getIcons().add(logo);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {           //Wordt niet opgeroepen bij replayen
            @Override
            public void handle(WindowEvent t) {
                Hero hero = main.getHero();
                main.reset();                       //Chamber-round wordt ook reset
                main.getPlaylist().disposePlaylist();

                if (hero != null && !main.getFighting() && hero.getRound() > 0 && !main.getRestarted() && !(hero.finishedGame() || !hero.getAlive())) //Spel is reeds bezig en niet net opnieuw gestart
                {
                    if (main.getChestWasOpened()) {
                        hero.setRound(hero.getRound() + 1);        //Zodat je niet kan valsspelen door weg te gaan als schat je niet aantstaat
                    }
                    dc.addHero(main.getHero());     //Hero opslaan
                    dc.removeAllTreasuresFromHero(hero);            //Voor als er dropoperaties hebben plaatsgevonden

                    List<Treasure> treasures = main.getHero().getInventory().giveTreasures();  //Hier wordt niet geraakt
                    for (Treasure treasure : treasures) {
                        dc.addTreasureToHero(main.getHero(), treasure);
                        //Schatten hier opslaan
                    }
                }
            }
        });

        stage.close();
        stage.setWidth(1000);           //niet nodig, staat normaal nog goed
        stage.setHeight(720);
        stage.show();
        restarted = true;               //Het spel is net heropgestart, de laatste hero is dus reeds eens opgeslagen
    }

    /**
     * Stop the animations and music and reset the round to it's starting value.
     */
    public void reset() {
        Chamber.reset();             //Round terug op beginwaarde zetten
        game.stopAnimations();
        playlist.stopMusic();
        playlist.disposePlaylist();
        if(highscores!=null)
            highscores.disposePlaylist();
    }

    /**
     * Check if the game has just restarted
     *
     * @return true if the game has just restarted, false if not
     */
    public boolean getRestarted() {
        return restarted;
    }

    /**
     * Check if there's a fight going on.
     *
     * @return true if the hero's fighting, false if not
     */
    public boolean getFighting() {
        return game.getFighting();
    }

    /**
     * Check if the chest in the game was opened.
     *
     * @return true if the chest was opened, false if not
     */
    public boolean getChestWasOpened() {
        return game.getChestWasOpened();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
