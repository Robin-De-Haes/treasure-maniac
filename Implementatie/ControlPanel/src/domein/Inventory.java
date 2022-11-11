//NIET voor iteratie 2 (en waarschijnlijk onnodig)
package domein;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory of the hero that contains his collected treasures and their
 * influence on his stats.
 *
 * @author Robin
 */
public class Inventory {

    private List<Treasure> treasures;

    private int invPower;
    private int invDefense;
    private int invSpeed;
    private int invAwareness;
    private int invValue;
    //private static int MAXITEMS = 30;               //Er wordt geen maximum gehanteerd

    /**
     * Default constructor for an empty inventory.
     */
    public Inventory() {
        invPower = 0;
        invDefense = 0;
        invSpeed = 0;
        invAwareness = 0;
        treasures = new ArrayList<>();
    }

    /**
     * Give all the treasures collected.
     *
     * @return a list containing all treasures that were collected
     */
    public List<Treasure> giveTreasures() {
        return treasures;
    }

    /**
     * Add a treasure to the inventory
     *
     * @param treasure the treasure that should be added to the inventory
     */
    public void addTreasure(Treasure treasure) {

        invPower += treasure.getPower();
        invDefense += treasure.getDefense();
        invSpeed += treasure.getSpeed();
        invAwareness += treasure.getAwareness();
        invValue += treasure.getValue();
        treasures.add(treasure);
    }

    /**
     * Remove a treasure from the inventory
     *
     * @param treasure the treasure that should be deleted from the inventory
     */
    public void removeTreasure(Treasure treasure) {
        invPower -= treasure.getPower();
        invDefense -= treasure.getDefense();
        invSpeed -= treasure.getSpeed();
        invAwareness -= treasure.getAwareness();
        invValue -= treasure.getValue();
        treasures.remove(treasure);
    }

    /**
     * Give the combined power of all the treasures in the inventory.
     *
     * @return the combined power of all the treasures
     */
    public int getInvPower() {
        return invPower;
    }

    /**
     * Give the combined defense of all the treasures in the inventory.
     *
     * @return the combined defense of all the treasures
     */
    public int getInvDefense() {
        return invDefense;
    }

    /**
     * Give the combined speed of all the treasures in the inventory.
     *
     * @return the combined speed of all the treasures
     */
    public int getInvSpeed() {
        return invSpeed;
    }

    /**
     * Give the combined awareness of all the treasures in the inventory.
     *
     * @return the combined awareness of all the treasures
     */
    public int getInvAwareness() {
        return invAwareness;
    }

    /**
     * Give the combined value of all the treasures in the inventory.
     *
     * @return the combined value of all the treasures
     */
    public int getInvValue() {
        return invValue;
    }
}
