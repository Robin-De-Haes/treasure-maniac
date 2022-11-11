/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;

/**
 * A chamber consisting of a floor, a background and possibly a monster and/or
 * treasure.
 *
 * @author Robin
 */
public class Chamber {

    private static int round = -1;         //Houdt bij in welke ronde je zit. De 1e kamer telt niet meer en na het creëren ervan zit je dus in Chamber 0
    private List<Treasure> treasures;
    private Monster monster;
    private String background, platformType;
    private DomeinController dc;

    /**
     * Constructor for an empty chamber with random floor tiles and a random
     * background.
     */
    public Chamber() {
        if (round == -1) {
            background = "info";
        } else {
            generateBackground();
        }
        generatePlatform();

        treasures = null;
        monster = null;

        round++;
    }

    /**
     * Constructor for a chamber containing a treasure chest
     *
     * @param treasures treasures inside the treasure chest
     */
    public Chamber(List<Treasure> treasures) {
        this();
        this.treasures = treasures;
    }

    /**
     * Constructor for a chamber containing a monster
     *
     * @param dc used to search the treasures linked to the monster
     * @param monster monster inside the chamber
     */
    public Chamber(DomeinController dc, Monster monster) {
        this();
        this.dc = dc;
        this.monster = monster;
        treasures = dc.searchAllTreasuresFromMonster(monster);        //Zodat kamer ook een schat heeft en de corresponderende trigger zal triggeren
    }

    /**
     * Dynamic generating FloorTiles
     *
     * @author Pieter-Jan
     */
    private void generateBackground() {               //Gebeurt in Chamber
        int random = Utility.generateRandom(0, 6);
        switch (random) {
            case 0:
                background = "ruinedcity";
                break;
            case 1:
                background = "sky";
                break;
            case 2:
                background = "stonewall";
                break;
            case 3:
                background = "ancientcity";
                break;
            case 4:
                background = "nightsky";
                break;
            case 5:
                background = "greenhills";
                break;
            case 6:
                background = "desert";
                break;

        }
    }

    /**
     * Dynamic generating FloorTiles.
     *
     * @author PJ
     */
    private void generatePlatform() {
        int platform = Utility.generateRandom(1, 2);
        switch (platform) {
            case 1:
                platformType = "wasteland";
                break;
            case 2:
                platformType = "grass";
                break;
        }
    }

    /**
     * Give the background of the chamber.
     *
     * @return the background-class of the chamber
     */
    public String getBackground() {
        return background;
    }

    /**
     * Give the floor tiles of the chamber
     *
     * @return the floor-class of the chamber
     */
    public String getPlatformType() {
        return platformType;
    }

    /**
     * Give the chamber's treasures
     *
     * @return the treasures inside the chamber (null if it contains none)
     */
    public List<Treasure> getTreasures() {
        return treasures;
    }

    /**
     * Check if the chamber has treasures in it.
     *
     * @return true if the chamber contains treasures, false if not
     */
    public boolean hasChest() {
        return (treasures != null);
    }

    /**
     * Give the chamber's monster
     *
     * @return the monster inside the chamber (null if it contains none)
     */
    public Monster getMonster() {
        return monster;
    }

    /**
     * Check if the chamber has a monster in it.
     *
     * @return true if the chamber contains a monster, false if not
     */
    public boolean hasMonster() {
        return (monster != null);
    }

    /**
     * Removes the chamber's monster.
     */
    public void removeMonster() {
        monster = null;
    }

    /**
     * Set the chamber's round.
     *
     * @param round the round to be set for the chambers
     */
    public static void setRound(int round) {
        Chamber.round = round;
    }

    /**
     * Give the chambers' round.
     *
     * @return
     */
    public static int getRound() {
        return round;
    }

    /**
     * Reset the chambers' round.
     */
    public static void reset() {
        round = -1;
    }
}
