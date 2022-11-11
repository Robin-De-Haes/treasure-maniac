package domein;

/**
 * Hero of the game.
 *
 * @author Robin
 */
public class Hero {

    private int id;
    private int power;
    private int defense;
    private int speed;
    private int awareness;
    private String name;
    private String avatar;
    private int score;
    private int round;

    private Inventory inventory;
    private boolean alive;

    private final static int MAX = 30;                 //Minimum en maximum-waarden voor de eigenschappen
    private final static int MIN = 0;
    private final static int MAX_NAME = 15;

    /**
     * Create a Hero object with default values.
     */
    public Hero() {
        this(-1, "Player", "MageAvatar.gif", 0, 0, 0, 0, 0, 0, true);
    }

    /**
     * Create a Hero object with custom values
     *
     * @param name Name of your hero
     * @param avatar The avatar of your hero
     * @param power power of your hero
     * @param defense defense of your hero
     * @param speed speed of your hero
     * @param awareness awareness of your hero
     */
    public Hero(int id, String name, String avatar, int power, int defense, int speed, int awareness, int score, int round, boolean alive) {
        this.id = id;
        setName(name);
        setAvatar(avatar);
        setPower(power);
        setDefense(defense);
        setSpeed(speed);
        setAwareness(awareness);
        setScore(score);
        setRound(round);
        inventory = new Inventory();
        this.alive = alive;
    }                                               //Bij het spel zelf ervoor zorgen dat alle nodige waarden zijn ingegeven, meerdere constructors zijn niet nodig

    /**
     * Set the id of the hero.
     *
     * @param id the value to which the id should be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Give the id of the hero.
     *
     * @return the id of the hero
     */
    public int getId() {
        return id;
    }

    /**
     * Give the power of the hero.
     *
     * @return the power of the hero
     */
    public int getPower() {
        return power;
    }

    /**
     * Set the hero's power
     *
     * @param power new value for power
     */
    public void setPower(int power) {
        this.power = (power >= MIN && power <= MAX) ? power : MAX;     //Eigenschappen liggen tussen 0 en 8
    }

    /**
     * Give the hero's defense.
     *
     * @return the defense of the hero
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Set the hero's defense
     *
     * @param defense new value for defense
     */
    public void setDefense(int defense) {
        this.defense = (defense >= MIN && defense <= MAX) ? defense : MAX;
    }

    /**
     * Give the hero's speed
     *
     * @return the speed of the hero
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Set the hero's speed
     *
     * @param speed new value for speed
     */
    public void setSpeed(int speed) {
        this.speed = (speed >= MIN && speed <= MAX) ? speed : MAX;
    }

    /**
     * Give the hero's awareness
     *
     * @return the awareness of the hero
     */
    public int getAwareness() {
        return awareness;
    }

    /**
     * Set the hero's awareness
     *
     * @param awareness new value for awareness
     */
    public void setAwareness(int awareness) {
        this.awareness = (awareness >= MIN && awareness <= MAX) ? awareness : MAX;
    }

    /**
     * Give the name of the hero
     *
     * @return the hero's name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the hero
     *
     * @param name changes the hero's name
     */
    public void setName(String name) {
        this.name = (!name.equals("")) ? name : "Treasure Maniac";          //Naam is per default Treasure Maniac indien niets ingegeven
    }

    /**
     * Give the path of the avatar-image.
     *
     * @return returns the path of the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Set the hero's avatar path
     *
     * @param avatar changes the path of the avatar, resulting in changing the
     * avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = (!avatar.equals("")) ? avatar : "";           //Ofwel een default-avatar ofwel geen
    }

    /**
     * Set the hero's score
     *
     * @param score the value to which the score should be set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Give the score the hero has reached.
     *
     * @return the hero's score
     */
    public int getScore() {
        return (inventory.getInvValue() == 0 ? score : inventory.getInvValue());            //Want heroes in highscores hebben geen inventory meer
    }

    /**
     * Set the round the hero has reached.
     *
     * @param round the new value for the round
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Give the round the hero has reached.
     *
     * @return the hero's reached round
     */
    public int getRound() {
        return round;
    }

    /**
     * Set if the hero's alive or not
     *
     * @param alive new value for hero's alive-attribute
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Check is hero's alive
     *
     * @return hero's alive-atrribute
     */
    public boolean getAlive() {
        return alive;
    }

    /**
     * Give the hero's total strength. This will be calculated as the sum of all
     * his stats, increased or decreased by the treasures in his inventory.
     *
     * @return the total strength of the hero
     */
    public int getTotal() {
        return getTotalPower() + getTotalDefense() + getTotalSpeed() + getTotalAwareness();
    }

    /**
     * Calculate the hero's awareness for one turn in a fight.
     *
     * @return total awareness of the hero for one turn
     */
    public int calcTotalAwareness() {

        int total = awareness + inventory.getInvAwareness();
        if (total >= MIN) {
            total += (int) (Math.random() * 6) + 1;
        } else {
            total = (int) (Math.random() * 6) + 1;         //Totaal=minimum van het vaste totaal (zonder randomwaarde) is 0
        }
        return total;
    }

    /**
     * Calculate the hero's power for one turn in a fight.
     *
     * @return total power of the hero for one turn
     */
    public int calcTotalPower() {
        int total = power + inventory.getInvPower();
        if (total >= MIN) {
            total += (int) (Math.random() * 6) + 1;
        } else {
            total = (int) (Math.random() * 6) + 1;
        }
        return total;
    }

    /**
     * Calculate the hero's defense for one turn in a fight.
     *
     * @return total defense of the hero for one turn
     */
    public int calcTotalDefense() {
        int total = defense + inventory.getInvDefense();
        if (total >= MIN) {
            total += (int) (Math.random() * 6) + 1;
        } else {
            total = (int) (Math.random() * 6) + 1;         //Totaal=minimum van het vaste totaal (zonder randomwaarde) is 0
        }
        return total;
    }

    /**
     * Calculate the hero's speed for one turn in a fight.
     *
     * @return total speed of the hero for one turn
     */
    public int calcTotalSpeed() {
        int total = speed + inventory.getInvSpeed();
        if (total >= MIN) {
            total += (int) (Math.random() * 6) + 1;
        } else {
            total = (int) (Math.random() * 6) + 1;         //Totaal=minimum van het vaste totaal (zonder randomwaarde) is 0
        }
        return total;
    }

    /**
     * Give the hero's total basic power. The total basic power is the hero's
     * own power combined with the power that his treasures give him.
     *
     * @return total basic power of the hero
     */
    public int getTotalPower() {
        int total = power + inventory.getInvPower();
        if (total < MIN) {
            total = MIN;
        } else if (total > MAX) {
            total = MAX;
        }
        return total;
    }

    /**
     * Give the hero's total basic awareness. The total basic awareness is the
     * hero's own awareness combined with the awareness that his treasures give
     * him.
     *
     * @return total basic awareness of the hero
     */
    public int getTotalAwareness() {
        int total = awareness + inventory.getInvAwareness();
        if (total < MIN) {
            total = MIN;
        } else if (total > MAX) {
            total = MAX;
        }
        return total;
    }

    /**
     * Give the hero's total basic speed. The total basic speed is the hero's
     * own speed combined with the speed that his treasures give him.
     *
     * @return total basic speed of the hero
     */
    public int getTotalSpeed() {
        int total = speed + inventory.getInvSpeed();
        if (total < MIN) {
            total = MIN;
        } else if (total > MAX) {
            total = MAX;
        }
        return total;
    }

    /**
     * Give the hero's total basic defense. The total basic defense is the
     * hero's own defense combined with the defense that his treasures give him.
     *
     * @return total basic defense of the hero
     */
    public int getTotalDefense() {
        int total = defense + inventory.getInvDefense();
        if (total < MIN) {
            total = MIN;
        } else if (total > MAX) {
            total = MAX;
        }
        return total;
    }

    /**
     * Give highest boundary for a hero's stats
     *
     * @return the maximum for a hero's stats
     */
    public static int getMAX() {
        return MAX;
    }

    /**
     * Give lowest boundary for a hero's stats
     *
     * @return the minimum for a hero's stats
     */
    public static int getMIN() {
        return MIN;
    }

    /**
     * Give maximum characters for the name
     *
     * @return maximum number of characters for the hero's name
     */
    public static int getMAX_NAME() {
        return MAX_NAME;
    }

    /**
     * Give the hero's inventory
     *
     * @return the heroes inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Pick up a treasure. The picked up treasure will be added to the hero's
     * inventory and its value to the hero's score.
     *
     * @param treasure the treasure to be picked up
     */
    public void pickUpTreasure(Treasure treasure) {
        inventory.addTreasure(treasure);
        score += treasure.getValue();
    }

    /**
     * Check if hero kills a monster.
     *
     * @param monster monster that will be attacked by the hero
     * @return true if monster was killed, false if not
     */
    public boolean attack(Monster monster) //true if won, false if not
    {
        if (calcTotalPower() > monster.calcTotalDefense()) {        //Bij gelijkstand wint de verdediger (of random nemen)
            return true;
        }
        return false;
    }

    /**
     * Check if hero can defend against the given monster. 
     * Defending party wins if value of the attacker and value of the defender are equal.
     * @param monster monster that attacks the hero
     * @return true if hero succesfully defend against the monster, false if not
     */
    public boolean defend(Monster monster) {
        if (calcTotalDefense() >= monster.calcTotalPower()) {
            return true;
        }
        alive = false;
        return false;
    }

    /**
     * Check if hero can flee from the given monster.
     *
     * @param monster from which the hero will try to flee
     * @return true if hero succesfully fled from monster, false if not
     */
    public boolean flee(Monster monster) {
        if (calcTotalSpeed() > monster.calcTotalSpeed()) {
            return true;
        }
        return false;
    }

    /**
     * Check if hero has finished the game
     *
     * @return true if hero has finished the game, false if not
     */
    public boolean finishedGame() {
        return (alive && round > GameProcesses.getMaxRound());
    }

}
