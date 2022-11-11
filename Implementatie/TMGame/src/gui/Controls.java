/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.GameProcesses;
import domein.Hero;
import domein.Monster;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Panel that has all the controls inside it. This panel will be added at the
 * bottom of the frame, below the gamepanel.
 *
 * @author Robin
 */
public class Controls extends Pane {

    private Pane controlPane;
    private Button atkBtn, fleeBtn, defBtn, openBtn;
    private MainPanel main;

    private static Image musicOn = new Image(Main.class.getResourceAsStream("/images/icons/unmute.png"));
    private static Image musicOff = new Image(Main.class.getResourceAsStream("/images/icons/mute.png"));
    private static Image inventory = new Image(Main.class.getResourceAsStream("/images/icons/inventory.gif"));

    private ImageView ivMusic, ivInv;

    /**
     * Initializing bottom box of the game, contains the floor as background.
     *
     * @param main the owner of Controls (its parent)
     */
    public Controls(MainPanel main) {
        this.main = main;
        getStylesheets().add("css/gameplatform.css");   // add stylesheet
        initializeMusic();
        initializeInventory();
        initializePane();
        initializeOpenBtn();
    }

    /**
     * Initialize the music icon for toggling the music on or off.
     */
    private void initializeMusic() {
        ivMusic = new ImageView(musicOn);
        getChildren().addAll(ivMusic);

        ivMusic.setFitHeight(50);
        ivMusic.setFitWidth(50);

        ivMusic.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                toggleMusic();
            }
        });
    }

    /**
     * Initialize the inventory icon for showing or hiding the inventory.
     */
    private void initializeInventory() {
        ivInv = new ImageView(inventory);
        getChildren().addAll(ivInv);

        ivInv.setFitHeight(100);
        ivInv.setFitWidth(100);

        ivInv.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                main.toggleInventory();
            }
        });
    }

    /**
     * Initialize the pane containing the buttons for controlling the
     * fight-options of the player.
     */
    private void initializePane() {
        controlPane = new Pane();
        atkBtn = new Button("Attack");
        fleeBtn = new Button("Flee");
        defBtn = new Button("Defend");
        openBtn = new Button("Take a look");

        double buttonWidth = 100.0;
        double buttonHeight = 40.0;
        double padding = 20.0;

        controlPane.setMaxWidth(3 * buttonWidth + padding);
        controlPane.setMinWidth(3 * buttonWidth + padding);
        controlPane.setMaxHeight(buttonHeight);
        controlPane.setMinHeight(buttonHeight);

        atkBtn.setMinSize(buttonWidth, buttonHeight);
        atkBtn.setMaxSize(buttonWidth, buttonHeight);
        fleeBtn.setMinSize(buttonWidth, buttonHeight);
        fleeBtn.setMaxSize(buttonWidth, buttonHeight);
        defBtn.setMinSize(buttonWidth, buttonHeight);
        defBtn.setMaxSize(buttonWidth, buttonHeight);
        openBtn.setMaxSize(buttonWidth, buttonHeight);
        openBtn.setMinSize(buttonWidth, buttonHeight);

        getStylesheets().add("css/UI.css");
        atkBtn.getStyleClass().add("panel-button");
        fleeBtn.getStyleClass().add("panel-button");
        defBtn.getStyleClass().add("panel-button");
        openBtn.getStyleClass().add("panel-button");

        controlPane.getChildren().addAll(atkBtn, defBtn, fleeBtn);
        getChildren().add(controlPane);

        atkBtn.relocate(0, 0);
        defBtn.relocate(buttonWidth + padding / 2.0, 0);
        fleeBtn.relocate(2 * buttonWidth + 2 * padding / 2.0, 0);
        openBtn.relocate(buttonWidth + padding / 2.0, 0);
        //controls zijn gecentreerd binnenin het controlPane

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                recalcPos();
            }
        });

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                recalcPos();
            }
        });

        setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.A && !atkBtn.isDisable()) {
                    atkBtn.fire();
                } else if (keyEvent.getCode() == KeyCode.D && !defBtn.isDisable()) {
                    defBtn.fire();
                } else if (keyEvent.getCode() == KeyCode.F && !fleeBtn.isDisable()) {
                    fleeBtn.fire();
                }
            }
        });

        atkBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {         //code voor aanvallen
                main.heroAttacks();
                disableInput();
            }
        });

        defBtn.setOnAction(new EventHandler<ActionEvent>() {           //code voor verdedigen

            @Override
            public void handle(ActionEvent t) {
                disableInput();
            }
        });

        fleeBtn.setOnAction(new EventHandler<ActionEvent>() {           //code voor vluchten

            @Override
            public void handle(ActionEvent t) {
                Hero hero = main.getHero();
                Monster monster = main.getMonster();

                if (hero.flee(monster)) {         //beslist of vluchten succesvol was of niet
                    hideButtons();
                    main.continueFight(-1);         //-1 is vluchten (en gevecht beëindigen)
                } else {
                    main.continueFight(2);
                }
            }
        });

        fleeBtn.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {         //Zodat ge enkel tussen de 2 knoppen kunt tabben

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.TAB) {
                    t.consume();            //Normale TAB-operatie wordt niet uitgevoerd
                    atkBtn.requestFocus();
                }
            }
        });

        defBtn.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {         //Zodat ge enkel tussen de 2 knoppen kunt tabben

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.TAB) {
                    t.consume();            //Normale TAB-operatie wordt niet uitgevoerd
                    defBtn.requestFocus();
                }
            }
        });

        controlPane.setOpacity(0);          //Knopjes verbergen
        atkBtn.setDisable(true);
        fleeBtn.setDisable(true);
        defBtn.setDisable(true);
        openBtn.setDisable(true);
        openBtn.setOpacity(0);
    }

    /**
     * Initialize and add the button to open the treasurechest in a game.
     */
    private void initializeOpenBtn() {
        double width = 50;
        double height = 150;

        openBtn.setMinSize(height, width);
        openBtn.setMaxSize(height, width);

        getChildren().addAll(openBtn);

        openBtn.setOnAction(new EventHandler<ActionEvent>() {           //code voor vluchten

            @Override
            public void handle(ActionEvent t) {
                main.focusGame();
                main.openChest();
            }
        });
    }

    /**
     * Enables the use of the Defend-button, disables the Attack- and
     * Defend-button.
     */
    public void enableDButton() {
        atkBtn.setDisable(true);
        fleeBtn.setDisable(true);
        defBtn.setDisable(false);
        defBtn.requestFocus();
    }

    /**
     * Enable the use of the Attack- and Flee-buttons, disable the
     * Defend-button.
     */
    public void enableAFButtons() {
        atkBtn.setDisable(false);
        if (!GameProcesses.isMaxRound()) {
            fleeBtn.setDisable(false);
        }
        defBtn.setDisable(true);
    }

    /**
     * Disable and show all the buttons.
     */
    public void showButtons() {
        atkBtn.setDisable(true);
        fleeBtn.setDisable(true);
        defBtn.setDisable(true);
        controlPane.setOpacity(1);      //Knopjes tonen
    }

    /**
     * Disable and hide all the buttons.
     */
    public void hideButtons() {
        controlPane.setOpacity(0);
        atkBtn.setDisable(true);
        fleeBtn.setDisable(true);
        defBtn.setDisable(true);
    }

    /**
     * Disable the input of all fight-buttons. This will be used during
     * fight-animations.
     */
    public void disableInput() {
        defBtn.setDisable(true);
        atkBtn.setDisable(true);
        fleeBtn.setDisable(true);
    }

    /**
     * Check if the Defend-button has been pressed during a defensive phase of
     * the fight. It will check this by seeing if the button has been disabled
     * when called in the defensive phase (only one button can be clicked every
     * phase).
     *
     * @return
     */
    public boolean defPressed() {
        return defBtn.isDisable();
    }

    /**
     * Shows and enables the button to open the treasurechest in the game.
     */
    public void showOpen() {
        openBtn.setOpacity(1);
        openBtn.setDisable(false);
    }

    /**
     * Hides and disables the button to open the treasurechest in the game.
     */
    public void hideOpen() {
        openBtn.setOpacity(0);
        openBtn.setDisable(true);
    }

    /**
     * Internally presses the open-button if it isn't disabled.
     */
    public void pressOpen() {
        if (!openBtn.isDisable()) {
            openBtn.fire();
        }
    }

    /**
     * Toggles the music on or off.
     */
    public void toggleMusic() {
        main.getPlaylist().toggleMute();
        if (ivMusic.getImage().equals(musicOn)) {
            ivMusic.setImage(musicOff);
        } else {
            ivMusic.setImage(musicOn);
        }
    }

    /**
     * Set the Controls background
     *
     * @param platformType type of platform that will be used as background of
     * Controls
     */
    public void setPlatform(String platformType) {
        getStyleClass().clear();
        getStyleClass().add(platformType);
    }

    /**
     * Recalculate the positions of Controls' children inside Controls.
     */
    private void recalcPos() {
        controlPane.relocate(getWidth() / 2.0 - controlPane.getWidth() / 2.0, getHeight() / 2.0 - controlPane.getHeight() / 2.0);
        //controlPane centreren
        ivMusic.relocate(0, getHeight() / 2.0 - ivMusic.getLayoutBounds().getHeight() / 2.0);

        ivInv.relocate(getWidth() - ivInv.getLayoutBounds().getWidth(), getHeight() / 2.0 - ivInv.getLayoutBounds().getHeight() / 2.0);

        openBtn.relocate(getWidth() / 2.0 - openBtn.getWidth() / 2.0, getHeight() / 2.0 - openBtn.getHeight() / 2.0);
    }

}
