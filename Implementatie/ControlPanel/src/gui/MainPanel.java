/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.Monster;
import domein.Treasure;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author pieterjan, Steve, Simon en Robin
 */
public class MainPanel extends BorderPane {

    private DomeinController controller;
    private FlowPane monsterFlowPanel, monsterFunctionPanel, treasureFlowPanel, treasureFunctionPanel, linkPanel, showPanel;
    private RelationsPanel relationsPanel;
    private BorderPane innerTab;
    private Label message;

    /**
     * Create the main panel
     *
     * @param controller Domain controller
     */
    public MainPanel(DomeinController controller) {
        this.controller = controller;
        buildGui();
    }

    /**
     * Builds the GUI.
     */
    private void buildGui() {
        ToolBar toolbar = new ToolBar();
        toolbar.setStyle("-fx-background-color: #d2d2d2");
        message = new Label("Succesfully started");
        message.setTextFill(Color.BLACK);
        toolbar.getItems().add(message);
        setBottom(toolbar);

        TabPane center = new TabPane();
        center.setPrefSize(1410, 800);
        center.setTabMinHeight(20);

        Tab monsters = new Tab();
        monsters.setClosable(false);
        monsters.setText("Monsters");
        initializeMonstersTab(monsters);

        //Creatie en initialisatie van Treasurestab
        Tab treasures = new Tab();
        treasures.setClosable(false);
        treasures.setText("Treasures");
        initializeTreasuresTab(treasures);

        Tab relations = new Tab();
        relations.setClosable(false);
        relations.setText("Relations");
        initializeRelationsTab(relations);

        center.getTabs().addAll(monsters, treasures, relations);
        setCenter(center);
    }

    /**
     * Creates the Monster Panel
     *
     * @param tab Tab where the Panel should be initialized.
     */
    private void initializeMonstersTab(Tab tab) {
        monsterFunctionPanel = new FlowPane(Orientation.VERTICAL);
        monsterFunctionPanel.setPadding(new Insets(10));
        monsterFunctionPanel.setHgap(10);
        monsterFunctionPanel.setVgap(10);

        monsterFlowPanel = new FlowPane(Orientation.HORIZONTAL);
        monsterFlowPanel.setPadding(new Insets(10));
        monsterFlowPanel.setHgap(10);
        monsterFlowPanel.setVgap(10);

        // create a scrollpane incase scrolling is needed
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // No horizontal scrollbar
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scrollbar if needed
        scroll.setContent(monsterFlowPanel);

        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                monsterFlowPanel.setPrefWidth(bounds.getWidth());
                monsterFlowPanel.setPrefHeight(bounds.getHeight());
            }
        });

        // add Monster panel
        DetailMonster detail = new DetailMonster(controller);
        MonsterPanel monsterPanel = new MonsterPanel(controller, detail, this);
        monsterFunctionPanel.getChildren().addAll(monsterPanel);

        // show all Monsters in the database on launch
        showAllMonsters(monsterFlowPanel);

        // stackpane to combine scrollbar & flowpane
        StackPane root = new StackPane();
        root.getChildren().addAll(scroll);

        innerTab = new BorderPane();
        innerTab.setLeft(monsterFunctionPanel);
        innerTab.setCenter(root);

        // adds the root to the tab
        tab.setContent(innerTab);
    }

    /**
     * Show all monsters in the database
     *
     * @param panel The flowpane where the monsterPanels should be added.
     */
    private void showAllMonsters(FlowPane panel) {
        List<Monster> allMonsters = controller.searchAllMonsters();

        for (Monster monster : allMonsters) {
            MonsterPanel monsterPanel = new MonsterPanel(controller, monster, this);
            panel.getChildren().addAll(monsterPanel);
        }
    }

    /**
     * Creates the Treasures Panel
     *
     * @param tab Tab where the Panel should be initialized.
     */
    private void initializeTreasuresTab(Tab tab) {
        //Bevat het AddPanel voor treasures
        treasureFunctionPanel = new FlowPane(Orientation.VERTICAL);
        treasureFunctionPanel.setPadding(new Insets(10));
        treasureFunctionPanel.setHgap(10);
        treasureFunctionPanel.setVgap(10);

        //Toont alle treasurePanels
        treasureFlowPanel = new FlowPane(Orientation.HORIZONTAL);
        treasureFlowPanel.setPadding(new Insets(10));
        treasureFlowPanel.setHgap(10);
        treasureFlowPanel.setVgap(10);

        //Creates a scrollpane in case scrolling is needed
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // No horizontal scrollbar
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scrollbar if needed
        scroll.setContent(treasureFlowPanel);

        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                treasureFlowPanel.setPrefWidth(bounds.getWidth());
                treasureFlowPanel.setPrefHeight(bounds.getHeight());
            }
        });

        // add Treasure panel for adding Treasures
        DetailTreasure detail = new DetailTreasure(controller);
        TreasurePanel treasurePanel = new TreasurePanel(controller, detail, this);
        treasureFunctionPanel.getChildren().addAll(treasurePanel);

        // show all Treasures in the database on launch
        showAllTreasures(treasureFlowPanel);

        // Stackpane to combine scrollbar & flowpane
        StackPane root = new StackPane();
        root.getChildren().addAll(scroll);

        //AddPanel staat links
        innerTab = new BorderPane();
        innerTab.setLeft(treasureFunctionPanel);
        innerTab.setCenter(root);

        // adds the root to the tab
        tab.setContent(innerTab);
    }

    /**
     * Show all treasures in the database
     *
     * @param panel The flowpane where the treasurePanels should be added.
     * @author Robin De Haes
     */
    private void showAllTreasures(FlowPane panel) {
        List<Treasure> allTreasures = controller.searchAllTreasures();

        //Creëert een panel voor elke treasure in de database en voegt die toe
        for (Treasure treasure : allTreasures) {
            TreasurePanel treasurePanel = new TreasurePanel(controller, treasure, this);
            panel.getChildren().addAll(treasurePanel);
        }
    }

    private void initializeRelationsTab(Tab tab) {

        linkPanel = new FlowPane(Orientation.HORIZONTAL);
        linkPanel.setPadding(new Insets(10));
        linkPanel.setHgap(10);
        linkPanel.setVgap(10);

        relationsPanel = new RelationsPanel(this,controller);

        linkPanel.getChildren().addAll(relationsPanel);
        linkPanel.setAlignment(Pos.CENTER);
        relationsPanel.setStyle("-fx-border: 4px solid; -fx-border-color: #9a9a9a;");
        /*r.setPadding(new Insets(20));
         r.setHgap(10);
         r.setVgap(10);*/

        innerTab = new BorderPane();
        innerTab.setCenter(linkPanel);

        tab.setContent(innerTab);

    }

    /**
     * Set text of message-label
     *
     * @param m new text of message-label
     * @author Robin De Haes
     */
    public void setMessage(String m) {
        message.setText(m);
    }

    /**
     * Set textcolor of message-label
     *
     * @param color new textcolor of message-label
     * @author Robin De Haes
     */
    public void setMessageColor(Color color) {
        message.setTextFill(color);
    }

    /**
     * Add a monster to the monsterFlowPanel
     *
     *
     * @param monsterPanel monsterPanel that has to be added to the FlowPane
     * @author Robin De Haes
     */
    public void addToFlowPanel(MonsterPanel monsterPanel) {
        monsterFlowPanel.getChildren().addAll(monsterPanel);
    }

    /**
     * Remove a monster from the monsterFlowPanel
     *
     *
     * @param monsterPanel monsterPanel that has to be removed from the FlowPane
     * @author Robin De Haes
     */
    public void removeFromFlowPanel(MonsterPanel monsterPanel) {
        monsterFlowPanel.getChildren().removeAll(monsterPanel);
    }

    /**
     * Add a treasure to the treasureFlowPanel
     *
     *
     * @param treasurePanel treasurePanel that has to be added to the FlowPane
     * @author Robin De Haes
     */
    public void addToFlowPanel(TreasurePanel treasurePanel) {
        treasureFlowPanel.getChildren().addAll(treasurePanel);
    }

    /**
     * Remove a treasure from the treasureFlowPanel
     *
     *
     * @param treasurePanel treasurePanel that has to be removed from the
     * FlowPane
     * @author Robin De Haes
     */
    public void removeFromFlowPanel(TreasurePanel treasurePanel) {
        treasureFlowPanel.getChildren().removeAll(treasurePanel);
    }

    public void resetRelationsPanel() {
        relationsPanel.reset();
    }
}
