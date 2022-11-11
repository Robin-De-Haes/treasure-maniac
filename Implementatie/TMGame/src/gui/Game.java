/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Chamber;
import domein.GameProcesses;
import domein.Hero;
import domein.Monster;
import domein.Playlist;
import domein.Utility;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Actual game panel with the necessary logic.
 *
 * @author Robin De Haes
 */
public class Game extends Pane {

    //Noodzakelijke of veel gebruikte andere klassen
    private MainPanel main;
    private HeroView player;
    private Controls controls;

    private GameProcesses gp;
    private Frame frame;
    private Hero hero;

    //panels containg the details
    private DetailHero detailHero;
    private DetailGame detailGame;
    private DetailMonster detailMonster;

    private Chamber chamber;
    private ChestPanel chestPanel;

    private boolean enableMovement, fighting, chestCanBeOpened, chestWasOpened;       //Hero can move, hero is fighting, chest can be opened, chest was opened
    private static final double STEP = 10.0;           //Hoeveel de hero per keer verplaatst
    private double heroFightPt, monsterFightPt, fightPoint;
        //Waar de hero staat bij initiatie gevecht, waar monster staat en waar het werkelijke gevecht zal plaatsvinden (bij de hero of het monster)

    //Vertragingen tussen screenovergangen
    private Timer timer;
    private TimerTask delay, wonTask, deadTask;

    //Animties
    private TranslateTransition hTranslationTo, mTranslationTo, hTranslationFrom, mTranslationFrom, translation, monsterMoveTo;
    private Timeline cloud;
    private FadeTransition ft, gameStatusTransition;

    //Gebruikt image/imageview-elementen in het paneel
    private static ImageView ivMonster = new ImageView();

    private static final Image closedChest = new Image(Main.class.getResourceAsStream("/images/treasure/ChestClosed.gif"));
    private static Image openChest = new Image(Main.class.getResourceAsStream("/images/treasure/ChestOpen.gif"));
    private static ImageView ivTreasure = new ImageView(closedChest);

    private static Image imgVictory = new Image(Main.class.getResourceAsStream("/images/UI/Victory.png"));
    private static Image imgDefeat = new Image(Main.class.getResourceAsStream("/images/UI/Defeat.png"));
    private static ImageView ivGameStatus = new ImageView();

    private static Image imgCloud = new Image(Main.class.getResourceAsStream("/images/icons/FightCloud.gif"));
    private static ImageView ivCloud = new ImageView(imgCloud);

    private static final Image deadHero = new Image(Main.class.getResourceAsStream("/images/heroes/Grave.gif"));

    private Playlist playlist;
    /**
     * Constructor for the game-panel
     *
     * @param player the heroview to be added in the game, represents the hero
     * @param controls the Controls-panel positioned below the gamepanel and
     * used in-game for performing the hero's actions
     * @param main the owner of the framepanel to which the game belongs. Used
     * as medium to communicate with other panels.
     */
    public Game(HeroView player, Controls controls, MainPanel main) {
        this.player = player;
        this.main = main;
        this.controls = controls;

        this.requestFocus();
        initialize();
        movement();

        hero = null;
        detailMonster = null;
        gp = null;

        timer = new Timer();
        delay = null;
        deadTask = null;
        wonTask = null;

        hTranslationTo = null;
        mTranslationTo = null;
        hTranslationFrom = null;
        mTranslationFrom = null;
        translation = null;
        monsterMoveTo = null;
        cloud = null;
        ft = null;
        gameStatusTransition = null;
        playlist=null;
    }

    /**
     * Settings for the game. Adding the necessary layout and positioning to
     * elements.
     */
    private void initialize() {
        getStylesheets().add("css/gamebackground.css");
        //Uittesten met monster
        //Een lege kamer met uitleg om in te beginnen
        initializeDetailGame();
        initializeChamber(new Chamber());           //Empty room
        
        // add player to game
        getChildren().addAll(player);

        //Vaste settings voor een treasure
        ivTreasure.setFitWidth(100);
        ivTreasure.setFitHeight(90);

        //Gevechtswolk (onzichtbaar) toevoegen
        getChildren().addAll(ivCloud);
        ivCloud.setLayoutY(getHeight() - ivCloud.getLayoutBounds().getHeight());
        ivCloud.setLayoutX(0);
        ivCloud.setOpacity(0);

        //Settings voor gameStatus
        ivGameStatus.setFitWidth(600);
        ivGameStatus.setFitHeight(300);
        ivGameStatus.setOpacity(0);

        
        //Correcte triggerpoints berekenen op basis van de grootte van de gekozen hero
        calcTriggerPoints();

        //Dynamisch berekenen van correcte posities op basis van de hoogte en breedte van het game-panel
        heightProperty().addListener(new ChangeListener<Number>() {           //hoogte player aanpassen

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldHeight, Number newHeight) {
                double heightHero = getHeroHeight();
                player.setLayoutY(newHeight.doubleValue() - heightHero);

                double heightTreasure = ivTreasure.getLayoutBounds().getHeight();
                ivTreasure.setLayoutY(newHeight.doubleValue() - heightTreasure);

                double heightMonster = getMonsterHeight();
                ivMonster.setLayoutY(newHeight.doubleValue() - heightMonster);

                double heightCloud = ivCloud.getLayoutBounds().getHeight();
                ivCloud.setLayoutY(newHeight.doubleValue() - heightCloud);

                double heightGameStatus = ivGameStatus.getLayoutBounds().getHeight();
                ivGameStatus.setLayoutY((newHeight.doubleValue() - heightGameStatus) / 2);
            }
        }
        );

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) {

                double widthTreasure = ivTreasure.getLayoutBounds().getWidth();
                ivTreasure.setLayoutX(newWidth.doubleValue() - widthTreasure);

                ivMonster.setLayoutX(newWidth.doubleValue());

                detailGame.setLayoutX(newWidth.doubleValue() / 2.0 - detailGame.getWidth() / 2.0);

                double widthGameStatus = ivGameStatus.getLayoutBounds().getWidth();
                ivGameStatus.setLayoutX((newWidth.doubleValue() - widthGameStatus) / 2);
            }
        }
        );

        //Game zal in de tab-cyclus gestoken worden en bovendien de focus krijgen na toevoegen/verwijderen element
        setFocusTraversable(true);
    }

    /**
     * Initializes a chamber or round in the game
     *
     * @author Robin
     * @param chamber chamber that holds the information for the logic and for
     * initializing the graphics of a round in the game
     */
    private void initializeChamber(Chamber chamber) {
        if (hero != null) {                             //Als het spel al daadwerkelijk begonnen is (en er dus een hero is aangemaakt en toegevoegd)
            hero.setRound(Chamber.getRound());
            frame.getChildren().removeAll(chestPanel);
            updateDetailGame("" + hero.getScore());
            detailHero.setOpacity(1);
        }

        getChildren().removeAll(ivTreasure, ivMonster);      //Verwijderd indien ze er nog inzitten van vorige ronde, anders gebeurt er niets
        removeMonsterDetail();          //Verwijderd monsterdetail indien het geïnitialiseerd en toegevoegd werd
        if (ivTreasure.getImage().equals(openChest)) {  //Kist sluiten indien nog open
            ivTreasure.setImage(closedChest);
        }

        this.chamber = chamber;
        this.getStyleClass().clear();               //Vorige achtergrond verwijderen
        this.getStyleClass().add(chamber.getBackground());          //Achtergrond en bodem initialiseren van de chamber
        main.changePlatform(chamber.getPlatformType());

        //Nieuwe ronde, dus chest is niet geopend en kan nog niet geopend worden.
        chestWasOpened = false;
        chestCanBeOpened = false;
        enableMovement = true;

        //Een treasurechest toevoegen indien nodig
        if (chamber.hasChest()) {
            //treasure toevoegen
            getChildren().addAll(ivTreasure);
        }

        //Een monster toevoegen indien nodig
        if (chamber.hasMonster()) {
            //Correcte avatar van monster gebruiken
            String avatar = chamber.getMonster().getAvatar();
            setMonsterImage(avatar);
            getChildren().addAll(ivMonster);
            moveMonsterToStart();           //monster naar zijn startpositie verplaatsen
        }
    }

    /**
     * Move the hero back to his starting position.
     *
     * @author Robin
     */
    private void moveHeroToStart() {
        player.setTranslateX(0);
        player.setHeroIdleRight();
    }

    /**
     * Move the monster back to his starting position.
     */
    private void moveMonsterToStart() {
        ivMonster.setTranslateX(0);
    }

    /**
     * Change the player of the game to another classimage.
     *
     * @param className class the player will be set to
     * @author Robin
     */
    public void setClass(String className) {
        player.setView(className);
        //Niet elke hero is even groot, dus hem herpositioneren zodat hij juist op de grond staat
        recalcHeroPos();
    }

    /**
     * Change the image used to represent the monster.
     *
     * @param avatar the avatar that will be used to represent the monster
     * @author Robin
     */
    public void setMonsterImage(String avatar) {
        ivMonster.setImage(new Image(Main.class.getResourceAsStream("/images/monster/" + avatar)));
        recalcMonsterPos();
    }

    /**
     * Recalculate the hero's position. Should be called everytime the
     * player-image changes its size.
     *
     * @author Robin
     */
    private void recalcHeroPos() {
        //Zet de hero op de grond
        player.setLayoutY(getHeight() - getHeroHeight());
        //Bereken nieuwe triggerpoints op basis van de nieuwe hero-size
        calcTriggerPoints();
    }

    /**
     * Recalculate the monster's position. Should be called everytime the
     * monster-image changes its size.
     *
     * @author Robin
     */
    private void recalcMonsterPos() {
        ivMonster.setLayoutY(getHeight() - getMonsterHeight());
        calcTriggerPoints();
    }

    /**
     * Calculate the correct triggerpoint positions for the hero and monster.
     * Should be called everytime one of them changes their size.
     */
    private void calcTriggerPoints() {
        heroFightPt = getWidth() / 2.0; //Hero staat net voorbij helft van het scherm
        monsterFightPt = getWidth() - ivTreasure.getFitWidth() - getMonsterWidth();  //Monster staat voor kist
    }

    /**
     * Animate the hero's movement and correctly position him after moving.
     *
     * @author Pieter-Jan en Robin
     */
    private void movement() {
        setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                double x = player.getTranslateX();      //Van zijn oude positie voortbewegen
                if (enableMovement) {
                    //Naar rechts lopen
                    if (t.getCode() == KeyCode.RIGHT) {
                        //Bewegen afhankelijk van snelheid hero
                        x = x + STEP + (hero.getTotalSpeed() * STEP / Hero.getMAX());
                        player.setHeroMoveRight();
                        player.setTranslateX(x);
                    }
                    //Naar links lopen
                    if (t.getCode() == KeyCode.LEFT) {
                        player.setHeroMoveLeft();
                        if (x > 0.0) {                      //Zodat hero niet buiten scherm kan lopen
                            x = x - STEP - (hero.getTotalSpeed() * STEP / Hero.getMAX());
                            player.setTranslateX(x);
                        }
                    }
                    triggers();         //Kijken of de hero's nieuwe positie een speciale gebeurtenis triggert
                }

            }
        });

        //Als hij stilstaat kan hij wel nog naar links en rechts bewegen, net als wanneer hij stopt met lopen
        setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.RIGHT) {
                    player.setHeroIdleRight();
                }
                if (t.getCode() == KeyCode.LEFT) {
                    player.setHeroIdleLeft();
                }
            }
        });
    }

    /**
     * Calculate if the hero moved to a special position and perform the
     * necessary actions if he did.
     *
     * @author Robin en Pieter-Jan
     */
    private void triggers() {
        if (chamber.hasMonster()) {
            //Hero komt op het punt om met het monster te vechten
            if (player.getTranslateX() >= heroFightPt) {
                enableMovement = false;         //Hero staat stil
                AnimateMonsterEntering();       //Monster komt in scherm gelopen

                monsterMoveTo.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        //Gebruik de idle-versie indien het monster een idleversie heeft
                        setIdle(chamber.getMonster());

                        //Even wachten zodat monsterIdle te zien is
                        final TimerTask delay = new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        initializeFight();
                                        cancel();
                                    }
                                });
                            }
                        };
                        timer.schedule(delay, 500);
                    }
                }
                );
                monsterMoveTo.play();
            }
        } else {
            if (chamber.hasChest()) {
                //Hero staat vlak bij de chest
                double stop = getWidth() - getHeroWidth() - ivTreasure.getFitWidth();
                //De hero kan de chest openen als hij dichtgenoeg is
                if (!chestCanBeOpened && player.getTranslateX() >= stop) {
                    controls.showOpen();
                    chestCanBeOpened = true;
                } else if (chestCanBeOpened && player.getTranslateX() < stop) {
                    //De hero kan de chest niet meer openen als hij te ver is
                    controls.hideOpen();
                    chestCanBeOpened = false;
                }
            }
            //De hero is uit het scherm gelopen
            if (player.getTranslateX() >= getWidth()) {
                controls.hideOpen();

                //Als het de laatste ronde is
                if (gp.isMaxRound()) {
                    enableMovement = false;

                    //Victory op het scherm laten verschijnen + vertraging + naar highscorescreen gaan
                    ivGameStatus.setImage(imgVictory);
                    getChildren().add(ivGameStatus);

                    gameStatusTransition = new FadeTransition(Duration.millis(1000), ivGameStatus);
                    gameStatusTransition.setFromValue(0);
                    gameStatusTransition.setToValue(1);

                    ivGameStatus.getLayoutBounds().getHeight();
                    ivGameStatus.getLayoutBounds().getWidth();

                    wonTask = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    fighting = false;
                                    try {
                                        playlist.disposePlaylist();
                                        playlist=null;
                                        main.gameWon();
                                    } catch (MalformedURLException ex) {
                                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    cancel();
                                }
                            });
                        }
                    };

                    gameStatusTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            //mainPlaylist.stopMusic();
                            try {
                                Playlist mainPlaylist = main.getPlaylist();
                                mainPlaylist.fadeOut();
                                mainPlaylist.disposePlaylist();
                                playlist = new Playlist(Playlist.Type.VICTORY);
                                playlist.playSound();
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            timer.schedule(wonTask, 3000);   //Als het de laatste ronde was heb je het spel nu uitgespeeld
                        }
                    });
                    gameStatusTransition.play();
                } else {
                    //Nieuwe willekeurige chamber initialiseren voor de volgende rond en de hero op zijn startpositie zetten
                    moveHeroToStart();
                    initializeChamber(gp.createRandomChamber());
                }
            }
        }
    }

    /**
     * Initializes the correct values for the current monster entering the
     * screen.
     */
    public void AnimateMonsterEntering() {
        monsterMoveTo = new TranslateTransition();
        monsterMoveTo.setNode(ivMonster);
        monsterMoveTo.setFromX(0);
        monsterMoveTo.setToX(-(getWidth() - monsterFightPt));      //Bewegen naar gevechtspositie monster
        monsterMoveTo.setDuration(Duration.millis(1500));          //Speed hier niet belangrijk, iedereen komt even snel scherm binnen
    }

    /**
     * Perform the necessary actions for initializing the fight. Removes
     * unnecessary panels, adds the right monsterpanel and then starts the first
     * fight turn
     *
     * @author Robin
     */
    private void initializeFight() {
        //Inventory sluiten indien hij nog openstond
        frame.closeInventory();

        //Gevecht is bezig (zodat je niet kan valsspelen door spel af te sluiten als je ziet dat je gaat verliezen of dood bent)
        fighting = true;

        //Gevecht starten met monster van deze kamer
        Monster monster = chamber.getMonster();
        main.initializeFight(chamber.getMonster());
        fight(hero, monster, 0);
    }

    /**
     * One turn in a fight. Either the hero or the monster will get a turn.
     *
     * @param hero the hero who will fight against a monster
     * @param monster the monster the hero will fight against
     * @param turn 1 for the hero's turn, 2 for the monster's turn and 0 if
     * awareness will decide whose turn it is
     */
    public void fight(Hero hero, Monster monster, int turn) {           //1 vechtbeurt (-1 of waarde kleiner dan 0 vluchten)

        int awarenessH = hero.calcTotalAwareness();
        int awarenessM = monster.calcTotalAwareness();

        if (turn >= 0) {
            if (turn == 0) {
                turn = (awarenessH > awarenessM) ? 1 : 2;           //Eerste beurt kijken wie grootste awareness heeft
            }
            if (turn == 1) {
                controls.enableAFButtons();               //Aanval en vluchten is mogelijk
            } else if (turn == 2) {
                controls.enableDButton();               //Verdedigen is mogelijk
                animateMonsterAtk(monster);                    //Monster valt aan (animatie)
            } else {
                fight(hero, monster, Utility.generateRandom(1, 2));  //Beurt random toekennen aan monster of hero
            }
        }
        if (turn < 0) {
            //Hero vlucht
            heroFlees();
            //Gevecht is voorbij
            fighting = false;
        }
    }

    /**
     * Initiate the hero's attack. Play an animation moving the hero to the
     * monster's position and calculate if he defeated it.
     */
    public void animateHeroAtk() {
        //Ongeveer op het punt van het monster het gevecht laten beginnen
        double offset = Math.abs(getHeroWidth() - getMonsterWidth()) / 2.0;
        double monsterPos = monsterFightPt - offset;
        double heroPos = heroFightPt;
        //Zet de wolk een beetje meer naar links
        fightPoint = monsterFightPt - offset - ivCloud.getLayoutBounds().getWidth() / 3.0;

        //Bewegen naar monster
        hTranslationTo = new TranslateTransition();
        hTranslationFrom = new TranslateTransition();
        player.setHeroMoveRight();
        hTranslationTo.setNode(player);
        hTranslationTo.setFromX(heroPos);
        hTranslationTo.setToX(monsterPos);
        //Afhankelijk van snelheid de hero laten bewegen
        hTranslationTo.setDuration(Duration.millis(2500 + Hero.getMAX() * 50 - hero.getSpeed() * 50));

        //Terug naar originele positie verplaatsen
        hTranslationFrom.setNode(player);
        hTranslationFrom.setFromX(monsterPos);
        hTranslationFrom.setToX(heroPos);
        hTranslationFrom.setDuration(Duration.millis(2500 + Hero.getMAX() * 50 - hero.getSpeed() * 50));

        //De wolkanimatie starten
        cloud = new Timeline();
        KeyValue kv = new KeyValue(ivCloud.layoutXProperty(), fightPoint);
        KeyFrame kf = new KeyFrame(Duration.millis(3000), kv);
        cloud.getKeyFrames().add(kf);

        cloud.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //Berekent of monster dood is en doet dan het nodige
                if (hero.attack(chamber.getMonster())) {
                    controls.hideButtons();
                    monsterDies();
                    player.setHeroIdleRight();
                    removeCloud();
                } else {
                    //Als monster niet door is wordt er teruggegaan
                    player.setHeroMoveLeft();
                    removeCloud();
                    hTranslationFrom.play();
                }
            }
        });

        //Gevecht gaat voor nadat hij terug is op zijn positie
        hTranslationFrom.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                player.setHeroIdleRight();
                frame.focusControls();
                //fight(hero, chamber.getMonster(), 2);             //Duidelijker met continuefight-methode, wel iets meer overhead
                main.continueFight(2);                              //Gevecht gaat voort en is monster's turn
            }
        });

        //Gevechtswolk initialiseren wanneer hij bij het gevechtspunt is
        hTranslationTo.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                initiateCloud();
                cloud.play();
            }
        });

        // Start the translation animation.
        hTranslationTo.play();
    }

    /**
     * Initiate the monster's attack. Play an animation moving the monster to
     * the hero's position and calculate if he defeated him.
     *
     * @param monster the monster who will attack the hero
     */
    private void animateMonsterAtk(final Monster monster) {
        //Correcte image van monster nemen (kan nog op idle staan, de avatar is de vooruitbewegende avatar)
        setMonsterImage(monster.getAvatar());

        double offset = Math.abs(getHeroWidth() - getMonsterWidth()) / 2.0;
        double monsterPos = -(getWidth() - monsterFightPt);
        double heroPos = -(getWidth() - heroFightPt - offset);
        fightPoint = heroFightPt + offset - ivCloud.getLayoutBounds().getWidth() / 3.0;

        mTranslationTo = new TranslateTransition();
        mTranslationFrom = new TranslateTransition();

        mTranslationTo.setNode(ivMonster);
        mTranslationTo.setFromX(monsterPos);
        mTranslationTo.setToX(heroPos);
        mTranslationTo.setDuration(Duration.millis(2500 + Monster.getMAX() - monster.getSpeed() * 50));

        mTranslationFrom.setNode(ivMonster);
        mTranslationFrom.setFromX(heroPos);
        mTranslationFrom.setToX(monsterPos);
        mTranslationFrom.setDuration(Duration.millis(2500 + Monster.getMAX() - monster.getSpeed() * 50));

        //Cloud
        cloud = new Timeline();
        KeyValue kv = new KeyValue(ivCloud.layoutXProperty(), fightPoint);
        KeyFrame kf = new KeyFrame(Duration.millis(3000), kv);
        cloud.getKeyFrames().add(kf);

        cloud.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //hero heeft op tijd op defend-knop gedrukt en succesvol verdedigt
                if (controls.defPressed() && hero.defend(chamber.getMonster())) {
                    removeCloud();
                    mTranslationFrom.play();
                } else {
                    //Monster gaat terug naar idle-image (indien het er een heeft)
                    setIdle(monster);

                    //Er kan niets meer gedaan worden
                    controls.hideButtons();
                    //Hero sterft
                    heroDies();
                    //Wolk verdwijnt
                    removeCloud();
                }
            }
        });

        mTranslationFrom.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setIdle(monster);
                frame.focusControls();
                main.continueFight(1);              //Gevecht gaat voort en is hero's turn
            }
        });

        mTranslationTo.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                setBack(monster);
                initiateCloud();
                cloud.play();
            }
        });

        // Start the translation animation.
        mTranslationTo.play();
    }

    /**
     * Place the cloud on the right position and show it while hiding the hero
     * and monster.
     */
    private void initiateCloud() {
        ivCloud.setLayoutX(fightPoint);
        ivCloud.setOpacity(1);
        player.setOpacity(0);
        ivMonster.setOpacity(0);
    }

    /**
     * Hide the cloud and show the hero and monster.
     */
    private void removeCloud() {
        player.setOpacity(1);
        ivMonster.setOpacity(1);
        ivCloud.setOpacity(0);
    }

    /**
     * Change the monster-image to one where he's standing still.
     *
     * @param monster the monster who's image has to be changed to the monster
     * standing still
     */
    public void setIdle(Monster monster) {
        String temp = monster.getAvatar();

        temp = temp.substring(0, temp.length() - 4);
        temp = temp + "Idle.gif";

        if (Main.class.getResourceAsStream("/images/monster/" + temp) != null) {
            setMonsterImage(temp);
        } else {
            setMonsterImage(monster.getAvatar());
        }
    }

    /**
     * Change the monster-image to one where he's running back.
     *
     * @param monster the monster who's image has to be changed to the monster
     * running back
     */
    public void setBack(Monster monster) {
        String temp = monster.getAvatar();
        temp = temp.substring(0, temp.length() - 4);
        setMonsterImage(temp + "Back.gif");
    }

    /**
     * Perform the necessary actions for ending a fight with the death of the
     * monster. Removes the monster from the game.
     */
    private void monsterDies() {
        //Kamer heeft enkel nog een treasure, geen monster meer
        chamber.removeMonster();
        //Zijn levenshartjes wegdoen
        removeMonsterLife();
        //monster is verslagen en verdwijnt
        getChildren().removeAll(ivMonster);

        //player kan terug bewegen
        enableMovement = true;
        //Gevecht is voorbij
        fighting = false;

        frame.focusGame();
    }

    /**
     *      * Perform the necessary actions for ending a fight with the hero
     * fleeing. Plays an animation of the hero running left and then resets the
     * chamber and the hero's position.
     */
    private void heroFlees() {
        //Hero loopt naar links tot net buiten beeld
        player.setHeroMoveLeft();
        translation = new TranslateTransition();
        translation.setNode(player);
        translation.setFromX(player.getTranslateX());
        translation.setToX(-getHeroWidth());
        translation.setDuration(Duration.seconds(3));
        translation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //Heropstarten kamer
                moveHeroToStart();
                frame.focusGame();
                initializeChamber(gp.createRandomChamber());
            }
        });

        // Start the translation animation.
        translation.play();
    }

    /**
     * Perform the necessary actions for ending a fight with the death of the
     * hero. Plays an animation of the hero running left and then resets the
     * chamber and the hero's position.
     */
    private void heroDies() {
        setFocusTraversable(false);        //Game is voorbij, heeft geen focus meer nodig
        enableMovement = false;             //Beweging is niet meer mogelijk
        hero.setAlive(false);               //Hero is gestorven
        removeHeroLife();                   //Hero's levenshartjes verwijderen
        placeGravestone();                  //Grafsteen tevoorschijn laten komen
    }

    /**
     * Place a gravestone with the dead hero's name on it. A defeat-message will
     * be shown afterwards and finally the highscores.
     */
    private void placeGravestone() {
        //Punt waar hero stierf
        double originalPt = heroFightPt + (Math.abs(getHeroWidth() - getMonsterWidth())) / 2.0;

        //Hero even onzichtbaar terwijl hij vervangen wordt door een grafsteen
        player.setOpacity(0);
        player.setImage(deadHero);

        //Positie voor de grafsteen bepalen
        recalcHeroPos();

        //Grafsteen vlak voor monster zetten en onder de grond waarna hij weer zichtbaar mag worden
        player.setTranslateX(originalPt - getHeroWidth());
        player.setTranslateY(getHeight());
        player.setOpacity(1);

        final double heightGrave = getHeroHeight();
        final double widthGrave = getHeroWidth();

        //Bepaling naam grafsteen, enter zetten bij een lange naam
        String name = hero.getName();
        if (name.length() > 7) {                //Plaats voor 14 letters
            name = name.substring(0, 7) + "\n" + name.substring(7);
        }

        //Naamlabel initialiseren en op grafsteen positioneren
        final Label nameLbl = new Label(name);
        nameLbl.setOpacity(0);
        getChildren().addAll(nameLbl);
        nameLbl.setLayoutY(getHeight() - heightGrave / 2.0);
        nameLbl.setMaxWidth(widthGrave / 3.0);

        nameLbl.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                nameLbl.setLayoutX(player.getTranslateX() + getHeroWidth() / 2.0 - newWidth.doubleValue() / 2.0);
            }
        });

        nameLbl.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newHeight
            ) {
                nameLbl.setLayoutY(getHeight() - getHeroHeight() / 2.0 - newHeight.doubleValue() / 4.0);
            }
        });

        deadTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fighting = false;
                        try {
                            playlist.disposePlaylist();
                            playlist=null;
                            main.gameOver();            //Er wordt naar de highscores gegaan nadat alle animaties afgelopen zijn
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        cancel();
                    }
                });
            }
        };

        //Grafsteen staat boven de grond
        translation = new TranslateTransition();
        translation.setNode(player);
        translation.setFromY(getHeight());                          //De gravestone staat onder de grond
        translation.setToY(0);                                      //De gravestone komt boven de grond
        translation.setDuration(Duration.seconds(2));

        translation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ft.play();
            }
        });

        ft = new FadeTransition(Duration.millis(1000), nameLbl);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(5);
        ft.setAutoReverse(true);

        ft.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                nameLbl.setOpacity(1);
                gameStatusTransition.play();            //Nadat grafsteen er staat aftellen naar highscores
            }
        });

        //Defeat-message laten verschijnen nadat grafsteen er staat
        ivGameStatus.setImage(imgDefeat);
        getChildren().addAll(ivGameStatus);
        gameStatusTransition = new FadeTransition(Duration.millis(1000), ivGameStatus);
        gameStatusTransition.setFromValue(0);
        gameStatusTransition.setToValue(1);

        ivGameStatus.getLayoutBounds().getHeight();
        ivGameStatus.getLayoutBounds().getWidth();

        gameStatusTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                try {
                    //Na een zekere delay naar startscherm gaan
                    Playlist mainPlaylist = main.getPlaylist();
                    mainPlaylist.fadeOut();
                    playlist = new Playlist(Playlist.Type.DEFEAT);
                    playlist.playSound();
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                timer.schedule(deadTask, 3000);
            }
        });

        // Start the translation animation.
        translation.play();
    }

    /**
     * Add a panel that shows the treasures inside the treasure chest sets the
     * chest-imageview to an open chest.
     *
     * @author Robin
     */
    public void openChest() {
        if (ivTreasure.getImage().equals(closedChest)) {        //Als kist gesloten is, de kist openen
            ivTreasure.setImage(openChest);
            if (frame.getChildren().contains(chestPanel)) {
                //ChestPanel zichtbaar maken indien reeds aangemaakt
                chestPanel.setOpacity(1);
            } else {
                //ChestPanel aanmaken indien nog niet gebeurd
                chestPanel = new ChestPanel(frame, this, chamber.getTreasures());

                frame.getChildren().addAll(chestPanel);

                chestPanel.setMinHeight(frame.getHeight() * 3.0 / 4.0);
                chestPanel.setMaxHeight(frame.getHeight() * 3.0 / 4.0);

                chestPanel.setLayoutY(125);     //Grootte detailhero en detailMonster, komt er vlak onder
                chestPanel.setLayoutX(frame.getWidth() * 2.0 / 4.0);        //Beginnend naast het inventorypanel, vanaf het midden van het scherm

               /* chestPanel.widthProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
                    ) {
                        double width = newWidth.doubleValue();
                        chestPanel.setLayoutX(frame.getWidth() * 2.0 / 4.0);
                    }
                });*/
            }

            //Kan even niet bewegen, kist was geopend (geweten zodat er niet kan herladen worden van het spel als je niet tevreden bent met de chest)  
            enableMovement = false;
            chestWasOpened = true;

            controls.hideOpen();            //Kist is reeds geopend, dus knop mag verborgen worden

            //inventory openen samen met chestpanel
            frame.openInventory();

            chestPanel.setDisable(false);
            chestPanel.requestFocus();
        }
    }

    /**
     * Hide and disable the panel that shows the treasures inside the treasure
     * chest sets the chest-imageview to a closed chest.
     *
     * @author Robin
     */
    public void closeChest() {
        if (ivTreasure.getImage().equals(openChest)) {
            //Als de kist leeg is, blijft ze openstaan en kan er niet meer mee geinterageerd worden
            if (!chestPanel.isCleared()) {
                ivTreasure.setImage(closedChest);                   //Kist sluiten indien nog open en indien nog iets bevat
                controls.showOpen();
            }

            //Als de kist niet leeg is, de kist sluiten
            chestPanel.setOpacity(0);
            chestPanel.setDisable(true);
            enableMovement = true;

            //Inventory sluiten
            frame.closeInventory();

            //Hero's stats aanpassen op detailhero en zijn score bijwerken
            updateDetailHero();
            updateDetailGame("" + hero.getScore());

            //Game herfocussen
            frame.focusGame();
        }
    }

    /**
     * Initialize DetailGame of this game.
     */
    private void initializeDetailGame() {
        detailGame = new DetailGame();
        getChildren().addAll(detailGame);
        detailGame.getStyleClass().add("detailgame");
    }

    /**
     * Update the DetailGame shown in the game. Will update the score to the
     * given score and the round to the current round the hero's in.
     *
     * @param score new value for the score shown in DetailGame
     */
    public void updateDetailGame(String score) {
        detailGame.update(score);
    }

    /**
     * Bring the hero's displayed stats up-to-date.
     */
    public void updateDetailHero() {            //Om gegevens van hero bij te werken als hij schatten opraapt
        detailHero.setPower("" + hero.getTotalPower());
        detailHero.setDefense("" + hero.getTotalDefense());
        detailHero.setSpeed("" + hero.getTotalSpeed());
        detailHero.setAwareness("" + hero.getTotalAwareness());
    }

    /**
     * Remove the hero's life from the game's DetailHero.
     */
    private void removeHeroLife() {
        detailHero.removeLife();
    }

    /**
     * Initialize and position DetailMonster showing detailed information of the
     * current monster in the game.
     *
     * @param monster the monster whose information will be used to create a
     * DetailMonser
     */
    public void initializeMonster(Monster monster) {
        detailMonster = new DetailMonster(monster);
        detailMonster.getStylesheets().add("/css/UI.css");
        detailMonster.getStyleClass().add("playerName");            //Zelfde opmaak als DetailHero

        getChildren().addAll(detailMonster);

        //Kan pas goed positioneren eens de width is ingesteld
        detailMonster.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                detailMonster.relocate(getWidth() - newWidth.doubleValue(), 0);
            }
        });

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newHeight
            ) {
                double widthTreasure = detailMonster.getLayoutBounds().getWidth();
                detailMonster.relocate(getWidth() - widthTreasure, 0);
            }
        });

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                double widthTreasure = detailMonster.getLayoutBounds().getWidth();
                detailMonster.relocate(getWidth() - widthTreasure, 0);
            }
        });
    }

    /**
     * Remove the current DetailMonster from the game-panel if one is present.
     */
    private void removeMonsterDetail() {
        if (detailMonster != null) {
            getChildren().removeAll(detailMonster);
            detailMonster = null;
        }
    }

    /**
     * Remove the monster's life from the game's DetailMonster.
     */
    private void removeMonsterLife() {
        //Levenshartjes verdwijnen
        detailMonster.removeLife();
    }

    /**
     * Add resources to the game for direct use. The resources that will be
     * added will be used a lot and adding them prevents unnecessary overhead by
     * having to call MainPanel-methods first
     *
     * @param hero hero of the game
     * @param detail DetailHero of the hero to be added to the game
     * @param frame owner of the game (its parent)
     * @param gp information and logic needed for the game's process.
     */
    public void addResources(Hero hero, DetailHero detail, Frame frame, GameProcesses gp) {
        this.hero = hero;
        detailHero = detail;
        detailHero.setOpacity(0);
        getChildren().addAll(detailHero);
        this.frame = frame;
        this.gp = gp;
    }

    /**
     * Stops all animations that are still running, cancels all timertasks and
     * the timer itself.
     */
    public void stopAnimations() {
        if (hTranslationTo != null) {
            hTranslationTo.stop();
        }
        if (mTranslationTo != null) {
            mTranslationTo.stop();
        }
        if (hTranslationFrom != null) {
            hTranslationFrom.stop();
        }
        if (mTranslationFrom != null) {
            mTranslationFrom.stop();
        }
        if (translation != null) {
            translation.stop();
        }
        if (monsterMoveTo != null) {
            monsterMoveTo.stop();
        }
        if (cloud != null) {
            cloud.stop();
        }
        if (ft != null) {
            ft.stop();
        }
        if (delay != null) {
            delay.cancel();
        }
        if (wonTask != null) {
            wonTask.cancel();
        }
        if (deadTask != null) {
            deadTask.cancel();
        }
        if (gameStatusTransition != null) {
            gameStatusTransition.stop();
        }
        if(playlist!=null)
        {
            playlist.disposePlaylist();
        }
        timer.cancel();
        timer.purge();
    }

    /**
     * Give the player's width.
     *
     * @return the player's width
     */
    public double getHeroWidth() {
        return player.getLayoutBounds().getWidth();
    }

    /**
     * Give the player's height.
     *
     * @return the player's height
     */
    public double getHeroHeight() {
        return player.getLayoutBounds().getHeight();
    }

    /**
     * Give the monster's width
     *
     * @return the monster's width
     */
    public double getMonsterWidth() {
        return ivMonster.getLayoutBounds().getWidth();
    }

    /**
     * Give the monster's height
     *
     * @return the monster's height
     */
    public double getMonsterHeight() {
        return ivMonster.getLayoutBounds().getHeight();
    }

    /**
     * Give the current monster of the game.
     *
     * @return the current monster of the game
     */
    public Monster getMonster() {
        return chamber.getMonster();
    }

    /**
     * Give the hero of the game.
     *
     * @return the hero of the game
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Check is the hero's fighting.
     *
     * @return true if the hero's currently in a fight, false if not
     */
    public boolean getFighting() {
        return fighting;
    }

    /**
     * Check if the chest was opened
     *
     * @return true if the current chamber's chest was opened, false if not
     */
    public boolean getChestWasOpened() {
        return chestWasOpened;
    }
}
