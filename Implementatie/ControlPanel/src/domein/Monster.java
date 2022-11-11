//Iteratie 2
package domein;

import exceptions.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Monster in the game.
 *
 * @author Robin
 */
public class Monster {

    private int id;
    private int power;
    private int defense;
    private int speed;
    private int awareness;
    private String name;
    private String avatar;
    private List<Treasure> treasures;             //Niet voor iteratie 2

    private final static int MAX = 30;
    private final static int MIN = 0;
    private final static int MAXTREASURES = 3;            //Er wordt geen maximum meer gehanteerd
    private final static int MAX_NAME = 20;

    /**
     * Default constructor to create a monster with default values.
     */
    public Monster() {
        this(0, "MONSTER", 0, 0, 0, 0, null);
    }

    /**
     * Constructor to create a monster with custom values
     *
     * @param id the id of the monster in the database
     * @param name the name of the monster
     * @param avatar the path of the monster's avatar
     * @param power the power of the monster
     * @param defense the defense of the monster
     * @param speed the speed of the monster
     * @param awareness the awareness of the monster
     */
    public Monster(int id, String name, int power, int defense, int speed, int awareness, String avatar) throws EmptyArgumentException, OutOfRangeException, ImageNotSelectedException {
        setId(id);
        setName(name);
        setAvatar(avatar);
        setPower(power);
        setDefense(defense);
        setSpeed(speed);
        setAwareness(awareness);
        treasures = new ArrayList<>();
    }

    /**
     *
     * Copy-constructor to create a monster with the same values as the given
     * monster
     *
     * @param monster monster whose values will be copied into the new monster
     */
    public Monster(Monster monster) {
        id = monster.id;
        name = monster.name;
        avatar = monster.avatar;
        power = monster.power;
        defense = monster.defense;
        speed = monster.speed;
        awareness = monster.awareness;
    }

    /**
     * Link a treasure to the monster monster can only defend a maximum 3
     * treasures @link isFull()
     *
     * @param treasure the treasure that should be linked with the monster
     * @return true or false depending if the monster can be linked with a
     * treasure
     * @deprecated 
     */
    public boolean addTreasure(Treasure treasure) {       
        if (!isFull()) {
            treasures.add(treasure);
            return true;
        }
        return false;
    }

    /**
     *
     * Checks if a monster can defend more treasure
     *
     * @return true or false depending if the monster can defend more treasure
     * or not
     * @deprecated
     */
    public boolean isFull() {                               //NIET voor iteratie 2 (en waarschijnlijk onnodig)
        return treasures.size() <= MAXTREASURES;             //Maximaal 3 schatten in 1 kamer, bij 1 monster
    }

    /**
     * Give the id of the monster
     *
     * @return the monster's id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the monster
     *
     * @param id new value for the monster's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Give the power of the monster
     *
     * @return the monster's power
     */
    public int getPower() {
        return power;
    }

    /**
     * Set the power of the monster
     *
     * @param power new value for the monster's power
     */
    public void setPower(int power) throws OutOfRangeException {
        if (power < MIN || power > MAX) {
            throw new OutOfRangeException();
        } else {
            this.power = power;                   //Eigenschappen moeten tussen (MIN=)1 en (MAX=)100 liggen
        }
    }

    /**
     * Give the defense of the monster
     *
     * @return the monster's defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Set the defense of the monster
     *
     * @param defense new value for the monster's defense
     */
    public void setDefense(int defense) throws OutOfRangeException {
        if (defense < MIN || defense > MAX) {
            throw new OutOfRangeException();
        } else {
            this.defense = defense;
        }
    }

    /**
     * Give the speed of the monster
     *
     * @return the monster's speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed of the monster
     *
     * @param speed new value for the monster's speed
     */
    public void setSpeed(int speed) throws OutOfRangeException {
        if (speed < MIN || speed > MAX) {
            throw new OutOfRangeException();
        }
        this.speed = speed;
    }

    /**
     * Give the awareness of the monster
     *
     * @return the monster's awareness
     */
    public int getAwareness() {
        return awareness;
    }

    /**
     * Set the awareness of the monster
     *
     * @param awareness new value for the monster's awareness
     */
    public void setAwareness(int awareness) throws OutOfRangeException {
        if (awareness < MIN || awareness > MAX) {
            throw new OutOfRangeException();
        } else {
            this.awareness = awareness;
        }
    }

    /**
     * Give the name of the monster
     *
     * @return the monster's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the monster
     *
     * @param name new value for the monster's name
     */
    public void setName(String name) throws EmptyArgumentException, OutOfRangeException {
        if (name.equals("")) {
            throw new EmptyArgumentException();
        }
        if (name.length() > MAX_NAME) {
            throw new OutOfRangeException();
        }
        this.name = name;
    }

    /**
     * Give the imagename of the monster avatar
     *
     * @return imagename of the monster's avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Set the avatar of the monster
     *
     * @param avatar new value for the monster's avatar
     */
    public void setAvatar(String avatar) throws ImageNotSelectedException {
        this.avatar = avatar;

    }

    /**
     * Give highest boundary for a monster's stats
     *
     * @return the maximum for a monster's stats
     */
    public static int getMAX() {
        return MAX;
    }

    /**
     * Give lowest boundary for a monster's stats
     *
     * @return the minimum for a monster's stats
     */
    public static int getMIN() {
        return MIN;
    }

    /**
     * Give maximum characters for the name
     *
     * @return maximum number of characters for the monster's name
     */
    public static int getMAX_NAME() {
        return MAX_NAME;
    }

    /**
     * Compare with a monster
     *
     * @param monster the monster that will be compared with
     * @return true if monsters are the same, false if not
     */
    public boolean equals(Monster monster) {
        return power == monster.power && defense == monster.defense
                && speed == monster.speed && awareness == monster.awareness
                && avatar.equals(monster.avatar) && name.equals(monster.name);
    }

    /**
     *
     * Copies all values from one monster into another one
     *
     * @param monster monster whose values will be copied into the original
     * monster
     *
     */
    public void copyValues(Monster monster) {
        name = monster.name;
        avatar = monster.avatar;
        power = monster.power;
        defense = monster.defense;
        speed = monster.speed;
        awareness = monster.awareness;
    }

    /**
     * Give the monsters' information
     *
     * @return information-String about the monsters-object
     */
    @Override
    public String toString() {
        String text;
        if (id >= 10) {
            text = "[#" + id + "]\t" + name;
        } else {
            text = "[#" + id + "]\t\t" + name;
        }
        return text;
    }

    /**
     * Calculate the monster's awareness for one turn in a fight.
     *
     * @return total awareness of the monster for one turn
     */
    public int calcTotalAwareness() {
        int total = awareness + (int) (Math.random() * 6) + 1;
        return total;
    }

    /**
     * Calculate the monster's power for one turn in a fight.
     *
     * @return total power of the monster for one turn
     */
    public int calcTotalPower() {
        int total = power + (int) (Math.random() * 6) + 1;
        return total;
    }

    /**
     * Calculate the monster's defense for one turn in a fight.
     *
     * @return total defense of the monster for one turn
     */
    public int calcTotalDefense() {
        int total = defense + (int) (Math.random() * 6) + 1;
        return total;
    }

    /**
     * Calculate the monster's speed for one turn in a fight.
     *
     * @return total speed of the monster for one turn
     */
    public int calcTotalSpeed() {
        int total = speed + (int) (Math.random() * 6) + 1;
        return total;
    }

    /**
     * Give the monster's total strength. This will be calculated as the sum of
     * all his stats.
     *
     * @return the total strength of the monster
     */
    public int getTotal() {
        return power + defense + speed + awareness;
    }
}
