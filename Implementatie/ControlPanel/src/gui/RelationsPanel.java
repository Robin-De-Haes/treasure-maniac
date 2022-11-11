package gui;

import domein.DomeinController;
import domein.Monster;
import domein.Treasure;
import exceptions.AlreadyLinkedException;
import exceptions.EmptyArgumentException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RelationsPanel extends GridPane {

    //link naar controller en main
    private DomeinController controller;
    private MainPanel main;

    //test of monster is geselecteerd
    private int monsterTest = -1;

    //alle grafische onderdelen
    private Label cblbl;
    private Label treasurelbl;
    private Label monsterTreasurelbl;

    private ComboBox monstercb;

    private Button remove;
    private Button add;

    private VBox monsterimg;
    //private VBox treasures;

    //private VBox monsterTreasures;
    private ListView<GridPane> treasureListView;
    private ListView<GridPane> monsterTreasureListView;

    //alle lijsten
    private List<Monster> monsterList;
    private List<Treasure> treasureList;
    private List<Treasure> monsterTreasureList;

    //voert GUI uit
    /**
     * RelationsPanel constructor, creates the panel for adding and removing
     * links between monsters and treasures
     *
     * @param main The MainPanel where the MonsterPanel will get attached on.
     * @param dc the DomeinController used for communcation with the persistence layer.
     */
    public RelationsPanel(MainPanel main,DomeinController dc) {
        controller=dc;
        this.main = main;
        buildGui();
    }

    /**
     * Builds the GUI of the RelationsPanel
     */
    private void buildGui() {

        //declareren van alle elementen in de GUI
        //combobox
        monstercb = new ComboBox();
        //de buttons
        add = new Button("Add\ntreasure");
        remove = new Button("Remove\ntreasure");
        //de Labels
        cblbl = new Label("Choose monster:");
        treasurelbl = new Label("All treasures");
        monsterTreasurelbl = new Label("Connected treasures");
        //Lists declareren
        treasureListView = new ListView<>();
        monsterTreasureListView = new ListView<>();

        monsterList = new ArrayList<>();
        treasureList = new ArrayList<>();
        monsterTreasureList = new ArrayList<>();

        //Multiple selection mogelijk maken
        treasureListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        monsterTreasureListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Vbox declareren
        monsterimg = new VBox();

        //de grafische elementen in grid plaatsen
        GridPane pane1 = new GridPane();
        GridPane pane2 = new GridPane();

        VBox chooseMonster = new VBox();
        chooseMonster.getChildren().addAll(cblbl, monstercb);
        chooseMonster.setAlignment(Pos.CENTER_LEFT);
        chooseMonster.setSpacing(10);
        pane1.add(chooseMonster, 0, 0);
        pane1.add(monsterimg, 1, 0);

        VBox buttons = new VBox();
        buttons.getChildren().addAll(add, remove);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(25);
        pane2.add(buttons, 1, 0, 1, 2);
        pane2.add(treasurelbl, 0, 0);
        pane2.add(monsterTreasurelbl, 2, 0);
        pane2.add(treasureListView, 0, 1);
        pane2.add(monsterTreasureListView, 2, 1);

        //pane 1 en 2 toevoegen aan hoofdpane
        add(pane1, 0, 0);
        add(pane2, 0, 1);
        pane1.setAlignment(Pos.CENTER);
        //pane2.setAlignment(Pos.CENTER);

        //de GUI elementen aanpassen
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        pane2.setPadding(new Insets(10));
        pane2.setHgap(10);
        pane2.setVgap(10);

        pane1.setPadding(new Insets(10));
        pane1.setHgap(50);
        pane1.setVgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        ColumnConstraints col2 = new ColumnConstraints();
        getColumnConstraints().addAll(col1, col2);

        //de lists
        treasureListView.setMinWidth(400);
        treasureListView.setMaxWidth(400);
        monsterTreasureListView.setMinWidth(400);
        monsterTreasureListView.setMaxWidth(400);

        //de buttons
        add.setMaxSize(100, 65);
        remove.setMaxSize(100, 65);

        add.setMinSize(100, 65);
        remove.setMinSize(100, 65);

        //de combobox
        monstercb.setMaxWidth(200);
        monstercb.setMinWidth(200);

        //opstartmethoden
        fillMonsterImageDefault();
        fillComboBox();
        fillTreasureList();

        //actionevents
        //de remove knop de link te verwijderen tussen schat en monster
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean removed = false;                //Er werd een link removed  
                try {
                    //Multiple selection
                    ObservableList<Integer> indices = monsterTreasureListView.getSelectionModel().getSelectedIndices();
                    if (indices.size() == 0) {
                        throw new EmptyArgumentException();
                    }
                    for (int i = 0; i < indices.size(); i++) {
                        int index = indices.get(i);
                        if (controller.removeTreasureFromMonster(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()), monsterTreasureList.get(index)) && !removed) {
                            index--;
                            removed = true;
                        }
                    }
                    if (removed) {
                        //Pas hier aanpassen want anders verandert size van monsterTreasureList
                        fillMonsterTreasureList(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()));
                        main.setMessage("Link has been destroyed succesfully!");
                        main.setMessageColor(Color.BLACK);
                    }
                    //Single selection
                    /*int index = monsterTreasureListView.getSelectionModel().getSelectedIndex();
                     if (index == -1) {
                     throw new EmptyArgumentException();
                     } else {
                     controller.removeTreasureFromMonster(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()), monsterTreasureList.get(index));
                     main.setMessage("Link has been destroyed succesfully!");
                     main.setMessageColor(Color.BLACK);
                     fillMonsterTreasureList(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()));
                     }*/
                } catch (EmptyArgumentException eae) {
                    main.setMessage("No treasure is selected from 'connected treasures' list!\nPlease select treasure before removing!");
                    main.setMessageColor(Color.RED);
                }
            }
        }
        );

        //de add knop om shatten en monsters te linken
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean added = false;          //Er werd een link gecreëerd
                    //Multiple selection
                    if (monsterTest == -1) {
                        throw new EmptyArgumentException();
                    }
                    ObservableList<Integer> indices = treasureListView.getSelectionModel().getSelectedIndices();
                    if (indices.size() == 0) {
                        throw new EmptyArgumentException();
                    }
                    for (int i = 0; i < indices.size(); i++) {
                        int index = indices.get(i);
                        try {
                            for (int j = 0; j < monsterTreasureList.size(); j++) {
                                if (monsterTreasureList.get(j).getId() == treasureList.get(index).getId()) {
                                    throw new AlreadyLinkedException();
                                }
                            }
                            if (controller.addTreasureToMonster(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()), treasureList.get(index)) && !added) {
                                added = true;
                            }

                        } catch (AlreadyLinkedException ale) {
                            main.setMessage(ale.getMessage());
                            main.setMessageColor(Color.RED);
                        }

                    }
                    //Message moet eigenlijk slechts eenmaal gezet worden (er wordt hier sowieso minstens 1 schat toegevoegd)
                    if (added) {
                        main.setMessage("Link between monster and treasure(s) has been established!");
                        main.setMessageColor(Color.BLACK);
                        fillMonsterTreasureList(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()));
                    }
                    //Single selection
                    /*int index = treasureListView.getSelectionModel().getSelectedIndex();
                     if (monsterTest == -1) {
                     throw new EmptyArgumentException();
                     }
                     if (index == -1) {
                     throw new EmptyArgumentException();
                     }
                     for (int i = 0; i < monsterTreasureList.size(); i++) {
                     if (monsterTreasureList.get(i).getId() == treasureList.get(index).getId()) {
                     throw new AlreadyLinkedException();
                     }
                     }
                     controller.addTreasureToMonster(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()), treasureList.get(index));
                     fillMonsterTreasureList(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()));
                     main.setMessage("Link between monster and treasure has been established!");
                     main.setMessageColor(Color.BLACK);*/
                } catch (EmptyArgumentException eae) {
                    main.setMessage("No treasure and/or monster is selected from 'all treasures' list and/or the combobox!\nPlease select treasure and/or monster before adding!");
                    main.setMessageColor(Color.RED);
                }
            }
        }
        );

        //monster geselecteerd, update de monstertreasurelist en treasurelist, vul monsterimg
        monstercb.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        //
                        //if (monsterTest == 1) {
                        if (!monstercb.getSelectionModel().isSelected(-1)) {
                            fillMonsterTreasureList(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()));
                            fillMonsterImage(monsterList.get(monstercb.getSelectionModel().getSelectedIndex()));
                            monsterTest = 1;
                        } else {
                            monsterTreasureListView.getItems().clear();
                            monsterTest = -1;
                        }
                        //}
                /*if (monsterTest == -1) {
                         monsterTreasureListView.getItems().clear();
                         }*/
                    }
                }
        );
    }

    //de methoden
    //maakt treasure panes aan om in de lijsten te zetten
    /**
     * Creates gridpane of treasure with all information needed, these panes can
     * be used to display the treasures on the GUI
     *
     * @param treasure Treasure is used to get all needed information from
     * database
     *
     * @return returns a gridpane of the given treasure ready to be displayed
     */
    private GridPane treasurePane(Treasure treasure) {
        GridPane treasurePane = new GridPane();

        Image iPower = new Image(Main.class
                .getResourceAsStream("/images/icons/Sword.png"));
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
        Label id = new Label(" #  " + treasure.getId());

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
        treasurePane.add(avatarimgv,
                0, 1, 1, 2);
        treasurePane.add(id,
                1, 1, 2, 1);
        treasurePane.add(powerimgv,
                3, 1);
        treasurePane.add(power,
                4, 1);
        treasurePane.add(speedimgv,
                5, 1);
        treasurePane.add(speed,
                6, 1);
        treasurePane.add(valueimgv,
                1, 2);
        treasurePane.add(value,
                2, 2);
        treasurePane.add(defenseimgv,
                3, 2);
        treasurePane.add(defense,
                4, 2);
        treasurePane.add(awarenessimgv,
                5, 2);
        treasurePane.add(awareness,
                6, 2);

        return treasurePane;
    }

//vult de monsterTreasureListView
    /**
     * Fills the list of treasures linked to the selected monster with data from
     * database
     *
     * @param monster Monster is needed to get data (=treasures) needed from
     * database
     */
    private void fillMonsterTreasureList(Monster monster) {
        monsterTreasureList.clear();
        monsterTreasureList.addAll(controller.searchAllTreasuresFromMonster(monster));
        List<GridPane> helpList = new ArrayList<>();
        helpList.clear();
        for (int i = 0; i < monsterTreasureList.size(); i++) {
            helpList.add(treasurePane(monsterTreasureList.get(i)));
        }
        monsterTreasureListView.setItems(FXCollections.observableList(helpList));
    }

    //vult de treasureListView
    /**
     * Fills the list of all treasures present in the database
     */
    private void fillTreasureList() {
        treasureList.clear();
        treasureList.addAll(controller.searchAllTreasures());
        List<GridPane> helpList = new ArrayList<>();
        helpList.clear();
        for (int i = 0; i < treasureList.size(); i++) {
            helpList.add(treasurePane(treasureList.get(i)));
        }
        treasureListView.setItems(FXCollections.observableList(helpList));
    }

    //vult de combobox
    /**
     * Fills the ComboBox where you can select a monster with data from database
     */
    private void fillComboBox() {
        //monsterTest = -1;
        monsterList.clear();
        monstercb.getItems().clear();
        monsterList.addAll(controller.searchAllMonsters());
        for (int i = 0; i < monsterList.size(); i++) {
            monstercb.getItems().add(monsterList.get(i));
        }
        monstercb.getSelectionModel().select(null);
        //monsterTest = 1;
        monsterTest = -1;         //Geen monster geselecteerd

    }

    //vult de monsterimg met default, of gegeven monster
    /**
     * Makes the monster appear on screen when selected from ComboBox
     *
     * @param monster Monster is used to get all the needed data
     */
    private void fillMonsterImage(Monster monster) {

        GridPane pane = new GridPane();

        Image iPower = new Image(Main.class
                .getResourceAsStream("/images/icons/Sword.png"));
        Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
        Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
        Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));
        Image iAvatar = new Image(Main.class.getResourceAsStream("/images/monsters/" + monster.getAvatar()));

        ImageView speedimgv = new ImageView(iSpeed);
        ImageView awarenessimgv = new ImageView(iAwareness);
        ImageView defenseimgv = new ImageView(iDefense);
        ImageView powerimgv = new ImageView(iPower);
        ImageView avatarimgv = new ImageView(iAvatar);

        speedimgv.setFitHeight(
                15);
        awarenessimgv.setFitHeight(
                15);
        defenseimgv.setFitHeight(
                15);
        powerimgv.setFitHeight(
                15);
        avatarimgv.setFitHeight(
                100);
        speedimgv.setFitWidth(
                15);
        awarenessimgv.setFitWidth(
                15);
        defenseimgv.setFitWidth(
                15);
        powerimgv.setFitWidth(
                15);
        avatarimgv.setFitWidth(
                100);
        pane.setPadding(
                new Insets(15));
        pane.setHgap(
                10);
        pane.setVgap(
                10);

        Label id = new Label(" #  " + monster.getId());
        Label awareness = new Label("" + monster.getAwareness());
        Label speed = new Label("" + monster.getSpeed());
        Label defense = new Label("" + monster.getDefense());
        Label power = new Label("" + monster.getPower());

        pane.add(avatarimgv,
                0, 0, 4, 1);
        pane.add(id,
                0, 1);
        pane.add(powerimgv,
                0, 2);
        pane.add(power,
                1, 2);
        pane.add(defense,
                1, 3);
        pane.add(defenseimgv,
                0, 3);
        pane.add(speedimgv,
                2, 2);
        pane.add(speed,
                3, 2);
        pane.add(awareness,
                3, 3);
        pane.add(awarenessimgv,
                2, 3);

        monsterimg.getChildren()
                .clear();
        monsterimg.getChildren()
                .add(pane);
    }

    /**
     * Makes the default image appear when no monster is selected
     */
    private void fillMonsterImageDefault() {

        GridPane pane = new GridPane();

        Image iPower = new Image(Main.class
                .getResourceAsStream("/images/icons/Sword.png"));
        Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
        Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
        Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));
        Image iAvatar = new Image(Main.class.getResourceAsStream("/images/icons/default.png"));

        ImageView speedimgv = new ImageView(iSpeed);
        ImageView awarenessimgv = new ImageView(iAwareness);
        ImageView defenseimgv = new ImageView(iDefense);
        ImageView powerimgv = new ImageView(iPower);
        ImageView avatarimgv = new ImageView(iAvatar);

        speedimgv.setFitHeight(
                15);
        awarenessimgv.setFitHeight(
                15);
        defenseimgv.setFitHeight(
                15);
        powerimgv.setFitHeight(
                15);
        avatarimgv.setFitHeight(
                100);
        speedimgv.setFitWidth(
                15);
        awarenessimgv.setFitWidth(
                15);
        defenseimgv.setFitWidth(
                15);
        powerimgv.setFitWidth(
                15);
        avatarimgv.setFitWidth(
                100);
        pane.setPadding(
                new Insets(15));
        pane.setHgap(
                10);
        pane.setVgap(
                10);

        Label id = new Label(" #  ?");
        Label awareness = new Label("?");
        Label speed = new Label("?");
        Label defense = new Label("?");
        Label power = new Label("?");

        pane.add(avatarimgv,
                0, 0, 4, 1);
        pane.add(id,
                0, 1);
        pane.add(powerimgv,
                0, 2);
        pane.add(power,
                1, 2);
        pane.add(defense,
                1, 3);
        pane.add(defenseimgv,
                0, 3);
        pane.add(speedimgv,
                2, 2);
        pane.add(speed,
                3, 2);
        pane.add(awareness,
                3, 3);
        pane.add(awarenessimgv,
                2, 3);

        monsterimg.getChildren()
                .clear();
        monsterimg.getChildren()
                .add(pane);
    }

    /**
     * Can be used to clear and update the lists and combobox in the
     * RelationsPanel with the latest data from database.
     *
     * @author Robin De Haes
     */
    public void reset() //Voor automatische update in monster- en treasurepanel
    {
        //Alles opnieuw clearen en resetten
        fillMonsterImageDefault();
        fillComboBox();
        fillTreasureList();
        monsterTreasureListView.setItems(FXCollections.observableList(new ArrayList<GridPane>()));      //Zichtbare lijst leeg maken
    }
}
