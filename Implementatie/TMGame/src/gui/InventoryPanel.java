/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Inventory;
import domein.Treasure;
import exceptions.EmptyArgumentException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.setHalignment;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 * Small panel showing the treasures inside the hero's inventory.
 *
 * @author simon
 */
public class InventoryPanel extends GridPane {

    private Inventory inventory;
    private MainPanel main;

    private int counter;
    private Button drop;
    private Label titel;
    private List<Treasure> treasureList;
    private ListView<GridPane> treasureListView;

    /**
     * Constructor for the InventoryPanel.
     *
     * @param inventory the inventory that will be displayed in the panel
     * @param main the owner of the panel (its parent)
     */
    public InventoryPanel(Inventory inventory, MainPanel main) {
        this.inventory = inventory;
        this.main = main;
        initPanel();
    }

    /**
     * Builds the InventoryPanel.
     */
    private void initPanel() {
        // de elementen declareren
        drop = new Button("Drop");
        titel = new Label("INVENTORY");

        treasureList = new ArrayList<>();
        treasureListView = new ListView<>();
        //GridPane gp = new GridPane();

        //de elementen plaatsen in gui
        add(titel, 0, 0, 3, 1);
        add(treasureListView, 0, 1, 3, 1);
        add(drop, 0, 2, 3, 1);

        //css en opmaak
        getStylesheets().add("css/UI.css");
        getStyleClass().add("panel");
        drop.getStyleClass().add("panel-button");
        titel.getStyleClass().add("panel-titel");

        titel.setAlignment(Pos.CENTER);
        titel.setMinSize(150, 70);
        titel.setMaxSize(150, 70);
        setHalignment(titel, HPos.CENTER);

        //listview vullen
        fillTreasureListView();

        //actionevent
        drop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ObservableList<Integer> indices = treasureListView.getSelectionModel().getSelectedIndices();

                    if (indices.size() == 0) {
                        throw new EmptyArgumentException();
                    }

                    for (int i = 0; i < indices.size(); i++) {
                        int position = indices.get(i);
                        inventory.removeTreasure(treasureList.get(position));
                    }

                    fillTreasureListView();
                    main.updateDetailHero();
                    main.updateDetailGame();
                } catch (EmptyArgumentException eae) {
                }

            }
        });

        drop.setAlignment(Pos.CENTER);
        drop.setMinSize(100, 65);
        drop.setMaxSize(100, 65);
        setHalignment(drop, HPos.CENTER);
        treasureListView.setMinWidth(300);           //Zodat alle 3 de knoppen erin kunnen
        treasureListView.setMaxWidth(300);
        setPadding(new Insets(27));
        setHgap(10);
        setVgap(10);
    }

    //Maakt treasure panes aan om in de lijsten te zetten
    /**
     * Creates gridpane of treasure with all information needed, these panes can
     * be used to display the treasures on the GUI.
     *
     * @param treasure Treasure is used to get all needed information from
     * database
     *
     * @return returns a gridpane of the given treasure ready to be displayed
     */
    private GridPane buildTreasurePane(Treasure treasure) {
        GridPane treasurePane = new GridPane();

        Image iPower = new Image(Main.class.getResourceAsStream("/images/icons/Sword.png"));
        Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
        Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
        Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));
        Image iValue = new Image(Main.class.getResourceAsStream("/images/icons/Value.png"));
        Image iAvatar = new Image(Main.class.getResourceAsStream("/images/treasures/" + treasure.getAvatar()));

        ImageView speedimgv = new ImageView(iSpeed);
        ImageView awarenessimgv = new ImageView(iAwareness);
        ImageView defenseimgv = new ImageView(iDefense);
        ImageView powerimgv = new ImageView(iPower);
        ImageView avatarimgv = new ImageView(iAvatar);
        ImageView valueimgv = new ImageView(iValue);

        Label power = new Label("" + treasure.getPower());
        Label defense = new Label("" + treasure.getDefense());
        Label speed = new Label("" + treasure.getSpeed());
        Label awareness = new Label("" + treasure.getAwareness());
        Label value = new Label("" + treasure.getValue());
        Label name = new Label("" + treasure.getName());
        Label description = new Label("\"" + format(treasure.getDescription()) + "\"");

        description.setFont(Font.font("Arial", FontPosture.ITALIC, USE_PREF_SIZE));
        name.setFont(Font.font("Arial", FontPosture.ITALIC, 16));

        valueimgv.setFitHeight(
                15);
        speedimgv.setFitHeight(
                15);
        awarenessimgv.setFitHeight(
                15);
        defenseimgv.setFitHeight(
                15);
        powerimgv.setFitHeight(
                15);
        avatarimgv.setFitHeight(
                50);

        valueimgv.setFitWidth(
                15);
        speedimgv.setFitWidth(
                15);
        awarenessimgv.setFitWidth(
                15);
        defenseimgv.setFitWidth(
                15);
        powerimgv.setFitWidth(
                15);
        avatarimgv.setFitWidth(
                50);

        treasurePane.setPadding(
                new Insets(15));
        treasurePane.setHgap(
                10);
        treasurePane.setVgap(
                10);

        treasurePane.add(name,
                0, 0, 7, 1);
        treasurePane.add(description,
                0, 1, 7, 1);
        treasurePane.add(avatarimgv,
                0, 2, 1, 2);
        treasurePane.add(powerimgv,
                1, 2);
        treasurePane.add(power,
                2, 2);
        treasurePane.add(speedimgv,
                3, 2);
        treasurePane.add(speed,
                4, 2);
        treasurePane.add(valueimgv,
                5, 3);
        treasurePane.add(value,
                6, 3);
        treasurePane.add(defenseimgv,
                1, 3);
        treasurePane.add(defense,
                2, 3);
        treasurePane.add(awarenessimgv,
                3, 3);
        treasurePane.add(awareness,
                4, 3);

        if (counter == 0) {
            treasurePane.getStyleClass().add("panel-cell1");
            counter = 1;
        } else {
            counter = 0;
            treasurePane.getStyleClass().add("panel-cell2");
        }

        return treasurePane;
    }

    //vult de treasureListView
    /**
     * Fills the list of all treasures present in the database.
     */
    private void fillTreasureListView() {
        treasureListView.getItems().clear();
        treasureList.clear();

        treasureList.addAll(inventory.giveTreasures());

        List<GridPane> helpList = new ArrayList<>();
        helpList.clear();
        for (int i = 0; i < treasureList.size(); i++) {
            helpList.add(buildTreasurePane(treasureList.get(i)));
        }
        treasureListView.setItems(FXCollections.observableList(helpList));
    }

    //Update methode om aan te halen in ChestPanel, om inventory steeds up to date te houden
    /**
     * Method used in other classes to update the inventory when needed.
     */
    public void update() {
        fillTreasureListView();
    }

    /**
     * Format a description so it would fit inside the panel
     *
     * @param description the description to be formatted
     * @return the formatted description
     * @author Robin
     */
    private String format(String description) {
        int line = Treasure.getMAX_DESCRIPTION() / 2;
        String temp = description;
        if (temp.length() > line) {
            temp = description.substring(0, line);
            int newLine = temp.lastIndexOf(" ");
            String firstHalf = description.substring(0, newLine) + "\n";
            String secondHalf = " " + description.substring(newLine + 1);
            String extra = "";
            if (secondHalf.length() > line) {
                temp = secondHalf.substring(0, line);
                newLine = temp.lastIndexOf(" ");
                extra = " " + secondHalf.substring(newLine + 1);
                secondHalf = secondHalf.substring(0, newLine) + "\n";
            }
            return firstHalf + secondHalf + extra;
        } else {
            return description;
        }
    }
}
