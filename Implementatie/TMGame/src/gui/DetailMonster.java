package gui;

import domein.Monster;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * A small panel containing a monster's information.
 *
 * @author Robin, Pieter-Jan, Steve
 */
public class DetailMonster extends Pane {

    private ImageView ivAvatar, ivPower, ivDefense, ivSpeed, ivAwareness, ivLife;
    protected Label lblName, lblPower, lblDefense, lblSpeed, lblAwareness;

    private static Image iPower = new Image(Main.class.getResourceAsStream("/images/icons/Sword.png"));
    private static Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
    private static Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
    private static Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));

    private static Image dead = new Image(Main.class.getResourceAsStream("/images/UI/hp0.png"));

    /**
     * Constructor for initializing DetailMonster based on a monster's
     * information
     *
     * @param monster the monster whose information will be used
     */
    public DetailMonster(Monster monster) {
        initialize(monster);
    }

    /**
     * Builds the DetailMonster-Panel
     *
     * @param monster the monster whose information the panel will contain
     */
    private void initialize(Monster monster) {
        getStylesheets().add("css/UI.css");
        getStyleClass().add("playerFrame");
        setMinWidth(300);
        setMinHeight(125);

        //Avatar
        StackPane avatar = new StackPane();
        avatar.setMaxSize(87, 87);
        avatar.setMinSize(87, 87);

        //Testen of er geen idleversie is van het monster
        String temp = monster.getAvatar();
        temp = temp.substring(0, temp.length() - 4);
        temp = temp + "Idle.gif";
        Image iAvatar;
        if (Main.class.getResourceAsStream("/images/monster/" + temp) != null) {
            iAvatar = new Image(Main.class.getResourceAsStream("/images/monster/" + temp));
        } else {
            iAvatar = new Image(Main.class.getResourceAsStream("/images/monster/" + monster.getAvatar()));
        }
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
        Label name = new Label(monster.getName());
        name.getStyleClass().add("playerName");
        Image iLife = new Image(Main.class.getResourceAsStream("/images/UI/hp6.png"));
        ivLife = new ImageView(iLife);
        ivLife.setFitHeight(14);
        ivLife.setFitWidth(120);

        ivLife.relocate(0, 20);
        name.relocate(0, 0);
        status.getChildren().addAll(name, ivLife);

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

        lblPower = new Label(String.valueOf(monster.getPower()));
        lblPower.getStyleClass().add("stats");
        lblDefense = new Label(String.valueOf(monster.getDefense()));
        lblDefense.getStyleClass().add("stats");
        lblAwareness = new Label(String.valueOf(monster.getAwareness()));
        lblAwareness.getStyleClass().add("stats");
        lblSpeed = new Label(String.valueOf(monster.getSpeed()));
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
     * Configures the icons used to symbolize the monster's stats
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
     * Remove all the hearts symbolizing the hero's life.
     *
     * @author Robin
     */
    public void removeLife() {
        ivLife.setImage(dead);
    }
}
