/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * Frame that contains both the game-panel and the controls-panel.
 *
 * @author Robin De Haes
 */
public class Frame extends Pane {

    private Game game;
    private Controls controls;

    private InventoryPanel inventoryPanel;

    private MainPanel main;

    /**
     * Initialize the Frame for the game
     *
     * @param game The Game-panel to be added to the Frame
     * @param controls The Controls-panel to be added to the game
     * @param main The owner of the Frame (its parent)
     */
    public Frame(Game game, Controls controls, MainPanel main) {
        this.game = game;
        this.controls = controls;
        this.main = main;
        buildFrame();
    }

    /**
     * Initialization and configuration of the Frame.
     */
    private void buildFrame() {
        getChildren().addAll(game, controls);

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newHeight
            ) {
                double percentage = 0.80;               //Hoeveel procent het game inneemt van het beeld
                double height = newHeight.doubleValue();

                game.relocate(0, 0);
                game.setMinHeight(percentage * height);
                game.setMaxHeight(0 - percentage * height);

                controls.relocate(0, percentage * height);
                controls.setMinHeight((1 - percentage) * height);
                controls.setMaxHeight((1 - percentage) * height);

            }
        });

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                double width = newWidth.doubleValue();

                game.setMinWidth(width);
                game.setMaxWidth(width);

                controls.setMinWidth(width);
                controls.setMaxWidth(width);
            }
        });

        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {         //Zodat ge enkel tussen de 2 knoppen kunt tabben

            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.T) {         //Om de kist te openen
                    controls.pressOpen();
                } else if (t.getCode() == KeyCode.M) {
                    controls.toggleMusic();
                } else if (t.getCode() == KeyCode.I) {
                    toggleInventory();
                }
            }
        });
    }

    /**
     * Add an InventoryPanel to the Frame. The content of the InventoryPanel
     * will be based on the hero's
     */
    public void addInventory() {
        inventoryPanel = new InventoryPanel(main.getHero().getInventory(), main);
        getChildren().addAll(inventoryPanel);
        inventoryPanel.setOpacity(0);
        inventoryPanel.setDisable(true);

        inventoryPanel.setMinHeight(getHeight() * 3.0 / 4.0);
        inventoryPanel.setMaxHeight(getHeight() * 3.0 / 4.0);
        inventoryPanel.setLayoutY(125);     //Hoogte detailHero, het moet net daaronder komen

        inventoryPanel.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newWidth
            ) {
                double width = newWidth.doubleValue();

                inventoryPanel.setLayoutX(getWidth() * 2.0 / 4.0 - width);
            }
        });

    }

    /**
     * Show or hide the InventoryPanel. Based on whether it's already showing or
     * hiding, it will respectively hide or show it.
     */
    public void toggleInventory() {
        double opacity = inventoryPanel.getOpacity();
        if (opacity == 0.0 && !game.getFighting()) {          //inventory niet openen tijdens een gevecht
            openInventory();
        } else {
            closeInventory();
        }
    }

    /**
     * Show the InventoryPanel.
     */
    public void openInventory() {
        inventoryPanel.setOpacity(1);
        inventoryPanel.setDisable(false);
    }

    /**
     * Hide the InventoryPanel.
     */
    public void closeInventory() {
        inventoryPanel.setOpacity(0);
        inventoryPanel.setDisable(true);
        //focusGame niet nodig als focustraversable van game nog op true staat
        focusGame();
    }

    /**
     * Bring the InventoryPanel up-to-date with the hero's current inventory.
     */
    public void updateInv() {
        inventoryPanel.update();
    }

    /**
     * Focus the Game and enable movement control of the hero. Will be used
     * while creating the MainPanel.
     */
    public void focusGame() {
        game.setFocusTraversable(true);                     //Als hij op true staat zal hij bij het toevoegen van een node als eerste de focus krijgen tenzij expliciete requestFocus gebruikt wordt
        controls.setFocusTraversable(false);
        game.requestFocus();
    }

    /**
     * Focus the Controls and enable fight-control of the hero. Will be used
     * when initiating a fight.
     */
    public void focusControls() {
        game.setFocusTraversable(false);                //Er kan niet naar het game worden gegaan
        controls.setFocusTraversable(true);
        controls.requestFocus();
        controls.showButtons();
    }
}
