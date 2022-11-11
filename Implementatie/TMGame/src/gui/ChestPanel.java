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
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.setHalignment;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 * Panel presenting the treasures inside a chest.
 *
 * @author Simon
 */
public class ChestPanel extends GridPane {

    private Frame frame;
    private Game game;

    private Inventory inventory;

    private List<Treasure> treasureList = new ArrayList<>();
    private ListView<GridPane> treasureListView;

    private Button take;
    private Button exit;
    private Button takeAll;

    private Label titel;

    private int counter;

    /**
     * Default constructor for a ChestPanel
     *
     * @param frame the frame to which the ChestPanel belongs
     * @param game the game in which the ChestPanel will be called
     * @param treasures the treasures the ChestPanel holds
     */
    public ChestPanel(Frame frame, Game game, List<Treasure> treasures) {
        this.frame = frame;
        inventory = game.getHero().getInventory();
        this.game = game;
        this.treasureList = treasures;
        initPanel();
    }

    /**
     * Builds the ChestPanel.
     */
    private void initPanel() {
        //declareren van elementen
        treasureListView = new ListView<>();
        //multiple selection
        treasureListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        take = new Button("Take");
        takeAll = new Button("Take All");
        exit = new Button("Exit");
        titel = new Label("CHEST");

        //vult treasureListView
        fillTreasureListView();

        //elementen plaatsen in Grid
        GridPane button = new GridPane();
        add(titel, 0, 0);
        add(treasureListView, 0, 1);
        button.add(take, 1, 0);
        button.add(takeAll, 2, 0);
        button.add(exit, 3, 0);
        add(button, 0, 3);

        //de opmaak en css
        setPadding(new Insets(27));
        setHgap(0);
        setVgap(10);

        take.setMaxSize(100, 65);
        takeAll.setMaxSize(100, 65);
        exit.setMaxSize(100, 65);

        take.setMinSize(100, 65);
        takeAll.setMinSize(100, 65);
        exit.setMinSize(100, 65);

        getStylesheets().add("css/UI.css");
        getStyleClass().add("panel");
        take.getStyleClass().add("panel-button");
        takeAll.getStyleClass().add("panel-button");
        exit.getStyleClass().add("panel-button");
        titel.getStyleClass().add("panel-titel");

        titel.setAlignment(Pos.CENTER);
        titel.setMinSize(150, 70);
        titel.setMaxSize(150, 70);
        setHalignment(titel, HPos.CENTER);
        setHalignment(treasureListView, HPos.CENTER);

        treasureListView.setMinWidth(300);           //Zodat alle 3 de knoppen erin kunnen
        treasureListView.setMaxWidth(300);

        exit.setAlignment(Pos.CENTER);
        take.setAlignment(Pos.CENTER);
        takeAll.setAlignment(Pos.CENTER);

        //actionevents
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exit();
            }
        });

        take.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ObservableList<Integer> indices = treasureListView.getSelectionModel().getSelectedIndices();

                    if (indices.size() == 0) {
                        throw new EmptyArgumentException();
                    }

                    for (int i = indices.size() - 1; i >= 0; i--) {            //indices-1 is de laatsste  schat
                        int position = indices.get(i);
                        inventory.addTreasure(treasureList.get(position));
                        treasureList.remove(position);
                    }

                    fillTreasureListView();
                    frame.updateInv();
                    exit();
                } catch (EmptyArgumentException eae) {
                }
            }
        });

        takeAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < treasureList.size(); i++) {
                    inventory.addTreasure(treasureList.get(i));
                }
                treasureList.clear();
                treasureListView.getItems().clear();
                frame.updateInv();
                exit();
            }
        });

    }

    //Maakt treasure panes aan om in de lijsten te zetten
    /**
     * Creates gridpane of treasure with all information needed, these panes can
     * be used to display the treasures on the GUI
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

    //de methoden
    /**
     * Check if the ChestPanel is cleared from treasures.
     *
     * @return true if this panels contains no more treasures, false if it does
     * @author Robin
     */
    public boolean isCleared() {
        return treasureList.isEmpty();
    }

    //deze methode kijkt of er nog items zitten in de treasureList en Exit wanneer deze leeg is
    /**
     * Closes the chest in the gamescreen.
     *
     * @author Robin
     */
    private void exit() {
        game.closeChest();
    }

    //vult de treasureListView
    /**
     * Fills the list of all treasures present in the database
     */
    private void fillTreasureListView() {
        treasureListView.getItems().clear();
        List<GridPane> helpList = new ArrayList<>();
        helpList.clear();
        for (int i = 0; i < treasureList.size(); i++) {
            helpList.add(buildTreasurePane(treasureList.get(i)));
        }
        treasureListView.setItems(FXCollections.observableList(helpList));
    }

    /**
     * Formats the description so it fits inside the preserved space. Will split
     * the description in parts and place them under each other if description
     * is too long.
     *
     * @param description description to be formatted
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
