/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.GameProcesses;
import domein.Hero;
import java.io.File;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.setHalignment;

/**
 *
 * @author Robin
 */
public class HeroPanel extends GridPane {

    private MainPanel owner;
    private DomeinController dc;

    private Hero lastHero;          //Last hero to have played the game (for continue purposes)

    private Hero hero;
    private DetailHero detail;
    private ImageView ivAvatar;

    private int count;
    private final int maxCount = 8;

    private Label message;

    private static ChoiceBox cbAvatars;
    private final static File folder = new File("./src/images/heroes/");
    private static File[] listOfFiles = folder.listFiles();

    private TextField txfName;
    private NumberField nfPower, nfDefense, nfSpeed, nfAwareness;
    private Label lblName, lblPower, lblDefense, lblSpeed, lblAwareness, lblAvatar;

    private Button createBtn, continueBtn;

    /**
     * Constructor for the HeroPanel.
     *
     * @param detail DetailHero that will show the information changes of the
     * hero
     * @param owner owner of the HeroPanel (its parent)
     * @param dc DomeinController used for searching the hero that has been used
     * last
     */
    public HeroPanel(DetailHero detail, MainPanel owner, DomeinController dc) {
        this.detail = detail;
        this.owner = owner;
        this.dc = dc;
        hero = new Hero();
        lastHero = null;
        settings();
        buildPane();
    }

    /**
     * Builds the actual HeroPanel and adds the necessary listeners for
     * validation purposes.
     */
    private void buildPane() {
        ivAvatar = new ImageView();         //Geen nut van er een defaultAvatar in te steken want wordt automatisch overschreven bij het vullen van de choicebox en het selecteren van de eerste keuze
        ivAvatar.setFitHeight(80);
        ivAvatar.setFitWidth(80);
        ivAvatar.getStyleClass().add("avatar");

        cbAvatars = fillAvatarChoiceBox();

        add(ivAvatar, 0, 0, 1, 2);
        add(detail, 1, 0, 1, 2);

        add(lblName, 0, 3);
        add(txfName, 1, 3);

        add(lblPower, 0, 4);
        add(nfPower, 1, 4);

        add(lblDefense, 0, 5);
        add(nfDefense, 1, 5);

        add(lblSpeed, 0, 6);
        add(nfSpeed, 1, 6);

        add(lblAwareness, 0, 7);
        add(nfAwareness, 1, 7);

        add(lblAvatar, 0, 8);
        add(cbAvatars, 1, 8, 2, 1);
        add(message, 0, 9, 2, 1);

        add(createBtn, 0, 12, 2, 2);
        add(continueBtn, 0, 14, 2, 2);

        setHalignment(continueBtn, HPos.CENTER);
        setHalignment(createBtn, HPos.CENTER);

        count = 0;            //Aantal stats toegewezen
        setCounter(maxCount);

        //Maximuminvoer voorzien
        txfName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (newValue.length() >= Hero.getMAX_NAME()) {
                    txfName.setText(oldValue);
                }
                if (newValue.length() == 0) {
                    detail.setName(hero.getName());
                } else {
                    detail.setName(txfName.getText());
                }
            }
        });

        nfPower.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                int oldV = Integer.parseInt(oldValue);
                int newV = Integer.parseInt(newValue);

                if (newV > oldV) {
                    if (count < maxCount && newV <= maxCount) {
                        nfPower.setNumber(newV);
                        detail.setPower(newValue);
                        count++;
                        setCounter(maxCount - count);
                    } else {
                        count++;
                        nfPower.setNumber(oldV);
                        detail.setPower(oldValue);
                    }
                    /*
                     Er moet ook rekening gehouden worden met de extra textfieldwijzigingen bij
                     herzetten van de oldValue. Er vindt immers een count-- plaats wanneer je van de nieuwe hogere
                     value teruggaat naar de oudere, lagere value. Die moet je dus opheffen met een count++.
                     Als de value succesvol verhoogt werd, heb je ook een count++ want de stats zijn verhoogd.
                     */
                }
                if (newV < oldV) {
                    if (count > 0 && newV >= 0) {
                        nfPower.setNumber(newV);
                        detail.setPower(newValue);
                        count--;
                        setCounter(maxCount - count);
                    } else {
                        count--;
                        /*
                         count-- moet hier bovenaan staan (voor setNumber), omdat anders het terugzetten van -1 naar 0 niet zal plaatsvinden
                         indien count op maxCount staat, want dan kan de correcte if niet uitgevoerd worden om -1 terug op 0 te zetten
                         aangezien count<maxCount een voorwaarde daarvoor is.
                         */
                        nfPower.setNumber(oldV);
                        detail.setPower(oldValue);
                    }

                }
            }
        });

        nfDefense.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                int oldV = Integer.parseInt(oldValue);
                int newV = Integer.parseInt(newValue);

                if (newV > oldV) {
                    if (count < maxCount && newV <= maxCount) {
                        nfDefense.setNumber(newV);
                        detail.setDefense(newValue);
                        count++;
                        setCounter(maxCount - count);
                    } else {
                        count++;
                        nfDefense.setNumber(oldV);
                        detail.setDefense(oldValue);
                    }
                }
                if (newV < oldV) {
                    if (count > 0 && newV >= 0) {
                        nfDefense.setNumber(newV);
                        detail.setDefense(newValue);
                        count--;
                        setCounter(maxCount - count);
                    } else {
                        count--;
                        nfDefense.setNumber(oldV);
                        detail.setDefense(oldValue);
                    }
                }

            }
        });

        nfSpeed.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                int oldV = Integer.parseInt(oldValue);
                int newV = Integer.parseInt(newValue);

                if (newV > oldV) {
                    if (count < maxCount && newV <= maxCount) {
                        nfSpeed.setNumber(newV);
                        detail.setSpeed(newValue);
                        count++;
                        setCounter(maxCount - count);
                    } else {
                        count++;
                        nfSpeed.setNumber(oldV);
                        detail.setSpeed(oldValue);
                    }
                }
                if (newV < oldV) {
                    if (count > 0 && newV >= 0) {
                        nfSpeed.setNumber(newV);
                        detail.setSpeed(newValue);
                        count--;
                        setCounter(maxCount - count);
                    } else {
                        count--;
                        nfSpeed.setNumber(oldV);
                        detail.setSpeed(oldValue);
                    }
                }

            }
        });

        nfAwareness.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                int oldV = Integer.parseInt(oldValue);
                int newV = Integer.parseInt(newValue);

                if (newV > oldV) {
                    if (count < maxCount && newV <= maxCount) {
                        nfAwareness.setNumber(newV);
                        detail.setAwareness(newValue);
                        count++;
                        setCounter(maxCount - count);
                    } else {
                        count++;
                        nfAwareness.setNumber(oldV);
                        detail.setAwareness(oldValue);
                    }
                }
                if (newV < oldV) {
                    if (count > 0 && newV >= 0) {
                        nfAwareness.setNumber(newV);
                        detail.setAwareness(newValue);
                        count--;
                        setCounter(maxCount - count);
                    } else {
                        count--;
                        nfAwareness.setNumber(oldV);
                        detail.setAwareness(oldValue);
                    }
                }

            }
        });

        cbAvatars.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) {
                if (newVal != null) {
                    setAvatar(newVal);
                }
            }
        }
        );

        createBtn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        if (txfName.getText().equalsIgnoreCase("GOD")) {
                            hero.setName("GOD");
                            hero.setPower(Hero.getMAX());
                            hero.setDefense(Hero.getMAX());
                            hero.setSpeed(Hero.getMAX());
                            hero.setAwareness(Hero.getMAX());
                            owner.initializeHero(hero);
                            owner.getChildren().removeAll(HeroPanel.this);
                            owner.focusGame();
                        }
                        else if (count == maxCount) {
                            String name = txfName.getText();
                            int power = nfPower.getNumber();
                            int defense = nfDefense.getNumber();
                            int speed = nfSpeed.getNumber();
                            int awareness = nfAwareness.getNumber();

                            if (name.length() > 0) //Anders wordt de defaultnaam Player gebruikt
                            {
                                hero.setName(name);
                            }
                            hero.setPower(power);
                            hero.setDefense(defense);
                            hero.setSpeed(speed);
                            hero.setAwareness(awareness);
                            owner.initializeHero(hero);
                            owner.getChildren().removeAll(HeroPanel.this);
                            owner.focusGame();
                        } else {
                            message.getStyleClass().remove("informationMessage");
                            message.getStyleClass().add("errorMessage");
                            message.setText("* Spend your remaining " + (maxCount - count) + " points!");
                        }
                    }
                }
        );

        continueBtn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        owner.continueHero(lastHero);
                        owner.getChildren().removeAll(HeroPanel.this);
                        owner.focusGame();
                    }
                }
        );

        cbAvatars.getSelectionModel().selectFirst();            //Uitvoeren na de listener te adden zodat avatar automatisch wordt geïnitialiseerd
        txfName.requestFocus();
        txfName.selectAll();

        if (!initLastHero()) {
            continueBtn.setDisable(true);
        }
    }

    /**
     * Implements the panels settings.
     */
    private void settings() {
        setMaxWidth(450);
        setMinWidth(450);
        setMinHeight(700);
        setMaxHeight(700);

        getStylesheets().addAll("css/panel.css", "css/UI.css");
        getStyleClass().add("startscreen");

        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        message = new Label();

        lblName = new Label("Name");
        lblName.getStyleClass().addAll("fontWhite");
        lblPower = new Label("Power");
        lblPower.getStyleClass().addAll("fontWhite");
        lblDefense = new Label("Defense");
        lblDefense.getStyleClass().addAll("fontWhite");
        lblSpeed = new Label("Speed");
        lblSpeed.getStyleClass().addAll("fontWhite");
        lblAwareness = new Label("Awareness");
        lblAwareness.getStyleClass().addAll("fontWhite");
        lblAvatar = new Label("Class");
        lblAvatar.getStyleClass().addAll("fontWhite");

        txfName = new TextField(hero.getName());  //Zal defaultnaam Player zijn
        txfName.setPromptText(hero.getName());
        nfPower = new NumberField();
        nfDefense = new NumberField();
        nfSpeed = new NumberField();
        nfAwareness = new NumberField();

        createBtn = new Button("NEW GAME");
        continueBtn = new Button("CONTINUE");

        getStylesheets().add("css/UI.css");
        createBtn.getStyleClass().add("panel-button");
        continueBtn.getStyleClass().add("panel-button");

        createBtn.setMinSize(200, 40);
        createBtn.setMaxSize(200, 40);
        continueBtn.setMinSize(200, 40);
        continueBtn.setMaxSize(200, 40);
    }

    /**
     * Fill and give a ChoiceBox filled with avars.
     *
     * @return the ChoiceBox filled with the found avatars
     */
    private ChoiceBox fillAvatarChoiceBox() {
        ChoiceBox avatars = new ChoiceBox();
        String name;

        for (File avatar : listOfFiles) {
            if (avatar.isFile()) {
                name = avatar.getName();
                if (name.contains("Avatar")) //Enkel de avatars worden toegevoegd
                {
                    avatars.getItems().add(name.substring(0, (int) (name.length() - 6 - 4)));
                }
                //De extensie van de afbeelding moet niet zichtbaar zijn voor de speler (=4 letters)
                //Ook de naam Avatar niet (=6 letters)
            }
        }

        return avatars;
    }

    /**
     * Set the counter in the messagelabel to the specified value.
     *
     * @param counter the new value for the amount of remaining points
     */
    private void setCounter(int counter) {
        message.setText("* " + counter + " points remaining");
        message.getStyleClass().remove("errorMessage");
        message.getStyleClass().add("informationMessage");
    }

    /**
     * Set the avatar
     *
     * @param name
     */
    private void setAvatar(String name) {
        Image newImg = new Image(Main.class.getResourceAsStream("/images/heroes/" + name + "Avatar.gif"));
        //Alle heroes zullen gifs zijn
        ivAvatar.setImage(newImg);
        hero.setAvatar(name + "Avatar.gif");
        owner.setHeroView(name);
    }

    /**
     * Initializes lastHero to the correct value.
     *
     * @return true if there was a previous hero found and initialized, false if
     * not
     */
    public boolean initLastHero() {
        List<Hero> allHeroes = dc.searchAllHeroes();

        if (allHeroes == null) {
            return false;
        }
        for (int i = allHeroes.size() - 1; i >= 0; i--) {
            Hero temp = allHeroes.get(i);
            int round = GameProcesses.getMaxRound();
            if (temp.getRound() <= GameProcesses.getMaxRound() && temp.getAlive()) {
                lastHero = temp;
                return true;
            }
        }
        return false;
    }

    /**
     * Check if continuation of a previous game was possible.
     *
     * @return true is the continuebutton was enabled, false if not
     */
    public boolean continueWasPossible() {
        return !continueBtn.isDisable();
    }
}
