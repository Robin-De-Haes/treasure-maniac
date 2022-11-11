/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.Hero;
import domein.Treasure;
import exceptions.*;
import gui.Dialog.Response;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Panel for building or showing a treasure
 *
 * @author Robin
 */
public class TreasurePanel extends GridPane {

    private DomeinController controller;
    private MainPanel main;                 //Paneel waartoe het TreasurePanel behoort
    private DetailTreasure detail;

    //Default-avatar en icoontjes
    private ImageView ivAvatar, ivPower, ivDefense, ivSpeed, ivAwareness, ivValue;
    private static Image iPower = new Image(Main.class.getResourceAsStream("/images/icons/Sword.png"));
    private static Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
    private static Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
    private static Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));
    private static Image iValue = new Image(Main.class.getResourceAsStream("/images/icons/Value.png"));
    private static Image iDefaultAvatar = new Image(Main.class.getResourceAsStream("/images/icons/default.png"));

    //Labels en tekstvelden voor alle stats
    private TextField txfId, txfName, txfPower, txfDefense, txfSpeed, txfAwareness, txfAvatar, txfValue, txfDescription;
    private Label lblId, lblName, lblPower, lblDefense, lblSpeed, lblAwareness, lblAvatar, lblValue, lblDescription, message;

    //Choicebox met alle mogelijke afbeeldingen erin (static zodat folder niet steeds opnieuw moet aangemaakt worden)
    private ChoiceBox cbAvatars;
    private final static File folder = new File("./src/images/treasures/");
    private static File[] listOfFiles = folder.listFiles();

    /**
     * Constructor for the TreasurePanel that creates the addPane for treasures
     *
     * @param controller The domaincontroller
     * @param detail The detailPane that's added at the top of the addPane
     * @param main The mainPanel where the treasurerPanel will get attached to
     */
    public TreasurePanel(DomeinController controller, DetailTreasure detail, MainPanel main) //DetailTreasure details,
    {
        this.controller = controller;
        this.detail = detail;
        this.main = main;
        settings();
        buildAddPane();
    }

    /**
     * Constructor for the TreasurePanel that creates the panels for showing,
     * editing and deleting treasures
     *
     * @param controller The domaincontroller
     * @param treasure Treasure that will be displayed on the panel
     * @param main The mainPanel where the treasurePanel will get attached to
     */
    public TreasurePanel(DomeinController controller, Treasure treasure, MainPanel main) {
        this.controller = controller;
        this.main = main;
        settings();
        buildTreasurePane(treasure);
    }

    /**
     * Initializes the general settings for a TreasurePanel.
     */
    private void settings() {
        //Border, padding en andere instellingen
        setStyle("-fx-border: 4px solid; -fx-border-color: #9a9a9a;");
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        //Labels initialiseren
        lblId = new Label("ID");
        lblName = new Label("Name");
        lblValue = new Label("Value");
        lblDescription = new Label("Description");
        lblPower = new Label("Power");
        lblDefense = new Label("Defense");
        lblSpeed = new Label("Speed");
        lblAwareness = new Label("Awareness");
        lblAvatar = new Label("Avatar");

        //Textfields initialiseren
        txfName = new TextField();
        txfValue = new TextField();
        txfDescription = new TextField();
        txfPower = new TextField();
        txfDefense = new TextField();
        txfSpeed = new TextField();
        txfAwareness = new TextField();
        txfAvatar = new TextField();
    }

    /**
     * Creates a choicebox and fills it with available treasure-images
     *
     * @return the choicebox filled with treasure-images
     */
    private ChoiceBox fillAvatarChoiceBox() {
        ChoiceBox avatars = new ChoiceBox();

        for (File avatar : listOfFiles) {
            if (avatar.isFile()) {
                avatars.getItems().add(avatar.getName());
            }
        }
        return avatars;
    }

    /**
     * Build the addPane for creating treasures. This will be used in
     * {@link #TreasurePanel(DomeinController, DetailTreasure, MainPanel)}
     */
    private void buildAddPane() {
        ivAvatar = new ImageView(iDefaultAvatar);
        ivAvatar.setFitHeight(80);
        ivAvatar.setFitWidth(80);

        txfId = new TextField("Auto-generated");
        txfId.setDisable(true);                             //Geen invoer mogelijk want ID kan niet zelf gekozen worden, database geeft een ID
        txfId.setStyle("-fx-background-color: #d3d3d3");

        //Alle grenswaarden en voorwaarden voor invoer vermelden
        txfName.setPromptText("Up to " + Treasure.getMAX_NAME() + " characters");
        txfValue.setPromptText("Positive integer under " + Treasure.getMAX_VALUE());
        txfDescription.setPromptText("Up to " + Treasure.getMAX_DESCRIPTION() + " characters");
        txfPower.setPromptText("Between " + Treasure.getMIN() + " and " + Treasure.getMAX());
        txfDefense.setPromptText("Between " + Treasure.getMIN() + " and " + Treasure.getMAX());
        txfSpeed.setPromptText("Between " + Treasure.getMIN() + " and " + Treasure.getMAX());
        txfAwareness.setPromptText("Between " + Treasure.getMIN() + " and " + Treasure.getMAX());
        cbAvatars = fillAvatarChoiceBox();

        final Button addBtn = new Button("Add");

        //Alle elementen toevoegen aan het treasurePanel
        add(ivAvatar, 0, 0, 1, 2);
        add(detail, 1, 0, 1, 2);

        add(lblId, 0, 3);
        add(txfId, 1, 3);

        add(lblName, 0, 4);
        add(txfName, 1, 4);

        add(lblDescription, 0, 5);
        add(txfDescription, 1, 5);

        add(lblPower, 0, 6);
        add(txfPower, 1, 6);

        add(lblDefense, 0, 7);
        add(txfDefense, 1, 7);

        add(lblSpeed, 0, 8);
        add(txfSpeed, 1, 8);

        add(lblAwareness, 0, 9);
        add(txfAwareness, 1, 9);

        add(lblValue, 0, 10);
        add(txfValue, 1, 10);

        add(lblAvatar, 0, 11);
        add(cbAvatars, 1, 11);

        add(addBtn, 1, 15);
        GridPane.setHalignment(addBtn, HPos.CENTER);

        //Bij het drukken op enter wordt er naar het volgende tekstveld gegaan
        txfName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfDescription.requestFocus();
            }
        }
        );

        //Automatische aanpassing detailtreasure
        txfName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setName(txfName.getText());
                if (txfName.getText().length() == 0) {
                    detail.setName("Name");                 //Default-waarde indien tekstveld leeggemaakt wordt
                }
            }
        });

        txfDescription.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfPower.requestFocus();
            }
        }
        );

        txfDescription.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setDescription(txfDescription.getText());
                if (txfDescription.getText().length() == 0) {
                    detail.setDescription("Description");
                }
            }
        });

        txfPower.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfDefense.requestFocus();
            }
        }
        );

        txfPower.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setPower(txfPower.getText());
                if (txfPower.getText().length() == 0) {
                    detail.setPower("-");
                }
            }
        });

        txfDefense.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfSpeed.requestFocus();
            }
        }
        );

        txfDefense.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setDefense(txfDefense.getText());
                if (txfDefense.getText().length() == 0) {
                    detail.setDefense("-");
                }
            }
        });

        txfSpeed.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfAwareness.requestFocus();
            }
        }
        );

        txfSpeed.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setSpeed(txfSpeed.getText());
                if (txfSpeed.getText().length() == 0) {
                    detail.setSpeed("-");
                }
            }
        });

        txfAwareness.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfValue.requestFocus();

            }
        }
        );

        txfAwareness.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setAwareness(txfAwareness.getText());
                if (txfAwareness.getText().length() == 0) {
                    detail.setAwareness("-");
                }
            }
        });

        txfValue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cbAvatars.requestFocus();
                cbAvatars.show();
            }
        }
        );

        txfValue.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setValue(txfValue.getText());
                if (txfValue.getText().length() == 0) {
                    detail.setValue("-");
                }
            }
        });

        //Aanpassing image naar geselecteerde avatar
        cbAvatars.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) {
                if (newVal != null) {
                    Image newImg = new Image(Main.class.getResourceAsStream("/images/treasures/" + newVal));
                    ivAvatar.setImage(newImg);
                }

                addBtn.requestFocus();
            }
        }
        );

        cbAvatars.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t
                    ) {
                        KeyCode keyCode = t.getCode();
                        if (keyCode == KeyCode.ENTER) {
                            addBtn.requestFocus();
                        }
                    }
                }
        );

        txfDescription.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (newValue.length() >= Treasure.getMAX_DESCRIPTION()) {
                    txfDescription.setText(oldValue);
                    detail.setDescription(oldValue);
                }
                if (newValue.length() == 0) {
                    detail.setDescription("Description");
                }
            }
        });

        //Automatische selectie van tekst bij klikken in venster (Misschien overbodig en weg te laten)
        /*txfName.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfName.selectAll();
         }
         });

         txfDescription.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfDescription.selectAll();
         }
         });

         txfPower.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfPower.selectAll();
         }
         });

         txfDefense.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfDefense.selectAll();
         }
         });

         txfSpeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfSpeed.selectAll();
         }
         });
         txfAwareness.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfAwareness.selectAll();
         }
         });

         txfValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfValue.selectAll();
         }
         });*/
        //Bij het klikken op de addBtn wordt bij geldige waarden een treasure gecreëerd
        addBtn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Treasure treasure = new Treasure();

                        String name = txfName.getText();
                        String valueS = txfValue.getText();
                        String description = txfDescription.getText();
                        String powerS = txfPower.getText();
                        String defenseS = txfDefense.getText();
                        String speedS = txfSpeed.getText();
                        String awarenessS = txfAwareness.getText();
                        String avatar = null;
                        if (!cbAvatars.getSelectionModel().isSelected(-1)) {
                            avatar = cbAvatars.getSelectionModel().selectedItemProperty().getValue().toString();
                        }
                        //Geselecteerde avatar nemen, -1 wijst op geen avatar geselecteerd en dan is avatar=null

                        int power, defense, speed, awareness, value;

                        boolean exceptionOccurred = false;

                        //Elk tekstveld word afzonderlijk op exceptions getest zodat enkel de foutieve tekstvelden
                        //leeg gemaakt worden.
                        //De error message is die van de eerst tegengekomen exception
                        try {
                            treasure.setName(name);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfName.setText("");
                            detail.setName("Name");
                            exceptionOccurred = true;
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            txfName.requestFocus();
                        }

                        try {
                            if (valueS.length() == 0) {
                                throw new EmptyArgumentException();
                            }
                            value = Integer.parseInt(valueS);
                            treasure.setValue(value);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfValue.setText("");
                            detail.setValue("-");
                            if (!exceptionOccurred) {
                                exceptionOccurred = true;
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                txfValue.requestFocus();
                            }
                        } catch (NumberFormatException nfe) {
                            txfValue.setText("");
                            detail.setValue("-");
                            if (!exceptionOccurred) {
                                txfValue.requestFocus();
                                main.setMessage("All stats have to be integers!");
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        }

                        try {
                            treasure.setDescription(description);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfDescription.setText("");
                            detail.setDescription("Description");
                            if (!exceptionOccurred) {
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                txfDescription.requestFocus();
                                exceptionOccurred = true;
                            }
                        }

                        try {
                            if (powerS.length() == 0) {
                                throw new EmptyArgumentException();
                            }
                            power = Integer.parseInt(powerS);
                            treasure.setPower(power);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfPower.setText("");
                            detail.setPower("-");
                            if (!exceptionOccurred) {
                                txfPower.requestFocus();
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        } catch (NumberFormatException nfe) {           //Geen geheel getal ingevuld
                            txfPower.setText("");
                            detail.setPower("-");
                            if (!exceptionOccurred) {
                                txfPower.requestFocus();
                                main.setMessage("All stats have to be integers!");
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        }

                        try {
                            if (defenseS.length() == 0) {
                                throw new EmptyArgumentException();
                            }
                            defense = Integer.parseInt(defenseS);
                            treasure.setDefense(defense);
                        } catch (EmptyArgumentException | OutOfRangeException e) {

                            txfDefense.setText("");
                            detail.setDefense("-");
                            if (!exceptionOccurred) {
                                txfDefense.requestFocus();
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        } catch (NumberFormatException nfe) {
                            txfDefense.setText("");
                            detail.setDefense("-");
                            if (!exceptionOccurred) {
                                txfDefense.requestFocus();
                                main.setMessage("All stats have to be integers!");
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        }

                        try {

                            if (speedS.length() == 0) {
                                throw new EmptyArgumentException();
                            }
                            speed = Integer.parseInt(speedS);
                            treasure.setSpeed(speed);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfSpeed.setText("");
                            detail.setSpeed("-");
                            if (!exceptionOccurred) {
                                txfSpeed.requestFocus();
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        } catch (NumberFormatException nfe) {
                            txfSpeed.setText("");
                            detail.setSpeed("-");
                            if (!exceptionOccurred) {
                                main.setMessage("All stats have to be integers!");
                                main.setMessageColor(Color.RED);
                                txfSpeed.requestFocus();
                                exceptionOccurred = true;
                            }
                        }

                        try {

                            if (awarenessS.length() == 0) {
                                throw new EmptyArgumentException();
                            }
                            awareness = Integer.parseInt(awarenessS);
                            treasure.setAwareness(awareness);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfAwareness.setText("");
                            detail.setAwareness("-");
                            if (!exceptionOccurred) {
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                txfAwareness.requestFocus();
                                exceptionOccurred = true;
                            }
                        } catch (NumberFormatException nfe) {
                            txfAwareness.setText("");
                            detail.setAwareness("-");
                            if (!exceptionOccurred) {
                                main.setMessage("All stats have to be integers!");
                                main.setMessageColor(Color.RED);
                                txfAwareness.requestFocus();
                                exceptionOccurred = true;
                            }
                        }

                        try {
                            treasure.setAvatar(avatar);
                        } catch (ImageNotSelectedException e) {
                            if (!exceptionOccurred) {
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                cbAvatars.requestFocus();
                                cbAvatars.show();
                                exceptionOccurred = true;
                            }
                        }

                        if (!exceptionOccurred) {           //Als er nergens een exception tegengekomen is kan ze toegevoegd worden
                            //Alles wordt op default-waarde gezet
                            txfName.setText("");
                            detail.setName("Name");
                            txfDescription.setText("");
                            detail.setDescription("Description");
                            txfPower.setText("");
                            detail.setPower("-");
                            txfDefense.setText("");
                            detail.setDefense("-");
                            txfSpeed.setText("");
                            detail.setSpeed("-");
                            txfAwareness.setText("");
                            detail.setAwareness("-");
                            txfValue.setText("");
                            detail.setValue("-");
                            cbAvatars.getSelectionModel().clearSelection();
                            ivAvatar.setImage(iDefaultAvatar);

                            if (!controller.isNewTreasure(treasure)) {          //Als een schat reeds bestaat in de database wordt ze niet toegevoegd
                                main.setMessage("Treasure already exists. Duplicates aren't allowed!");
                                main.setMessageColor(Color.RED);
                            } else if (controller.addTreasure(treasure)) {
                                main.setMessage("Treasure has been added!");
                                main.setMessageColor(Color.GREEN);
                                List<Treasure> treasures = controller.searchAllTreasures(); //Enkel nodig om correct id te verkrijgen (ook nodig voor delete-methode)
                                //TreasurePanel met nieuwe treasure creëren en toevoegen
                                //main.getTreasureFlowPanel().getChildren().addAll(new TreasurePanel(controller, treasures.get(treasures.size() - 1), main));     //Enkel nodig indien ID ook correct moet worden meegegeven 
                                main.addToFlowPanel(new TreasurePanel(controller, treasures.get(treasures.size() - 1), main));
                                //relationsPanel automatisch updaten
                                main.resetRelationsPanel();
                            }
                        }
                    }
                }
        );
    }

    /**
     * Creates a TreasurePanel to be added to the treasures overview flowpanel,
     * will be used in {@link #TreasurePanel(domein.DomeinController, domein.Treasure, gui.MainPanel)
     * }
     *
     * @param treasure The treasure that will be displayed
     */
    private void buildTreasurePane(final Treasure treasure) {        //final want wordt gebruikt in inner classes
        //Vaak gebruikte attributen van treasure
        final int id = treasure.getId();
        final String avatar = treasure.getAvatar();

        //Breedte vastzetten van 1 vierkantje zodat ze allen even groot zijn
        this.setMinWidth(460);
        this.setMaxWidth(460);

        //Icoontjes
        ivPower = new ImageView(iPower);
        ivPower.setFitHeight(24);
        ivPower.setFitWidth(24);

        ivDefense = new ImageView(iDefense);
        ivDefense.setFitHeight(24);
        ivDefense.setFitWidth(24);

        ivSpeed = new ImageView(iSpeed);
        ivSpeed.setFitHeight(24);
        ivSpeed.setFitWidth(24);

        ivAwareness = new ImageView(iAwareness);
        ivAwareness.setFitHeight(24);
        ivAwareness.setFitWidth(24);

        ivValue = new ImageView(iValue);
        ivValue.setFitHeight(24);
        ivValue.setFitWidth(24);

        //Correcte avatar tonen
        Image icon = new Image(Main.class.getResourceAsStream("/images/treasures/" + avatar));
        ivAvatar = new ImageView(icon);
        ivAvatar.setFitHeight(80);
        ivAvatar.setFitWidth(80);

        //lblId niet meer vertoond in laatste versie
        lblId = new Label("[#" + String.valueOf(treasure.getId()) + "]");

        //Vertonen van alle waarden van meegegeven treasure
        lblName = new Label(treasure.getName());
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblDescription = new Label("\"" + format(treasure.getDescription()) + "\"");
        lblDescription.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
        lblPower = new Label(String.valueOf(treasure.getPower()), ivPower);
        lblDefense = new Label(String.valueOf(treasure.getDefense()), ivDefense);
        lblSpeed = new Label(String.valueOf(treasure.getSpeed()), ivSpeed);
        lblAwareness = new Label(String.valueOf(treasure.getAwareness()), ivAwareness);
        lblValue = new Label(String.valueOf(treasure.getValue()), ivValue);
        lblAvatar = new Label(avatar);
        cbAvatars = fillAvatarChoiceBox();

        //Juiste item selecteren
        final List items = cbAvatars.getItems();
        for (int i = 0; i < items.size(); i++) //Of misschien beter gewoon geselecteerde item meegeven aan buildTreasurePane
        {
            if (items.get(i).toString().equals(avatar)) {
                cbAvatars.getSelectionModel().select(items.get(i));
            }
        }

        //Tekstvelden zijn oorspronkelijk onzichtbaar en worden vertoond bij klikken op modify treasure
        txfName.setOpacity(0);
        cbAvatars.setOpacity(0);
        txfPower.setOpacity(0);
        txfSpeed.setOpacity(0);
        txfAwareness.setOpacity(0);
        txfDefense.setOpacity(0);
        txfValue.setOpacity(0);
        txfDescription.setOpacity(0);

        //Icoontjes in tekstvelden vertonen als background en beginnen op inspringing na icoontjes via padding
        txfPower.setMaxWidth(65);
        txfPower.setMinWidth(65);
        txfPower.setMinHeight(24);
        txfPower.setStyle("-fx-background-image:url('images/icons/Sword.png');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-padding: 0 0 0 30px;");
        txfDefense.setMaxWidth(65);
        txfDefense.setMinWidth(65);
        txfDefense.setMinHeight(24);
        txfDefense.setStyle("-fx-background-image:url('images/icons/Shield.png');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-padding: 0 0 0 30px;");
        txfSpeed.setMaxWidth(65);
        txfSpeed.setMinWidth(65);
        txfSpeed.setMinHeight(24);
        txfSpeed.setStyle("-fx-background-image:url('images/icons/Speed.png');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-padding: 0 0 0 30px;");
        txfAwareness.setMaxWidth(65);
        txfAwareness.setMinWidth(65);
        txfAwareness.setMinHeight(24);
        txfAwareness.setStyle("-fx-background-image:url('images/icons/Awareness.png');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-padding: 0 0 0 30px;");
        txfValue.setMaxWidth(75);
        txfValue.setMinWidth(75);
        txfValue.setMinHeight(24);
        txfValue.setStyle("-fx-background-image:url('images/icons/Value.png');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-padding: 0 0 0 30px;");

        //Ruimte tussen labels wordt verkregen door textfield iets groter te maken
        /*HBox padding1 = new HBox();
         padding1.setMinWidth(20);
         HBox padding2 = new HBox();
         padding2.setMinWidth(20);*/
        final Button updateBtn = new Button("Modify");
        final Button deleteBtn = new Button("Delete");

        add(ivAvatar, 0, 0, 1, 2);
        add(lblName, 1, 0, 5, 1);
        //add(lblId, 6, 0);           
        add(lblDescription, 1, 1, 5, 1);
        add(lblPower, 1, 2);
        add(lblDefense, 1, 3);
        //add(padding1, 2, 2, 1, 2);
        add(lblSpeed, 3, 2);
        add(lblAwareness, 3, 3);
        //add(padding2, 4, 2, 1, 2);
        add(lblValue, 5, 2);

        //Tekstvelden staan over de labels maar zijn onzichtbaar
        add(txfName, 1, 0, 5, 1);
        add(txfDescription, 1, 1, 5, 1);
        add(txfPower, 1, 2);
        add(txfDefense, 1, 3);
        add(txfSpeed, 3, 2);
        add(txfAwareness, 3, 3);
        add(txfValue, 5, 2);
        add(cbAvatars, 1, 4, 5, 1);

        add(updateBtn, 0, 2);
        setHalignment(updateBtn, HPos.CENTER);

        add(deleteBtn, 0, 3);
        setHalignment(deleteBtn, HPos.CENTER);

        //Naar volgend tekstvenster gaan bij het drukken op enter
        txfName.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        txfDescription.requestFocus();
                    }
                }
        );

        txfDescription.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        txfPower.requestFocus();
                    }
                }
        );

        txfPower.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        txfDefense.requestFocus();
                    }
                }
        );

        txfDefense.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        txfSpeed.requestFocus();
                    }
                }
        );

        txfSpeed.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        txfAwareness.requestFocus();
                    }
                }
        );

        txfAwareness.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        txfValue.requestFocus();
                    }
                }
        );

        txfValue.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        cbAvatars.requestFocus();
                        cbAvatars.show();
                    }
                }
        );

        //Afbeelding aanpassen bij selecteren nieuwe avatar
        cbAvatars.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue
            ) {
                String temp = cbAvatars.getItems().get(newValue.intValue()).toString();
                Image newImg = new Image(Main.class.getResourceAsStream("/images/treasures/" + temp));
                ivAvatar.setImage(newImg);
                updateBtn.requestFocus();
            }
        }
        );

        cbAvatars.setOnKeyPressed(
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t
                    ) {
                        KeyCode keyCode = t.getCode();
                        if (keyCode == KeyCode.ENTER) {
                            updateBtn.requestFocus();
                        }
                    }
                }
        );

        txfDescription.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (newValue.length() >= Treasure.getMAX_DESCRIPTION()) {
                    txfDescription.setText(oldValue);
                    lblDescription.setText(format(oldValue));
                }
            }
        });

        //Alle tekst selecteren bij muisklik in tekstveld
   /*     txfName.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfName.selectAll();
         }
         });

         txfDescription.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfDescription.selectAll();
         }
         });

         txfPower.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfPower.selectAll();
         }
         });

         txfDefense.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfDefense.selectAll();
         }
         });

         txfSpeed.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfSpeed.selectAll();
         }
         });
         txfAwareness.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfAwareness.selectAll();
         }
         });

         txfValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfValue.selectAll();
         }
         });
         */
        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Treasure oldTreasure = new Treasure(treasure);      //Originele treasure, gebruikt om naar terug te keren indien duplicaat van bestaande treasure gecreëerd wordt

                if (updateBtn.getText().equals("Modify")) {     //Als de waarden aangepast moeten worden

                    //Tekstvelden bevatten oorspronkelijke waarden
                    txfName.setText(lblName.getText());
                    String temp = lblDescription.getText();
                    temp = deformat(temp.substring(1, temp.length() - 1));
                    txfDescription.setText(temp);       //"" verwijderen om enkel descriptionwaarde te bekomen
                    lblDescription.setOpacity(0);
                    txfValue.setText(lblValue.getText());
                    txfPower.setText(lblPower.getText());
                    txfSpeed.setText(lblSpeed.getText());
                    txfDefense.setText(lblDefense.getText());
                    txfAwareness.setText(lblAwareness.getText());
                    //txfAvatar.setText(lblAvatar.getText());

                    //Tekstvelden worden zichtbaar en staan bovenop de labels zodat die niet meer zichtbaar zijn
                    txfName.setOpacity(1);
                    cbAvatars.setOpacity(1);
                    txfDescription.setOpacity(1);
                    txfValue.setOpacity(1);
                    txfPower.setOpacity(1);
                    txfSpeed.setOpacity(1);
                    txfAwareness.setOpacity(1);
                    txfDefense.setOpacity(1);

                    updateBtn.setText("Update");    //Als nu nog eens op de updatebutton geklikt wordt, zal het de treausre aanpassen met de nieuwe waarden (indien geldig)
                    txfName.requestFocus();
                    main.setMessage("Same rules as adding apply!");
                    main.setMessageColor(Color.GREEN);
                } else {            //Updaten treasure
                    String nameU = txfName.getText();
                    String valueU = txfValue.getText();
                    String descriptionU = txfDescription.getText();
                    String powerU = txfPower.getText();
                    String defenseU = txfDefense.getText();
                    String speedU = txfSpeed.getText();
                    String awarenessU = txfAwareness.getText();
                    String avatarU = cbAvatars.getSelectionModel().getSelectedItem().toString();    //Avatar is geselecteerde avatar
                    int valueUi, powerUi, defenseUi, speedUi, awarenessUi;
                    boolean exceptionOccurred = false;

                    //Alle waarden worden weer apart getest en naar originele waarden hersteld indien exception optrad
                    try {
                        treasure.setName(nameU);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        txfName.setText(lblName.getText());
                        exceptionOccurred = true;
                        main.setMessage(e.getMessage());
                        main.setMessageColor(Color.RED);
                        txfName.requestFocus();
                    }

                    try {
                        if (valueU.length() == 0) {
                            throw new EmptyArgumentException();
                        }
                        valueUi = Integer.parseInt(valueU);
                        treasure.setValue(valueUi);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        txfValue.setText(lblValue.getText());
                        if (!exceptionOccurred) {
                            exceptionOccurred = true;
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            txfValue.requestFocus();
                        }
                    } catch (NumberFormatException nfe) {
                        txfValue.setText(lblValue.getText());
                        if (!exceptionOccurred) {
                            txfValue.requestFocus();
                            main.setMessage("All stats have to be integers!");
                            main.setMessageColor(Color.RED);
                            exceptionOccurred = true;
                        }
                    }

                    try {
                        treasure.setDescription(descriptionU);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        String temp = lblDescription.getText();
                        txfDescription.setText(format(temp.substring(1, temp.length() - 1)));
                        if (!exceptionOccurred) {
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            txfDescription.requestFocus();
                            exceptionOccurred = true;
                        }
                    }

                    try {
                        if (powerU.length() == 0) {
                            throw new EmptyArgumentException();
                        }
                        powerUi = Integer.parseInt(powerU);
                        treasure.setPower(powerUi);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        txfPower.setText(lblPower.getText());
                        if (!exceptionOccurred) {
                            txfPower.requestFocus();
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            exceptionOccurred = true;
                        }
                    } catch (NumberFormatException nfe) {

                        txfPower.setText(lblPower.getText());
                        if (!exceptionOccurred) {
                            txfPower.requestFocus();
                            main.setMessage("All stats have to be integers!");
                            main.setMessageColor(Color.RED);
                            exceptionOccurred = true;
                        }
                    }

                    try {

                        if (defenseU.length() == 0) {
                            throw new EmptyArgumentException();
                        }
                        defenseUi = Integer.parseInt(defenseU);
                        treasure.setDefense(defenseUi);
                    } catch (EmptyArgumentException | OutOfRangeException e) {

                        txfDefense.setText(lblDefense.getText());
                        if (!exceptionOccurred) {
                            txfDefense.requestFocus();
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            exceptionOccurred = true;
                        }
                    } catch (NumberFormatException nfe) {
                        txfDefense.setText(lblDefense.getText());
                        if (!exceptionOccurred) {
                            txfDefense.requestFocus();
                            main.setMessage("All stats have to be integers!");
                            main.setMessageColor(Color.RED);
                            exceptionOccurred = true;
                        }
                    }

                    try {

                        if (speedU.length() == 0) {
                            throw new EmptyArgumentException();
                        }
                        speedUi = Integer.parseInt(speedU);
                        treasure.setSpeed(speedUi);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        txfSpeed.setText(lblSpeed.getText());
                        if (!exceptionOccurred) {
                            txfSpeed.requestFocus();
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            exceptionOccurred = true;
                        }
                    } catch (NumberFormatException nfe) {
                        txfSpeed.setText(lblSpeed.getText());
                        if (!exceptionOccurred) {
                            main.setMessage("All stats have to be integers!");
                            main.setMessageColor(Color.RED);
                            txfSpeed.requestFocus();
                            exceptionOccurred = true;
                        }
                    }

                    try {
                        if (awarenessU.length() == 0) {
                            throw new EmptyArgumentException();
                        }
                        awarenessUi = Integer.parseInt(awarenessU);
                        treasure.setAwareness(awarenessUi);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        txfAwareness.setText(lblAwareness.getText());
                        if (!exceptionOccurred) {
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            txfAwareness.requestFocus();
                            exceptionOccurred = true;
                        }
                    } catch (NumberFormatException nfe) {
                        txfAwareness.setText(lblAwareness.getText());
                        if (!exceptionOccurred) {
                            main.setMessage("All stats have to be integers!");
                            main.setMessageColor(Color.RED);
                            txfAwareness.requestFocus();
                            exceptionOccurred = true;
                        }
                    }

                    /* try {*/           //kan geen exception meer optreden want werkt nu met choicebox gevuld met geldige waarden
                    treasure.setAvatar(avatarU);
                    Image newImg = new Image(Main.class.getResourceAsStream("/images/treasures/" + avatarU));
                    ivAvatar.setImage(newImg);          //Avatar-image aanpassen bij selecteren nieuwe avatar
                    /* } catch (EmptyArgumentException | OutOfRangeException | InvalidImageException | ImageNotFoundException e) {
                     if (!exceptionOccurred) {
                     main.setMessage(e.getMessage());
                     main.getMessage().setTextFill(Color.RED);
                     txfAvatar.requestFocus();
                     exceptionOccurred = true;
                     }
                     //txfAvatar.setText(lblAvatar.getText());
                     cbAvatars.getSelectionModel().selectFirst();
                     }*/
                    if (!exceptionOccurred) {//Als alle ingevulde waarden geldig waren
                        //Als de aangepaste schat niet reeds voorkomt in de database of als de schat ongewijzigd is
                        if ((controller.isNewTreasure(treasure) || treasure.equals(oldTreasure)) && controller.updateTreasure(treasure)) {
                            if (!lblAvatar.getText().equals(avatarU)) {
                                newImg = new Image(Main.class.getResourceAsStream("/images/treasures/" + avatarU));
                                ivAvatar.setImage(newImg);
                                //Als nieuwe avatar niet de originele avatar is, wordt de image gewijzigd
                            }

                            //Alle labels worden op de nieuwe waarden gezet
                            lblName.setText(txfName.getText());
                            lblDescription.setText("\"" + format(txfDescription.getText()) + "\"");
                            lblValue.setText(txfValue.getText());
                            lblPower.setText(txfPower.getText());
                            lblSpeed.setText(txfSpeed.getText());
                            lblDefense.setText(txfDefense.getText());
                            lblAwareness.setText(txfAwareness.getText());
                            lblAvatar.setText(avatarU);

                            txfName.setOpacity(0);
                            cbAvatars.setOpacity(0);
                            txfDescription.setOpacity(0);
                            txfValue.setOpacity(0);
                            txfPower.setOpacity(0);
                            txfSpeed.setOpacity(0);
                            txfAwareness.setOpacity(0);
                            txfDefense.setOpacity(0);
                            lblDescription.setOpacity(1);

                            main.setMessage("Treasure has been updated!");
                            main.setMessageColor(Color.GREEN);
                            updateBtn.setText("Modify");
                            //RelationsPanel wordt upgedatet
                            main.resetRelationsPanel();

                            /*Overbodig, want wordt bij klikken op modify automatisch overschreven
                             txfName.setText("");
                             txfDescription.setText("");
                             txfValue.setText("");
                             txfPower.setText("");
                             txfSpeed.setText("");
                             txfDefense.setText("");
                             txfAwareness.setText("");*/
                        } else if (!controller.isNewTreasure(treasure)) { //Als schat al voorkomt in database
                            main.setMessage("Treasure already exists. Duplicates aren't allowed!");
                            main.setMessageColor(Color.RED);

                            //Alles wordt weer op de originele waarden gezet
                            treasure.copyValues(oldTreasure);

                            txfName.setText(treasure.getName());
                            txfDescription.setText(treasure.getDescription());
                            txfValue.setText("" + treasure.getValue());
                            txfPower.setText("" + treasure.getPower());
                            txfSpeed.setText("" + treasure.getSpeed());
                            txfDefense.setText("" + treasure.getDefense());
                            txfAwareness.setText("" + treasure.getAwareness());

                            //Afbeelding wordt op origineel gezet indien dat niet reeds het geval is
                            if (!treasure.getAvatar().equals(avatarU)) {
                                for (int i = 0; i < items.size(); i++) //Of beter gewoon geselecteerde item meegeven aan buildTreasurePane
                                {
                                    if (items.get(i).toString().equals(treasure.getAvatar())) {
                                        cbAvatars.getSelectionModel().select(items.get(i));
                                    }
                                }
                                newImg = new Image(Main.class.getResourceAsStream("/images/treasures/" + treasure.getAvatar()));
                                ivAvatar.setImage(newImg);
                            }
                        }
                    }
                }
            }
        }
        );

        //final TreasurePanel pane = this;            //Nodig voor gebruik van dit TreasurePanel in inner class voor werking deleteButton
        //outerclass.this in de plaats gebruiken
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean succes = false;
                int unconnectedToHero = controller.isUnconnectedToHeroes(id);
                int unconnectedToMonsters = controller.isUnconnectedToMonsters(id);

                Response answer = null;

                if (unconnectedToMonsters == 1 && unconnectedToHero == 1) {     //Als treasure niet met een monster is verbonden kan ze gewoon verwijderd worden
                    succes = controller.deleteTreasure(id);
                } else if (unconnectedToHero == 0 && unconnectedToMonsters == 0) {
                    answer = Dialog.showConfirmationDialog(null, "Treasure is still connected with monster(s) and hero(es)!\n"
                            + "Do you want to delete it anyway?", "Break all links?");
                } else if (unconnectedToHero == 0) {
                    answer = Dialog.showConfirmationDialog(null, "Treasure is still connected with hero(es)!\n"
                            + "Do you want to delete it anyway?", "Break all links?");
                } else if (unconnectedToMonsters == 0) {
                    //Als treasure wel met een monster is verbonden wordt de gebruiker eerst op de hoogte gebracht waarna hij kan kiezen om die al dan niet te verwijderen
                    //De links tussen beide worden dan automatisch verbroken
                    answer = Dialog.showConfirmationDialog(null, "Treasure is still connected with monster(s)!\n"
                            + "Do you want to delete it anyway?", "Break all links?");
                }
                if (answer == Response.YES) {
                    succes = controller.deleteTreasure(id);
                }

                if (succes) {
                    main.setMessage("Treasure has been deleted!");
                    main.setMessageColor(Color.GREEN);
                    //Overeenkomstig paneel wordt verwijderd indien treasure succesvol verwijderd werd
                    //main.getTreasureFlowPanel().getChildren().removeAll(pane);
                    main.removeFromFlowPanel(TreasurePanel.this);
                    //relationsPanel wordt upgedatet
                    main.resetRelationsPanel();
                }
            }
        }
        );
    }

    /**
     * Formats a description by starting on a new line when needed.
     *
     * @param description sentence that needs to be formatted
     * @return the formatted description
     */
    private String format(String description) {
        int line = Treasure.getMAX_DESCRIPTION() / 2;
        String temp = description;
        String firstHalf;
        String secondHalf;
        if (temp.length() > line) {
            temp = description.substring(0, line);
            int newLine = temp.lastIndexOf(" ");
            if (newLine < 0) {
                firstHalf = description.substring(0, temp.length() - 1) + "-\n";
                secondHalf = " " + description.substring(temp.length() - 1);
            } else {
                firstHalf = description.substring(0, newLine) + "\n";
                secondHalf = " " + description.substring(newLine + 1);
            }
            String extra = "";
            if (secondHalf.length() > line) {
                temp = secondHalf.substring(0, line);
                newLine = temp.lastIndexOf(" ");
                if (newLine == 0) {         //Er staat vooraan een spatie
                    extra = " " + secondHalf.substring(temp.length() - 1);        
                    secondHalf = secondHalf.substring(0, temp.length() - 1) + "-\n";

                } else {
                    extra = " " + secondHalf.substring(newLine + 1, secondHalf.length() - 1);
                    secondHalf = secondHalf.substring(0, newLine) + "\n";
                }

            }
            return firstHalf + secondHalf + extra;
        } else {
            return description;
        }
    }

    /**
     * Removes the added format of the description
     *
     * @param description the description whose format has to be removed
     * @return the deformatted description
     */
    private String deformat(String description) {
        description = description.replace("-\n ", "");
        description = description.replace("\n", "");          
        return description;
    }
}
