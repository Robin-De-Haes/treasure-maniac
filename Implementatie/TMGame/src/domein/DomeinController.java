/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import persistentie.HeroMapper;
import persistentie.MonsterMapper;
import persistentie.TreasureMapper;

/**
 * Contact point for communication with persistence and presentation layer.
 *
 * @author Robin
 */
public class DomeinController {

    private final MonsterMapper monsterMapper;
    private final TreasureMapper treasureMapper;
    private final HeroMapper heroMapper;

    /**
     * Constructor for DomeinController, initializes monsterMapper and
     * treasureMapper
     *
     * @see persistentie.MonsterMapper
     * @see persistentie.TreasureMapper
     *
     */
    public DomeinController() {
        monsterMapper = new MonsterMapper();
        treasureMapper = new TreasureMapper();
        heroMapper = new HeroMapper();
    }

    /**
     * Add a treasure to the database
     *
     * @param treasure the treasure to be added by using {@link persistentie.TreasureMapper#addTreasure(Treasure) addTreasure-method
     * } from treasureMapper
     * @return true if treasure was added succesfully
     */
    public boolean addTreasure(Treasure treasure) {
        return treasureMapper.addTreasure(treasure);
    }

    /**
     * Add a monster to the database
     *
     * @param monster the monster to be added by using {@link persistentie.MonsterMapper#addMonster(Monster) addMonster-method
     * } from monsterMapper
     * @return true if Monster was added succesfully
     */
    public boolean addMonster(Monster monster) {
        return monsterMapper.addMonster(monster);
    }

    /**
     * Delete a treasure in the database
     *
     * @param treasure the treasture to be deleted by using
     * {@link persistentie.TreasureMapper#deleteTreasure(Treasure) deleteTreasure-method}
     * from treasureMapper
     * @return true if treasure was deleted succesfully
     */
    public boolean deleteTreasure(Treasure treasure) {
        return treasureMapper.deleteTreasure(treasure);
    }

    /**
     * Delete a treasure in the database
     *
     * @param id the id of the treasure to be deleted by using
     * {@link persistentie.TreasureMapper#deleteTreasure(int) deleteTreasure-method}
     * from treasureMapper
     * @return true if treasure was deleted succesfully
     */
    public boolean deleteTreasure(int id) {
        return treasureMapper.deleteTreasure(id);
    }

    /**
     * Delete a monster in the database
     *
     * @param monster the monster to be deleted by using {@link persistentie.MonsterMapper#deleteMonster(Monster) deleteMonster-method
     * } from monsterMapper
     * @return true if monster was deleted succesfully
     */
    public boolean deleteMonster(Monster monster) {
        return monsterMapper.deleteMonster(monster);
    }

    /**
     * Delete a monster in the database
     *
     * @param id the id of the monster to be deleted by using {@link persistentie.MonsterMapper#deleteMonster(int) deleteMonster-method
     * } from monsterMapper
     * @return true if monster was deleted succesfully
     */
    public boolean deleteMonster(int id) {
        return monsterMapper.deleteMonster(id);
    }

    /**
     * Update a monster in the database
     *
     * @param monster the monster to be updated by using {@link persistentie.MonsterMapper#updateMonster(Monster) updateMonster-method
     * } from monsterMapper
     * @return true if monster was succesfully updated
     */
    public boolean updateMonster(Monster monster) {
        return monsterMapper.updateMonster(monster);
    }

    /**
     * Update a treasure in the database
     *
     * @param treasure the treasure to be updated by using
     * {@link persistentie.TreasureMapper#updateTreasure(Treasure) updateTreasure-method}
     * from treasureMapper
     * @return true if treasure was succesfully updated
     */
    public boolean updateTreasure(Treasure treasure) {
        return treasureMapper.updateTreasure(treasure);
    }

    /**
     * Give a list of all monsters in the database
     *
     * @return a list with all Monsters in database by using {@link persistentie.MonsterMapper#searchAllMonsters() searchAllMonsters-method
     * } from monsterMapper
     */
    public List<Monster> searchAllMonsters() {
        List<Monster> temp = monsterMapper.searchAllMonsters();
        
        //Baas weer verwijderen. remove(monsterMapper.searchBoss()) werkt niet omdat de boss in searchAllMonsters niet verwijst naar de boss van searchBoss().
        //Het zijn 2 verschillende objecten
        Monster boss=monsterMapper.searchBoss();
        for(Monster m:temp)
        {
            if(m.equals(boss))
            {
                temp.remove(m);             //Er zit maar 1 monster in
                break;
            }
        }

        return temp;
    }

    /**
     * Give a list of all treasures in the database
     *
     * @return a list with all Treasures in database by using
     * {@link persistentie.TreasureMapper#searchAllTreasures() searchAllTreasures()}
     * from treasureMapper
     */
    public List<Treasure> searchAllTreasures() {
        return treasureMapper.searchAllTreasures();
    }

    /**
     * Add a treasure to a monster using {@link persistentie.MonsterMapper#addTreasureToMonster(Monster,Treasure) addTreasureToMonster-method
     * } from monsterMapper
     *
     * @param monster the monster to which we'll try to link a treasure
     * @param treasure the treasure that will be linked to the monster
     * @return true if treasure was succesfully linked to the monster, false if
     * not
     */
    public boolean addTreasureToMonster(Monster monster, Treasure treasure) {
        return monsterMapper.addTreasureToMonster(monster, treasure);
    }

    /**
     * Remove a treasure from a monster using {@link persistentie.MonsterMapper#removeTreasureFromMonster(Monster,Treasure) removeTreasureFromMonster-method
     * } from monsterMapper
     *
     * @param monster the monster from which we'll try to remove a treasure
     * @param treasure the treasure that will be removed from the monster
     * @return true if treasure was succesfully removed from the monster, false
     * if not
     */
    public boolean removeTreasureFromMonster(Monster monster, Treasure treasure) {
        return monsterMapper.removeTreasureFromMonster(monster, treasure);
    }

    /**
     * Give a list of all treasures connected to a monster
     *
     * @param id the id of the monster you want to get the treasures from
     * @return a list of all the treasures a monster guards by using
     * {@link persistentie.MonsterMapper#searchAllTreasuresFromMonster(int) removeTreasureFromMonster-method }
     * from monsterMapper
     */
    public List<Treasure> searchAllTreasuresFromMonster(int id) {
        return monsterMapper.searchAllTreasuresFromMonster(id);
    }

    /**
     * Give a list of all treasures connected to a monster
     *
     * @param monster the monster you want to get the treasures from
     * @return a list of all the treasures a monster guards by using
     * {@link persistentie.MonsterMapper#searchAllTreasuresFromMonster(Monster) searchAllTreasuresFromMonster-method }
     * from monsterMapper
     */
    public List<Treasure> searchAllTreasuresFromMonster(Monster monster) {
        return monsterMapper.searchAllTreasuresFromMonster(monster);
    }

    public Monster searchBoss() {
        return monsterMapper.searchBoss();
    }

    /**
     * Check connection of treasure with monster(s)
     *
     * @param id the id of the treasure of which you want to check if it's
     * linked with a monster using
     * {@link persistentie.TreasureMapper#isUnconnected(int) isUnconnected-method}
     * from TreasureMapper
     * @return 1 if unconnected, 0 otherwise or -1 in case an unexpected error
     * occurred
     *
     */
    public int isUnconnectedToMonsters(int id) {
        return treasureMapper.isUnconnectedToMonsters(id);
    }

        /**
     * Check connection of treasure with monster(s)
     *
     * @param id the id of the treasure of which you want to check if it's
     * linked with a monster using
     * @return 1 if unconnected, 0 otherwise or -1 in case an unexpected error
     * occurred
     *
     */
    public int isUnconnectedToHeroes(int id) {
        return treasureMapper.isUnconnectedToHeroes(id);
    }

    /**
     * Check connection of monster with treasure(s)
     *
     * @param id the id of the monster of which you want to check if is still
     * has treasures linked to it using the {@link persistentie.MonsterMapper#isUnconnected(int) isUnconnected-method
     * } from monsterMapper
     * @return 1 if unconnected, 0 otherwise or -1 in case an unexpected error
     * occurred
     *
     */
    public int isUnconnectedMonster(int id) {
        return monsterMapper.isUnconnected(id);
    }

    /**
     * Add a treasure to a monster by using {@link persistentie.MonsterMapper#addTreasureToMonster(int,int) addTreasureToMonster-method
     * } from monsterMapper
     *
     * @param monsterId the id of the monster that will be added a treasure to
     * @param treasureId the id of the treasure that will be added to the
     * monster
     *
     * @return true if linking was succesfull, false if not
     *
     */
    public boolean addTreasureToMonster(int monsterId, int treasureId) {
        return monsterMapper.addTreasureToMonster(monsterId, treasureId);
    }

    /**
     * Remove a treasure from a monster by using {@link persistentie.MonsterMapper#removeTreasureFromMonster(int,int) removeTreasureFromMonster-method
     * } from monsterMapper
     *
     * @param monsterId the id of the monster of which a treasure will be
     * removed from
     * @param treasureId the id of the treasure that will be removed from the
     * monster
     *
     * @return true if the link was succesfully broken, false if not
     *
     */
    public boolean removeTreasureFromMonster(int monsterId, int treasureId) {
        return monsterMapper.removeTreasureFromMonster(monsterId, treasureId);
    }

    /**
     * Check if treasure already exists in the database
     *
     * @param treasure the treasure whose presence in the database has to be
     * checked
     * @return true is treasure doesn't yet exist in the database, false if it
     * does
     */
    public boolean isNewTreasure(Treasure treasure) {
        List<Treasure> treasures = treasureMapper.searchAllTreasures();

        for (Treasure t : treasures) {
            if (t.equals(treasure)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if monster already exists in the database
     *
     * @param monster the monster whose presence in the database has to be
     * checked
     * @return true is monster doesn't yet exist in the database, false if it
     * does
     */
    public boolean isNewMonster(Monster monster) {
        List<Monster> monsters = monsterMapper.searchAllMonsters();

        for (Monster m : monsters) {
            if (m.equals(monster)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add or update a hero in the database
     *
     * @param hero hero to be added or updated
     * @return true if addition or update was succesful, false if not
     */
    public boolean addHero(Hero hero) {
        if (heroMapper.isPresent(hero)) {
            return heroMapper.updateHero(hero);
        }
        return heroMapper.addHero(hero);
    }

    /**
     * Give all heroes in the database.
     *
     * @return a list of all heroes in the database
     */
    public List<Hero> searchAllHeroes() {
        return heroMapper.searchAllHeroes();
    }

    /**
     * Give last added hero.
     *
     * @return the hero that was last added to the database
     */
    public Hero getLastHero() {
        return heroMapper.getLastHero();
    }

    /**
     * Link a treasure to a hero in the database.
     *
     * @param hero the hero who will receive a treasure
     * @param treasure the treasure who will be added to the hero
     * @return true if linking was succesful, false if not
     */
    public boolean addTreasureToHero(Hero hero, Treasure treasure) {
        if (!heroMapper.heroHasTreasure(treasure)) {
            return heroMapper.addTreasureToHero(hero.getId(), treasure.getId(), false);
        } else {
            return heroMapper.addTreasureToHero(hero.getId(), treasure.getId(), true);
        }
    }

    /**
     * Give all treasures linked to the hero
     *
     * @param hero the hero whose treasures will be searched
     * @return a list with all the treasures linked to the hero
     */
    public List<Treasure> searchAllTreasuresFromHero(Hero hero) {
        return heroMapper.searchAllTreasuresFromHero(hero.getId());
    }

    /**
     * Remove all treasures linked to the hero.
     *
     * @param hero the hero whose treasures should be removed from him
     * @return true if operation was succesful, false if not
     */
    public boolean removeAllTreasuresFromHero(Hero hero) {
        return heroMapper.removeAllTreasuresFromHero(hero.getId());
    }

    /**
     * Delete a hero.
     *
     * @param hero the hero to be deleted
     * @return true if operation was succesful, false if not
     */
    public boolean deleteHero(Hero hero) {
        return heroMapper.deleteHero(hero.getId());
    }

    /**
     * Remove the possibility to continue with heroes who were created before
     * the given hero. This will happen by killing them if they haven't already
     * won the game.
     *
     * @param hero the hero whose predecessors shouldn't be able to continue
     * @return true if operation was succesful, false if not
     */
    public boolean removePreviousHeroes(Hero hero) {
        return heroMapper.disablePreviousHeroes(hero.getId(), GameProcesses.getMaxRound());
    }
}
