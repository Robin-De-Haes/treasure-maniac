/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Hero;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Small panel for initializing or containing the hero's information.
 *
 * @author Robin, Pieter-Jan
 */
public class DetailHero extends Pane {

    private ImageView ivAvatar, ivPower, ivDefense, ivSpeed, ivAwareness, ivLife;
    protected Label lblName, lblPower, lblDefense, lblSpeed, lblAwareness;

    private static Image iPower = new Image(Main.class.getResourceAsStream("/images/icons/Sword.png"));
    private static Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
    private static Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
    private static Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));

    private static Image dead = new Image(Main.class.getResourceAsStream("/images/UI/hp0.png"));

    /**
     * Default constructor for DetailHero for initialization of the hero. Will
     * be used to show initialization changes in a hero.
     *
     * @author Robin
     */
    public DetailHero() {
        initialize();
    }

    /**
     * Constructor for DetailHero containing the Hero's information
     *
     * @param hero hero whose information DetailHero will contain
     * @author Pieter-Jan
     */
    public DetailHero(Hero hero) {
        initialize(hero);
    }

    /**
     * Builds the panel for initializing the hero.
     *
     * @author Robin
     */
    private void initialize() {
        lblName = new Label("Player");
        lblName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblName.getStyleClass().addAll("fontWhite");
        lblName.relocate(0, 0);

        ivPower = new ImageView(iPower);
        configStatsIcons(ivPower, 24);
        lblPower = new Label("0", ivPower);
        lblPower.getStyleClass().addAll("fontWhite");
        lblPower.relocate(0, 30);

        ivDefense = new ImageView(iDefense);
        configStatsIcons(ivPower, 24);
        lblDefense = new Label("0", ivDefense);
        lblDefense.getStyleClass().addAll("fontWhite");
        lblDefense.relocate(0, 60);

        ivSpeed = new ImageView(iSpeed);
        configStatsIcons(ivSpeed, 24);
        lblSpeed = new Label("0", ivSpeed);
        lblSpeed.getStyleClass().addAll("fontWhite");
        lblSpeed.relocate(60, 30);

        ivAwareness = new ImageView(iAwareness);
        configStatsIcons(ivAwareness, 24);
        lblAwareness = new Label("0", ivAwareness);
        lblAwareness.getStyleClass().addAll("fontWhite");
        lblAwareness.relocate(60, 60);

        getChildren().addAll(lblName, lblPower, lblDefense, lblAwareness, lblSpeed);
    }

    /**
     * Builds the panel for showing the hero's information.
     *
     * @param hero
     * @author Pieter-Jan
     */
    private void initialize(Hero hero) {
        getStylesheets().add("css/UI.css");
        getStyleClass().add("playerFrame");
        setMinWidth(300);
        setMinHeight(125);

        //Avatar
        StackPane avatar = new StackPane();
        avatar.setMaxSize(87, 87);
        avatar.setMinSize(87, 87);

        Image iAvatar = new Image(Main.class.getResourceAsStream("/images/heroes/" + hero.getAvatar()));
        ivAvatar = new ImageView(iAvatar);
        ivAvatar.setFitWidth(80);
        ivAvatar.setFitHeight(80);

        Image iOverlay = new Image(Main.class.getResourceAsStream("/images/UI/avatar_overlay.png"));
        ImageView ivOverlay = new ImageView(iOverlay);
        ivOverlay.setFitHeight(87);
        ivOverlay.setFitWidth(87);

        avatar.getChildren().addAll(ivAvatar, ivOverlay);

        //Name & Health
        Pane status = new Pane();
        lblName = new Label(hero.getName());
        lblName.getStyleClass().add("playerName");
        Image iLife = new Image(Main.class.getResourceAsStream("/images/UI/hp6.png"));
        ivLife = new ImageView(iLife);
        ivLife.setFitHeight(14);
        ivLife.setFitWidth(120);

        ivLife.relocate(0, 20);
        lblName.relocate(0, 0);
        status.getChildren().addAll(lblName, ivLife);

        //stats
        Pane stats = new Pane();
        ivPower = new ImageView(iPower);
        configStatsIcons(ivPower, 18);
        ivDefense = new ImageView(iDefense);
        configStatsIcons(ivDefense, 18);
        ivAwareness = new ImageView(iAwareness);
        configStatsIcons(ivAwareness, 18);
        ivSpeed = new ImageView(iSpeed);
        configStatsIcons(ivSpeed, 18);

        lblPower = new Label(String.valueOf(hero.getTotalPower()));
        lblPower.getStyleClass().add("stats");
        lblDefense = new Label(String.valueOf(hero.getTotalDefense()));
        lblDefense.getStyleClass().add("stats");
        lblAwareness = new Label(String.valueOf(hero.getTotalAwareness()));
        lblAwareness.getStyleClass().add("stats");
        lblSpeed = new Label(String.valueOf(hero.getTotalSpeed()));
        lblSpeed.getStyleClass().add("stats");

        ivPower.relocate(0, 0);
        lblPower.relocate(20, 0);
        ivDefense.relocate(0, 25);
        lblDefense.relocate(20, 25);
        ivAwareness.relocate(65, 0);
        lblAwareness.relocate(85, 0);
        ivSpeed.relocate(65, 25);
        lblSpeed.relocate(85, 25);
        stats.getChildren().addAll(ivPower, ivDefense, ivAwareness, ivSpeed, lblPower,
                lblDefense, lblAwareness, lblSpeed);

        avatar.relocate(20, 20); //avatar icon
        status.relocate(120, 20); // name & health
        stats.relocate(120, 60); // stats
        getChildren().addAll(avatar, status, stats);
    }

    /**
     * Configures the icons used to symbolize the hero's stats
     *
     * @param iv imageview of the icon to be configured
     * @param size height and width in which the square icon should fit
     * @author Pieter-Jan
     */
    private void configStatsIcons(ImageView iv, int size) {
        iv.setFitHeight(size);
        iv.setFitWidth(size);
    }

    /**
     * Set the text of the name-label
     *
     * @param name new text in name-label
     * @author Robin
     */
    public void setName(String name) {
        this.lblName.setText(name);
    }

    /**
     * Set the text of the power-label
     *
     * @param power new text in power-label
     * @author Robin
     */
    public void setPower(String power) {
        this.lblPower.setText("" + power);
    }

    /**
     * Set the text of the defense-label
     *
     * @param defense new text in defense-label
     * @author Robin
     */
    public void setDefense(String defense) {
        this.lblDefense.setText("" + defense);
    }

    /**
     * Set the text of the speed-label
     *
     * @param speed new text in speed-label
     * @author Robin
     */
    public void setSpeed(String speed) {
        this.lblSpeed.setText("" + speed);
    }

    /**
     * Set the text of the awareness-label
     *
     * @param awareness new text in awareness-label
     * @author Robin
     */
    public void setAwareness(String awareness) {
        this.lblAwareness.setText("" + awareness);
    }

    /**
     * Remove all the hearts symbolizing the hero's life.
     *
     * @author Robin
     */
    public void removeLife() {
        ivLife.setImage(dead);
    }
}
