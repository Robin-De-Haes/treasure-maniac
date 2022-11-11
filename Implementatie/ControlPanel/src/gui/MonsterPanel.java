/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.Monster;
import exceptions.EmptyArgumentException;
import exceptions.ImageNotSelectedException;
import exceptions.OutOfRangeException;
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
import javafx.scene.text.Text;

/**
 *
 * @author pieterjan
 */
public class MonsterPanel extends GridPane {

    private MainPanel main;
    private DetailMonster detail;
    private DomeinController controller;

    private ImageView ivAvatar, ivPower, ivDefense, ivSpeed, ivAwareness;
    private static Image iDefaultAvatar = new Image(Main.class.getResourceAsStream("/images/icons/default.png"));
    private static Image iPower = new Image(Main.class.getResourceAsStream("/images/icons/Sword.png"));
    private static Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
    private static Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
    private static Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));

    private TextField txfId, txfName, txfPower, txfDefense, txfSpeed, txfAwareness, txfAvatar;
    private ChoiceBox cbAvatars;
    private Label lblId, lblName, lblPower, lblDefense, lblSpeed, lblAwareness, lblAvatar, message;
    //protected Label lblDetailName, lblDetailPower, lblDetailDefense, lblDetailSpeed, lblDetailAwareness;
    private final static File folder = new File("./src/images/monsters/");
    private static File[] listOfFiles = folder.listFiles();

    /**
     * MonsterPanel constructor, creates the addMonsterPanel
     *
     * @param controller The domaincontroller
     * @param detail The detailPane that should be added at the top.
     * @param main The mainPanel where the MonsterPanel will get attached on.
     */
    public MonsterPanel(DomeinController controller, DetailMonster detail, MainPanel main) {
        this.controller = controller;
        this.detail = detail;
        this.main = main;
        settings();
        buildPane();
    }

    /**
     * MonsterPanel constructor, creates the panels for edit/delete
     *
     * @param controller The domaincontroller
     * @param monster Monster used for data to fill the panel
     * @param main The mainPanel where the MonsterPanel will get attached on.
     */
    public MonsterPanel(DomeinController controller, Monster monster, MainPanel main) {
        this.controller = controller;
        this.main = main;
        settings();
        //addPane(monster); // old panel
        monsterView(monster); // new panel
    }

    /**
     * General settings for both Panels
     */
    private void settings() {
        setStyle("-fx-border: 4px solid; -fx-border-color: #9a9a9a;");
        setPadding(new Insets(20));
        setHgap(10);
        setVgap(10);

        lblId = new Label("ID");
        lblName = new Label("Name");
        lblPower = new Label("Power");
        lblDefense = new Label("Defense");
        lblSpeed = new Label("Speed");
        lblAwareness = new Label("Awareness");
        lblAvatar = new Label("Avatar");

        txfName = new TextField();
        txfPower = new TextField();
        txfDefense = new TextField();
        txfSpeed = new TextField();
        txfAwareness = new TextField();
        txfAvatar = new TextField();
    }

    /**
     * Creates the choicebox with available pictures
     *
     * @return the filled choicebox
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
     * Build the Pane to add monsters
     */
    private void buildPane() {
        ivAvatar = new ImageView(iDefaultAvatar);
        ivAvatar.setFitHeight(80);
        ivAvatar.setFitWidth(80);

        txfId = new TextField("Auto-generated");
        txfId.setDisable(true);
        txfId.setStyle("-fx-background-color: #d3d3d3");

        txfName.setPromptText("Up to " + Monster.getMAX_NAME() + " characters");
        txfPower.setPromptText("Between " + Monster.getMIN() + " and " + Monster.getMAX());
        txfDefense.setPromptText("Between " + Monster.getMIN() + " and " + Monster.getMAX());
        txfSpeed.setPromptText("Between " + Monster.getMIN() + " and " + Monster.getMAX());
        txfAwareness.setPromptText("Between " + Monster.getMIN() + " and " + Monster.getMAX());
        cbAvatars = fillAvatarChoiceBox();

        final Button addBtn = new Button("Add");

        add(ivAvatar, 0, 0, 1, 2);
        add(detail, 1, 0, 1, 2);

        add(lblId, 0, 3);
        add(txfId, 1, 3);

        add(lblName, 0, 4);
        add(txfName, 1, 4);

        add(lblPower, 0, 5);
        add(txfPower, 1, 5);

        add(lblDefense, 0, 6);
        add(txfDefense, 1, 6);

        add(lblSpeed, 0, 7);
        add(txfSpeed, 1, 7);

        add(lblAwareness, 0, 8);
        add(txfAwareness, 1, 8);

        add(lblAvatar, 0, 9);
        add(cbAvatars, 1, 9);

        add(addBtn, 1, 15);
        setHalignment(addBtn, HPos.CENTER);

        txfName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txfPower.requestFocus();
            }
        }
        );

        txfName.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                detail.setName(txfName.getText());
                if (txfName.getText().length() == 0) {
                    detail.setName("Name");
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
                cbAvatars.requestFocus();
                cbAvatars.show();
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

        cbAvatars.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal) {
                if (newVal != null) {
                    Image newImg = new Image(Main.class.getResourceAsStream("/images/monsters/" + newVal));
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
        /*
         txfName.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfName.selectAll();
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
         */
        /*
         addBtn.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
         try {
         int ID = 0;
         String name = txfName.getText();
         String powerS = txfPower.getText();
         String defenseS = txfDefense.getText();
         String speedS = txfSpeed.getText();
         String awarenessS = txfAwareness.getText();
         String avatar = cbAvatars.getItems().get(cbAvatars.getSelectionModel().selectedIndexProperty().getValue()).toString();

         int power, defense, speed, awareness;

         if (powerS.length() == 0) {
         throw new EmptyArgumentException();
         }
         power = Integer.parseInt(powerS);

         if (defenseS.length() == 0) {
         throw new EmptyArgumentException();
         }
         defense = Integer.parseInt(defenseS);

         if (speedS.length() == 0) {
         throw new EmptyArgumentException();
         }
         speed = Integer.parseInt(speedS);

         if (awarenessS.length() == 0) {
         throw new EmptyArgumentException();
         }
         awareness = Integer.parseInt(awarenessS);

         Monster monster = new Monster(ID, name, power, defense, speed, awareness, avatar);
         boolean succes = controller.addMonster(monster);
         if (succes) {
         message.setText("Monster has been added!");
         message.setTextFill(Color.GREEN);
         MonsterPanel monsterPanel = new MonsterPanel(controller, monster, main, message);
         main.monsterFlowPanel.getChildren().addAll(monsterPanel);
         }

         // reset Detail label
         detail.lblDetailName.setText("Name");
         detail.lblDetailPower.setText("0");
         detail.lblDetailDefense.setText("0");
         detail.lblDetailSpeed.setText("0");
         detail.lblDetailAwareness.setText("0");
         } catch (EmptyArgumentException | OutOfRangeException | ImageNotSelectedException e) {
         message.setText(e.getMessage());
         message.setTextFill(Color.RED);
         } catch (NumberFormatException nfe) {
         message.setText("All stats have to be integers!");
         message.setTextFill(Color.RED);
         } finally {
         txfName.setText("");
         txfPower.setText("");
         txfDefense.setText("");
         txfSpeed.setText("");
         txfAwareness.setText("");
         cbAvatars.getSelectionModel().selectFirst();
         txfName.requestFocus();
         }
         }
         });
         */
        addBtn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        Monster monster = new Monster();

                        String name = txfName.getText();
                        String powerS = txfPower.getText();
                        String defenseS = txfDefense.getText();
                        String speedS = txfSpeed.getText();
                        String awarenessS = txfAwareness.getText();
                        String avatar = null;
                        if (!cbAvatars.getSelectionModel().isSelected(-1)) {
                            avatar = cbAvatars.getSelectionModel().selectedItemProperty().getValue().toString();
                        }

                        int power, defense, speed, awareness;

                        boolean exceptionOccurred = false;

                        try {
                            monster.setName(name);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfName.setText("");
                            detail.setName("Name");
                            exceptionOccurred = true;
                            main.setMessage(e.getMessage());
                            main.setMessageColor(Color.RED);
                            txfName.requestFocus();
                        }

                        try {
                            if (powerS.length() == 0) {
                                throw new EmptyArgumentException();
                            }
                            power = Integer.parseInt(powerS);
                            monster.setPower(power);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfPower.setText("");
                            detail.setPower("-");
                            if (!exceptionOccurred) {
                                txfPower.requestFocus();
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                exceptionOccurred = true;
                            }
                        } catch (NumberFormatException nfe) {

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
                            monster.setDefense(defense);
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
                            monster.setSpeed(speed);
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
                            monster.setAwareness(awareness);
                        } catch (EmptyArgumentException | OutOfRangeException e) {
                            txfAwareness.setText("");
                            detail.setAwareness("-");
                            if (!exceptionOccurred) {
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);;
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
                            monster.setAvatar(avatar);
                        } catch (ImageNotSelectedException e) {
                            if (!exceptionOccurred) {
                                main.setMessage(e.getMessage());
                                main.setMessageColor(Color.RED);
                                cbAvatars.requestFocus();
                                cbAvatars.show();
                                exceptionOccurred = true;
                            }
                        }
                        if (!exceptionOccurred) {
                            txfName.setText("");
                            detail.setName("Name");
                            txfPower.setText("");
                            detail.setPower("-");
                            txfDefense.setText("");
                            detail.setDefense("-");
                            txfSpeed.setText("");
                            detail.setSpeed("-");
                            txfAwareness.setText("");
                            detail.setAwareness("-");
                            cbAvatars.getSelectionModel().clearSelection();//selectFirst();
                            ivAvatar.setImage(iDefaultAvatar);

                            if (!controller.isNewMonster(monster)) {
                                main.setMessage("Monster already exists. Duplicates aren't allowed!");
                                main.setMessageColor(Color.RED);
                            } else if (controller.addMonster(monster)) {
                                main.setMessage("Monster has been added!");
                                main.setMessageColor(Color.GREEN);
                                List<Monster> monsters = controller.searchAllMonsters(); //Enkel nodig als correct id moet getoond worden
                                //main.getMonsterFlowPanel().getChildren().addAll(new MonsterPanel(controller, monsters.get(monsters.size() - 1), main));
                                // main.getMonsterFlowPanel().getChildren().addAll(new MonsterPanel(controller, monster, main));
                                main.addToFlowPanel(new MonsterPanel(controller, monsters.get(monsters.size() - 1), main));
                                main.resetRelationsPanel();
                            }
                        }
                    }
                }
        );

    }

    /**
     * Creates the monster overview window
     *
     * @param monster The monster that should be showed
     */
    private void monsterView(final Monster monster) {
        final int id = monster.getId();
        final String avatar = monster.getAvatar();

        //Vaste breedte toevoegen
        this.setMinWidth(350);
        this.setMaxWidth(350);

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

        Image icon = new Image(Main.class.getResourceAsStream("/images/monsters/" + monster.getAvatar()));
        ivAvatar = new ImageView(icon);
        ivAvatar.setFitHeight(80);
        ivAvatar.setFitWidth(80);

        lblId = new Label("[#" + String.valueOf(monster.getId()) + "]");
        lblName = new Label(monster.getName());
        //lblId.setFont(Font.font("Arial", 20));
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblPower = new Label(String.valueOf(monster.getPower()), ivPower);
        lblDefense = new Label(String.valueOf(monster.getDefense()), ivDefense);
        lblSpeed = new Label(String.valueOf(monster.getSpeed()), ivSpeed);
        lblAwareness = new Label(String.valueOf(monster.getAwareness()), ivAwareness);
        lblAvatar = new Label(avatar);
        cbAvatars = fillAvatarChoiceBox();

        final List items = cbAvatars.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).toString().equals(lblAvatar.getText())) {
                cbAvatars.getSelectionModel().select(items.get(i));
            }
        }

        txfName.setOpacity(0);
        txfPower.setOpacity(0);
        txfSpeed.setOpacity(0);
        txfAwareness.setOpacity(0);
        txfDefense.setOpacity(0);
        cbAvatars.setOpacity(0);

        txfName.setMinWidth(170);
        txfName.setMaxWidth(170);
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

        HBox padding1 = new HBox();
        padding1.setMinWidth(20);

        final Button btnUpdate = new Button("Modify");
        final Button btnDelete = new Button("Delete");

        /*HBox identification=new HBox();
         identification.getChildren().addAll(lblName, lblId);
         identification.setSpacing(15);*/
        add(ivAvatar, 0, 0);
        add(lblName, 1, 0, 2, 1);
        add(lblPower, 1, 1);
        add(lblDefense, 1, 2);
        //add(padding1, 2, 1, 1, 2);
        add(lblSpeed, 2, 1);
        add(lblAwareness, 2, 2);

        add(txfName, 1, 0, 2, 1);
        add(txfPower, 1, 1);
        add(txfDefense, 1, 2);
        add(txfSpeed, 2, 1);
        add(txfAwareness, 2, 2);
        add(cbAvatars, 1, 3, 2, 1);

        add(btnUpdate, 0, 1);
        setHalignment(btnUpdate, HPos.CENTER);
        add(btnDelete, 0, 2);
        setHalignment(btnDelete, HPos.CENTER);

        txfName.setOnAction(
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
                        cbAvatars.requestFocus();
                        cbAvatars.show();
                    }
                }
        );

        cbAvatars.getSelectionModel()
                .selectedIndexProperty().addListener(new ChangeListener<Number>() {

                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number value, Number newValue
                    ) {
                        String temp = cbAvatars.getItems().get(newValue.intValue()).toString();
                        Image newImg = new Image(Main.class.getResourceAsStream("/images/monsters/" + temp));
                        ivAvatar.setImage(newImg);
                        btnUpdate.requestFocus();
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
                            btnUpdate.requestFocus();
                        }
                    }
                }
        );
        /*
         txfName.setOnMouseClicked(new EventHandler<MouseEvent>() {
         @Override
         public void handle(MouseEvent event
         ) {
         txfName.selectAll();
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
         */
        btnUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Monster oldMonster = new Monster(monster);
                if (btnUpdate.getText().equals("Modify")) {
                    txfName.setText(lblName.getText());
                    txfPower.setText(lblPower.getText());
                    txfSpeed.setText(lblSpeed.getText());
                    txfDefense.setText(lblDefense.getText());
                    txfAwareness.setText(lblAwareness.getText());
                    //txfAvatar.setText(lblAvatar.getText());

                    txfName.setOpacity(1);
                    cbAvatars.setOpacity(1);
                    txfPower.setOpacity(1);
                    txfSpeed.setOpacity(1);
                    txfAwareness.setOpacity(1);
                    txfDefense.setOpacity(1);

                    btnUpdate.setText("Update");
                    txfName.requestFocus();
                    main.setMessage("Same rules as adding apply!");
                    main.setMessageColor(Color.GREEN);
                } else {
                    String nameU = txfName.getText();
                    String powerU = txfPower.getText();
                    String defenseU = txfDefense.getText();
                    String speedU = txfSpeed.getText();
                    String awarenessU = txfAwareness.getText();
                    String avatarU = cbAvatars.getSelectionModel().getSelectedItem().toString();
                    int powerUi, defenseUi, speedUi, awarenessUi;
                    boolean succes = false;
                    boolean exceptionOccurred = false;

                    try {
                        monster.setName(nameU);
                    } catch (EmptyArgumentException | OutOfRangeException e) {
                        txfName.setText(lblName.getText());
                        exceptionOccurred = true;
                        main.setMessage(e.getMessage());
                        main.setMessageColor(Color.RED);
                        txfName.requestFocus();
                    }

                    try {
                        if (powerU.length() == 0) {
                            throw new EmptyArgumentException();
                        }
                        powerUi = Integer.parseInt(powerU);
                        monster.setPower(powerUi);
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
                        monster.setDefense(defenseUi);
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
                        monster.setSpeed(speedUi);
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
                        monster.setAwareness(awarenessUi);
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

                    /* try {*/           //kan geen exception meer optreden
                    monster.setAvatar(avatarU);
                    Image newImg = new Image(Main.class.getResourceAsStream("/images/monsters/" + avatarU));
                    ivAvatar.setImage(newImg);
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
                    if (!exceptionOccurred) {

                        if ((controller.isNewMonster(monster) || monster.equals(oldMonster)) && controller.updateMonster(monster)) {
                            if (!lblAvatar.getText().equals(avatarU)) {
                                newImg = new Image(Main.class.getResourceAsStream("/images/monsters/" + avatarU));
                                ivAvatar.setImage(newImg);
                            }

                            lblName.setText(txfName.getText());
                            lblPower.setText(txfPower.getText());
                            lblSpeed.setText(txfSpeed.getText());
                            lblDefense.setText(txfDefense.getText());
                            lblAwareness.setText(txfAwareness.getText());
                            lblAvatar.setText(avatarU);

                            txfName.setOpacity(0);
                            cbAvatars.setOpacity(0);
                            txfPower.setOpacity(0);
                            txfSpeed.setOpacity(0);
                            txfAwareness.setOpacity(0);
                            txfDefense.setOpacity(0);

                            main.setMessage("Monster has been updated!");
                            main.setMessageColor(Color.GREEN);
                            btnUpdate.setText("Modify");

                            main.resetRelationsPanel();

                        } else if (!controller.isNewMonster(monster)) {
                            main.setMessage("Monster already exists. Duplicates aren't allowed!");
                            main.setMessageColor(Color.RED);

                            monster.copyValues(oldMonster);

                            txfName.setText(monster.getName());
                            txfPower.setText("" + monster.getPower());
                            txfSpeed.setText("" + monster.getSpeed());
                            txfDefense.setText("" + monster.getDefense());
                            txfAwareness.setText("" + monster.getAwareness());

                            //Nog afbeelding aanpassen
                            if (!monster.getAvatar().equals(avatarU)) {
                                for (int i = 0; i < items.size(); i++) //Of beter gewoon geselecteerde item meegeven aan buildTreasurePane
                                {
                                    if (items.get(i).toString().equals(monster.getAvatar())) {
                                        cbAvatars.getSelectionModel().select(items.get(i));
                                    }
                                }

                                newImg = new Image(Main.class.getResourceAsStream("/images/monsters/" + monster.getAvatar()));
                                ivAvatar.setImage(newImg);
                            }
                        }
                    }
                }
            }
        }
        );

        //final MonsterPanel monsterPanel = this;
        btnDelete.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        boolean succes = false;
                        int unconnected = controller.isUnconnectedMonster(id);
                        if (unconnected == 1) {
                            succes = controller.deleteMonster(id);
                        } else if (unconnected == 0) {
                            Dialog.Response answer = Dialog.showConfirmationDialog(null, "Monster still has treasure(s) connected to it!\n"
                                    + "Do you want to delete it anyway?", "Break all links?");
                            if (answer == Dialog.Response.YES) {
                                succes = controller.deleteMonster(id);
                            }
                        }

                        if (succes) {
                            main.setMessage("Monster has been deleted!");
                            main.setMessageColor(Color.GREEN);
                            main.removeFromFlowPanel(MonsterPanel.this);
                            main.resetRelationsPanel();
                        }
                    }
                }
        );
        /*  btnDelete.setOnAction(
         new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event
         ) {
         boolean succes = controller.deleteMonster(Id);     //eerst db leegmaken

         if (succes) {
         message.setText("Monster has been deleted!");
         message.setTextFill(Color.GREEN);
         main.monsterFlowPanel.getChildren().removeAll(monsterPanel);
         }
         }
         }
         );*/
    }

    /**
     * Builds the Pane to edit/delete monsters
     *
     * @param monster the monster to be used
     * @deprecated
     */
    private void addPane(Monster monster) {
        final Text txtId, txtName, txtPower, txtDefense, txtSpeed, txtAwareness, txtAvatar;
        final int id = monster.getId();
        String name = monster.getName();
        int power = monster.getPower();
        int defense = monster.getDefense();
        int speed = monster.getSpeed();
        int awareness = monster.getAwareness();
        String avatar = monster.getAvatar();
        Image icon = new Image(Main.class
                .getResourceAsStream("/images/monsters/" + avatar));
        ivAvatar = new ImageView(icon);

        txtId = new Text("(#" + id + ")");

        txtId.setStyle(
                "-fx-background-color: white");
        txtName = new Text(name);
        txtPower = new Text(power + "");
        txtDefense = new Text(defense + "");
        txtSpeed = new Text(speed + "");
        txtAwareness = new Text(awareness + "");
        txtAvatar = new Text(avatar);
        final Button updateBtn = new Button("Modify");
        final Button deleteBtn = new Button("Delete");

        add(ivAvatar,
                0, 8, 2, 1);
        setHalignment(ivAvatar, HPos.CENTER);           //CENTER

        txtAvatar.setFont(Font.font("Arial", FontPosture.ITALIC, 10));
        add(txtAvatar,
                0, 9, 2, 1);
        setHalignment(txtAvatar, HPos.CENTER);

        txtName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        add(txtName,
                0, 1);
        add(txtId,
                1, 1);

        add(lblPower,
                0, 3);
        add(txtPower,
                1, 3);

        add(lblDefense,
                0, 4);
        add(txtDefense,
                1, 4);

        add(lblSpeed,
                0, 5);
        add(txtSpeed,
                1, 5);

        add(lblAwareness,
                0, 6);
        add(txtAwareness,
                1, 6);

        add(txfAvatar,
                0, 9, 2, 1);
        setHalignment(txfAvatar, HPos.CENTER);

        add(txfName,
                0, 1);
        add(txfPower,
                1, 3);
        add(txfDefense,
                1, 4);
        add(txfSpeed,
                1, 5);
        add(txfAwareness,
                1, 6);

        txfName.setOpacity(
                0);
        txfAvatar.setOpacity(
                0);
        txfPower.setOpacity(
                0);
        txfSpeed.setOpacity(
                0);
        txfAwareness.setOpacity(
                0);
        txfDefense.setOpacity(
                0);

        add(updateBtn,
                0, 15);
        setHalignment(updateBtn, HPos.CENTER);

        add(deleteBtn,
                1, 15);
        setHalignment(deleteBtn, HPos.CENTER);

        txfName.setOnAction(
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
                        txfAvatar.requestFocus();
                    }
                }
        );

        updateBtn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        if (updateBtn.getText().equals("Modify")) {
                            txfName.setText(txtName.getText());
                            txfPower.setText(txtPower.getText());
                            txfSpeed.setText(txtSpeed.getText());
                            txfDefense.setText(txtDefense.getText());
                            txfAwareness.setText(txtAwareness.getText());
                            txfAvatar.setText(txtAvatar.getText());

                            txfName.setOpacity(1);
                            txfAvatar.setOpacity(1);

                            txfPower.setOpacity(1);
                            txfSpeed.setOpacity(1);
                            txfAwareness.setOpacity(1);
                            txfDefense.setOpacity(1);

                            updateBtn.setText("Update");
                            txfAvatar.requestFocus();
                            message.setText("Same rules as adding apply!");
                            message.setTextFill(Color.GREEN);
                        } else {
                            String nameU = txfName.getText();
                            String powerU = txfPower.getText();
                            String defenseU = txfDefense.getText();
                            String speedU = txfSpeed.getText();
                            String awarenessU = txfAwareness.getText();
                            String avatarU = txfAvatar.getText();
                            int powerUi, defenseUi, speedUi, awarenessUi;
                            boolean succes = false;

                            try {

                                if (powerU.length() == 0) {
                                    throw new EmptyArgumentException();
                                }
                                powerUi = Integer.parseInt(powerU);

                                if (defenseU.length() == 0) {
                                    throw new EmptyArgumentException();
                                }
                                defenseUi = Integer.parseInt(defenseU);

                                if (speedU.length() == 0) {
                                    throw new EmptyArgumentException();
                                }
                                speedUi = Integer.parseInt(speedU);

                                if (awarenessU.length() == 0) {
                                    throw new EmptyArgumentException();
                                }
                                awarenessUi = Integer.parseInt(awarenessU);

                                Monster monsterU = new Monster(id, nameU, powerUi, defenseUi, speedUi, awarenessUi, avatarU);
                                succes = controller.updateMonster(monsterU);
                            } catch (EmptyArgumentException | OutOfRangeException | ImageNotSelectedException e) {
                                message.setText(e.getMessage());
                                message.setTextFill(Color.RED);
                            } catch (NumberFormatException nfe) {
                                message.setText("All stats have to be integers!");
                                message.setTextFill(Color.RED);
                            } finally {
                                if (succes) {
                                    if (!txtAvatar.getText().equals(avatarU)) {
                                        if (getChildren().contains(ivAvatar)) {
                                            getChildren().remove(ivAvatar);
                                        } else {
                                            getChildren().remove(getChildren().size() - 1);       //Laatst toegevoegde node is een nieuwe afbeelding
                                        }

                                        Image newImg = new Image(Main.class.getResourceAsStream("/images/monsters/" + avatarU));
                                        ImageView newView = new ImageView(newImg);
                                        add(newView, 0, 13, 2, 1);
                                        setHalignment(newView, HPos.CENTER);
                                    }

                                    txtName.setText(txfName.getText());
                                    txtPower.setText(txfPower.getText());
                                    txtSpeed.setText(txfSpeed.getText());
                                    txtDefense.setText(txfDefense.getText());
                                    txtAwareness.setText(txfAwareness.getText());
                                    txtAvatar.setText(txfAvatar.getText());

                                    txfName.setOpacity(0);
                                    txfAvatar.setOpacity(0);

                                    txfPower.setOpacity(0);
                                    txfSpeed.setOpacity(0);
                                    txfAwareness.setOpacity(0);
                                    txfDefense.setOpacity(0);

                                    message.setText("Monster has been updated!");
                                    message.setTextFill(Color.GREEN);
                                    updateBtn.setText("Modify");

                                    txfName.setText("");

                                    txfPower.setText("");
                                    txfSpeed.setText("");
                                    txfDefense.setText("");
                                    txfAwareness.setText("");
                                    txfAvatar.setText("");
                                } else {
                                    txfName.setText(txtName.getText());
                                    txfPower.setText(txtPower.getText());
                                    txfSpeed.setText(txtSpeed.getText());
                                    txfDefense.setText(txtDefense.getText());
                                    txfAwareness.setText(txtAwareness.getText());
                                    txfAvatar.setText(txtAvatar.getText());
                                    txfAvatar.requestFocus();
                                }
                            }
                        }
                    }
                }
        );

        final MonsterPanel monsterPanel = this;

        deleteBtn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event
                    ) {
                        boolean succes = controller.deleteMonster(id);     //eerst db leegmaken

                        if (succes) {
                            message.setText("Monster has been deleted!");
                            message.setTextFill(Color.GREEN);
                            //main.getMonsterFlowPanel().getChildren().removeAll(monsterPanel);
                        }
                    }
                }
        );

    }
}
