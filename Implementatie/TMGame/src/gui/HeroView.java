/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The player of the game.
 *
 * @author Robin
 */
public class HeroView extends ImageView {

    private String className;
    private static Image heroLeft;
    private static Image heroRight;
    private static Image heroIdleRight;
    private static Image heroIdleLeft;

    //De images van alle klassen hier aanmaken
    private static final Image warriorLeft = new Image(Main.class.getResourceAsStream("/images/heroes/WarriorLeft.gif"));
    private static final Image warriorRight = new Image(Main.class.getResourceAsStream("/images/heroes/WarriorRight.gif"));
    private static final Image warriorIdleRight = new Image(Main.class.getResourceAsStream("/images/heroes/WarriorIdleRight.gif"));
    private static final Image warriorIdleLeft = new Image(Main.class.getResourceAsStream("/images/heroes/WarriorIdleLeft.gif"));

    private static final Image mageLeft = new Image(Main.class.getResourceAsStream("/images/heroes/MageLeft.gif"));
    private static final Image mageRight = new Image(Main.class.getResourceAsStream("/images/heroes/MageRight.gif"));
    private static final Image mageIdleRight = new Image(Main.class.getResourceAsStream("/images/heroes/MageIdleRight.gif"));
    private static final Image mageIdleLeft = new Image(Main.class.getResourceAsStream("/images/heroes/MageIdleLeft.gif"));

    private static final Image rangerLeft = new Image(Main.class.getResourceAsStream("/images/heroes/RangerLeft.gif"));
    private static final Image rangerRight = new Image(Main.class.getResourceAsStream("/images/heroes/RangerRight.gif"));
    private static final Image rangerIdleRight = new Image(Main.class.getResourceAsStream("/images/heroes/RangerIdleRight.gif"));
    private static final Image rangerIdleLeft = new Image(Main.class.getResourceAsStream("/images/heroes/RangerIdleLeft.gif"));

    private static final Image deadHero = new Image(Main.class.getResourceAsStream("/images/heroes/DeadHero.gif"));

    /**
     * Default constructor of HeroView.
     */
    public HeroView() {
        setView("Mage");
        setHeroIdleRight();
    }

    /**
     * Changes the images used for movements of the hero, based on the chosen
     * class.
     *
     * @param className class to which the hero will be changed
     * @author Robin
     */
    public void setView(String className) {
        switch (className) //Alle mogelijke klassen hier toevoegen
        {
            case "Warrior":
                heroLeft = warriorLeft;
                heroRight = warriorRight;
                heroIdleLeft = warriorIdleLeft;
                heroIdleRight = warriorIdleRight;
                break;
            case "Ranger":
                heroLeft = rangerLeft;
                heroRight = rangerRight;
                heroIdleLeft = rangerIdleLeft;
                heroIdleRight = rangerIdleRight;
                break;
            case "Mage":
                heroLeft = mageLeft;
                heroRight = mageRight;
                heroIdleLeft = mageIdleLeft;
                heroIdleRight = mageIdleRight;
                break;
        }
        setHeroIdleRight();
    }

    /**
     * Change the image of the hero to the movement for running to the left.
     */
    public void setHeroMoveLeft() {
        setImage(heroLeft);
    }

    /**
     * Change the image of the hero to the movement for running to the right.
     */
    public void setHeroMoveRight() {
        setImage(heroRight);
    }

    /**
     * Change the image of the hero to the one for facing the right.
     */
    public void setHeroIdleRight() {
        setImage(heroIdleRight);
    }

    /**
     * Change the image of the hero to the one for facing the left.
     */
    public void setHeroIdleLeft() {
        setImage(heroIdleLeft);
    }

    public void placeGravestone() {
        heroIdleRight = deadHero;
    }
}
