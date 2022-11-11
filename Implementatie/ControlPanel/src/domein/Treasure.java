package domein;

import exceptions.*;
import java.util.List;

/**
 * Treasure that can be found in the game
 *
 * @author Robin
 */
public class Treasure {

    private int id;
    private String name;
    private int value;
    private int awareness;
    private String description;
    private int power;
    private int defense;
    private int speed;
    private String avatar;
    private List<Monster> monsters;

    private final static int MAX = 3;
    private final static int MIN = -3;
    private final static int MAX_NAME = 15;
    private final static int MAX_DESCRIPTION = 50;
    private final static int MAX_VALUE = 5000;

    /**
     * Default constructor to create a treasure with default values.
     */
    public Treasure() {
        this(0, "TREASURE", 0, "DESCRIPTION", 0, 0, 0, 0, "default.png");
    }

    /**
     *
     * Constructor to create a treasure with custom values
     *
     * @param id
     * @param name name of the treasure
     * @param value
     * @param description description of the treasure
     * @param avatar path of the avatar
     * @param power power of the treasure
     * @param defense defense of the treasure
     * @param speed speed of the treasure
     * @param awareness awareness of the treasure
     *
     * @exception if incorrect value was entered
     */
    public Treasure(int id, String name, int value, String description, int power, int defense, int speed, int awareness, String avatar) throws EmptyArgumentException, OutOfRangeException, ImageNotSelectedException {
        setId(id);
        setName(name);
        setDescription(description);
        setAvatar(avatar);
        setPower(power);
        setDefense(defense);
        setSpeed(speed);
        setAwareness(awareness);
        setValue(value);
    }

    /**
     *
     * Copy-constructor to create a treasure with the same values as the given
     * treasure
     *
     * @param treasure treasure whose values will be copied into the new
     * treasure
     */
    public Treasure(Treasure treasure) {
        id = treasure.id;
        name = treasure.name;
        description = treasure.description;
        avatar = treasure.avatar;
        power = treasure.power;
        defense = treasure.defense;
        speed = treasure.speed;
        awareness = treasure.awareness;
        value = treasure.value;
    }

    /**
     * Give the id of the treasure
     *
     * @return the treasure's id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the treasure
     *
     * @param id new value for the treasure's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Give the name of the treasure
     *
     * @return the name of the treasure
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the treasure
     *
     * @param name new value for the treasure's name
     * @throws EmptyArgumentException if entered value was empty
     * @throws OutOfRangeException if the length of the entered value exceeds
     * the boundaries
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
     * Give the value of the treasure
     *
     * @return the value of the treasure
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the treasure
     *
     * @param value new value for the treasure's value
     * @throws OutOfRangeException if negative value was entered
     */
    public void setValue(int value) throws OutOfRangeException {
        if (value < 0 || value > MAX_VALUE) {
            throw new OutOfRangeException();
        } else {
            this.value = value;
        }
    }

    /**
     * Give the description of the treasure
     *
     * @return the description of the treasure
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the treasure
     *
     * @param description new value for treasure's description
     * @throw EmptyArgumentException or OutOfRangeException if incorrect value
     * was entered
     */
    public void setDescription(String description) throws EmptyArgumentException, OutOfRangeException {
        if (description.equals("")) {
            throw new EmptyArgumentException();
        }
        if (description.length() > MAX_DESCRIPTION) {
            throw new OutOfRangeException();
        }
        this.description = description;

    }

    /**
     * Give the power of the treasure
     *
     * @return the power of the treasure
     */
    public int getPower() {
        return power;
    }

    /**
     * Sets the power of the treasure
     *
     * @param power new value for the treasure's power
     * @throws OutOfRangeException if entered value exceeds the boundaries
     */
    public void setPower(int power) throws OutOfRangeException {
        if (power < MIN || power > MAX) {
            throw new OutOfRangeException();
        } else {
            this.power = power;
        }
    }

    /**
     * Give the defense of the treasure
     *
     * @return the defense of the treasure
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Sets the defense of the treasure
     *
     * @param defense new value for the treasure's defense
     * @throws OutOfRangeException if entered value exceeds the boundaries
     */
    public void setDefense(int defense) throws OutOfRangeException {
        if (defense < MIN || defense > MAX) {
            throw new OutOfRangeException();
        } else {
            this.defense = defense;
        }
    }

    /**
     * Give the speed of the treasure
     *
     * @return the speed of the treasure
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set the speed of the treasure
     *
     * @param speed new value for the treasure's speed
     * @throws OutOfRangeException if entered value exceeds the boundaries
     */
    public void setSpeed(int speed) throws OutOfRangeException {
        if (speed < MIN || speed > MAX) {
            throw new OutOfRangeException();
        } else {
            this.speed = speed;
        }
    }

    /**
     * Give the awareness of the treasure
     *
     * @return the awareness of the treasure
     */
    public int getAwareness() {
        return awareness;
    }

    /**
     * Set the awareness of the treasure
     *
     * @param awareness new value for the treasure's awareness
     * @throws OutOfRangeException if entered value exceeds the boundaries
     */
    public void setAwareness(int awareness) throws OutOfRangeException {
        if (awareness < MIN || awareness > MAX) {
            throw new OutOfRangeException();
        } else {
            this.awareness = awareness;
        }
    }

    /**
     * Give the avatar of the treasure
     *
     * @return the image-name of the treasure
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Set the avatar of the treasure
     *
     * @param avatar new value for the treasure's avatar.
     * @throws ImageNotSelectedException if null was entered which means no
     * avatar was chosen
     */
    public void setAvatar(String avatar) throws ImageNotSelectedException {
        if (avatar == null) {
            throw new ImageNotSelectedException();
        }
        this.avatar = avatar;

    }

    /**
     * Give highest boundary for a treasure's stats
     *
     * @return the maximum for a treasure's stats
     */
    public static int getMAX() {
        return MAX;
    }

    /**
     * Give lowest boundary for a treasure's stats
     *
     * @return the minimum for a treasure's stats
     */
    public static int getMIN() {
        return MIN;
    }

    /**
     * Give maximum characters for the name
     *
     * @return maximum number of characters for the treasure's name
     */
    public static int getMAX_NAME() {
        return MAX_NAME;
    }

    /**
     * Give maximum characters for the description
     *
     * @return maximum number of characters for the treasure's description
     */
    public static int getMAX_DESCRIPTION() {
        return MAX_DESCRIPTION;
    }

    /**
     * Give maximum value of a treasure
     *
     * @return maximum value of a treasure
     */
    public static int getMAX_VALUE() {
        return MAX_VALUE;
    }

    /**
     * Compare with a treasure
     *
     * @param treasure the treasure that will be compared with
     * @return true if treasures are the same, false if not
     */
    public boolean equals(Treasure treasure) {
        return power == treasure.power && defense == treasure.defense
                && speed == treasure.speed && awareness == treasure.awareness
                && value == treasure.value && avatar.equals(treasure.avatar)
                && name.equals(treasure.name) && description.equals(treasure.description);
    }

    /**
     *
     * Copies all values from one treasure into another one
     *
     * @param treasure treasure whose values will be copied into the original
     * treasure
     */
    public void copyValues(Treasure treasure) {
        id = treasure.id;
        name = treasure.name;
        description = treasure.description;
        avatar = treasure.avatar;
        power = treasure.power;
        defense = treasure.defense;
        speed = treasure.speed;
        awareness = treasure.awareness;
        value = treasure.value;
    }

    /**
     * Give the treasures' information
     *
     * @return information-String about the treasure-object
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
     * Give the sum of all the treasure's stats.
     *
     * @return the total of the treasure's stats
     */
    public int getTotal() {
        return power + defense + speed + awareness;
    }

}
