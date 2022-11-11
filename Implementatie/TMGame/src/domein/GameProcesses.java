/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Processes and information needed to play the game.
 *
 * @author Robin
 */
public class GameProcesses {

    private Hero hero;
    private DomeinController controller;

    private static final int maxRound = 10;

    private static List<Treasure> allTreasures;
    private static List<Monster> allMonsters;
    private List<Monster> seenMonsters;
    private List<Treasure> seenTreasures;

    private int rangeM;
    private int rangeT;

    /**
     * Constructor for the gameprocesses of a game.
     *
     * @param hero the hero of the game
     * @param dc the domaincontroller used for communication with the database
     */
    public GameProcesses(Hero hero, DomeinController dc) {
        controller = dc;
        this.hero = hero;
        rangeM = 2;
        rangeT = 8;
        allMonsters = controller.searchAllMonsters();
        allTreasures = controller.searchAllTreasures();
        seenMonsters = new ArrayList();
        seenTreasures = new ArrayList();
    }

    /**
     * Create a random chamber.
     *
     * @return a random chamber that can either be empty, filled with treasures
     * or filled with a monster
     */
    public Chamber createRandomChamber() {
        if (Chamber.getRound() + 1 == maxRound) {         //bossChamber         
            return new Chamber(controller, controller.searchBoss());
        }

        if (Chamber.getRound() > maxRound) {
            return null;            //null teruggeven als het spel afgelopen is
        }

        Random random = new Random();

        List<Treasure> treasures;
        List<Monster> monsters;

        if (random.nextBoolean()) {       //random.nextBoolean bepaalt of een monster wordt gezet in de kamer of niet (als er monsters beschikbaar zijn)
            int range = rangeM;
            monsters = getValidMonsters(range);
            while (monsters.isEmpty()) {
                range += 1;          //range vergroten als ge nix vind
                monsters = getValidMonsters(range);
            }
            Monster monster = monsters.get(random.nextInt(monsters.size()));        //Genereert een int tussen 0 en monstercount-1
            seenMonsters.add(monster);
            return new Chamber(controller, monster);
        } else if (random.nextBoolean()) {
            int range = rangeT;
            treasures = getValidTreasures(range);
            while (treasures.isEmpty()) {
                range += 1;          //zoekrange vergroten (voor treasures is rangeT dan kleiner) als ge nix vind
                treasures = getValidTreasures(range);
            }
            return new Chamber(treasures);          //Enkel treasures selecteren nog
        } else {
            return new Chamber();
        }
    }

    /**
     * Search for valid treasures. This will search for treasures that are in a
     * valid range according the hero's progress.
     *
     * @param range the range in which should be sought
     * @return a list with treasures that are in a valid range
     */
    private List<Treasure> getValidTreasures(int range) {           //range staat standaard op 8
        List<Treasure> tempTreasures = new ArrayList();
        List<Treasure> validTreasures = new ArrayList();
        for (Treasure t : allTreasures) {
            //Delen door 0 mag niet
            if (rangeT / range != 0) {
                tempTreasures.addAll(allTreasures);
                break;
            }
            if (seenTreasures.contains(t) && range == rangeT) //Treasure is nog niet tegengekomen en je moet nog niet in een groter range beginne te zoeken
            {
                continue;
            }
            if (((hero.getTotal()) / (rangeT * (rangeT / range)) >= t.getTotal() && t.getTotal() >= (hero.getTotal()) / (2 * range))
                    || (t.getTotal() == 0
                    && t.getValue() <= Chamber.getRound() * range * 50) && t.getValue() >= Chamber.getRound() * (rangeT * 25) * (rangeT / range)) {
                //Je kan in totaal een 8e van je huidige stats+difference bijkrijgen 
                //de values zijn afgestemd op hoe goed een item is en moeten dus niet apart getest worden
                //tenzij wanneer het gaat om een goudkist die geen stats omhoog doet en enkel een value heeft
                //treasures moeten een sterkte tussen een 8e en een 16e van die van hero+difference hebben
                //of een zuivere value tussen 200 en 400 keer de ronde waarin je zit
                //Indien niets gevonden werd, wordt er in een grotere range gezocht
                tempTreasures.add(t);
            }
        }
        if (tempTreasures.size() >= 2) {
            int maxCount = Utility.generateRandom(1, 2);
            for (int i = 0; i < maxCount; i++) {
                validTreasures.add(tempTreasures.get(Utility.generateRandom(0, tempTreasures.size() - 1)));
            }
        } else if (!tempTreasures.isEmpty()) {
            validTreasures.add(tempTreasures.get(0));
        }

        seenTreasures.addAll(validTreasures);
        return validTreasures;
    }

    /**
     * Search for valid monsters. This will search for monsters that are in a
     * valid range around the hero's strength.
     *
     * @param range the range in which should be sought
     * @return a list with monsters that are in a valid range
     */
    private List<Monster> getValidMonsters(int range) {
        List<Monster> validMonsters = new ArrayList();
        boolean valid;
        for (Monster m : allMonsters) {
            if (seenMonsters.contains(m) && range == rangeM) //Monster is nog niet gezien
            {
                continue;
            }
            valid = true;
            if (m.getDefense() + 1 >= hero.getTotalPower() + 6 && m.getSpeed() + 1 >= hero.getTotalSpeed() + 6 && hero.getDefense() + 1 >= m.getPower() + 6) {
                valid = false;
                //Eeuwig vastzitten in gevecht, mag nooit gebeuren (hero kan monster niet doden, kan niet vluchten en niet gedood worden door monster)
            }
            if (range <= 10 * rangeM) {
                //Testen of het te sterk is
                if (m.getPower() + 1 > hero.getTotalDefense() + 6 && !(m.getAwareness() + 1 <= hero.getTotalAwareness() + 6 && m.getSpeed() + 1 < hero.getTotalSpeed() + 6)) {
                    valid = false;
                    //Hero sterft altijd
                } //Testen of het te zwak is (Monster kan nooit winnen)
                if (hero.getTotalPower() + 1 > m.getDefense() + 6 && !(m.getAwareness() >= hero.getTotalAwareness() && m.getPower() + 6 >= hero.getTotalDefense() + 1)) {
                    valid = false;
                } else if (hero.getTotalDefense() + 1 >= m.getPower() + 6) {
                    valid = false;
                }
            }
            if (valid && (hero.getTotal() >= m.getTotal() + (m.getTotal() * rangeM / 4) - (m.getTotal() * range / 8) //hero is 1.25 keer sterker
                    && hero.getTotal() <= m.getTotal() + (m.getTotal() * range / 2) - (m.getTotal() * rangeM / 4) //monster is niet te zwak (hero is niet meer dan 1.5 keer sterker)
                    )) {
                //Monsters moeten minstens qua totale sterkte een beetje onder die van hero liggen, maar ook niet te ver
                //en range wordt vergroot indien er geen geldige monsters gevonden zijn
                validMonsters.add(m);
            }
        }
        seenMonsters.addAll(validMonsters);
        return validMonsters;
    }

    /**
     * Check if the highest round has been reached.
     *
     * @return true if the highest round has been reached
     */
    public static boolean isMaxRound() {
        return Chamber.getRound() >= maxRound;
    }

    /**
     * Give the highest possible round.
     *
     * @return the highest possible round to be reached
     */
    public static int getMaxRound() {
        return maxRound;
    }

}
