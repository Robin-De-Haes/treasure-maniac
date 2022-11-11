//Iteratie 2
package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import domein.Monster;
import domein.Treasure;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for monsters WORKING
 *
 * @author Pieter-Jan Geeroms
 */
public class MonsterMapper {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/treasuremaniac?user=tm&password=tm";

    /**
     * Adds a monster
     *
     * @param monster the monster to be added
     * @return true if the monster was added, false if an error occured.
     */
    public Boolean addMonster(Monster monster) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAddMonster = conn.prepareStatement("INSERT INTO MONSTERS VALUES (?,?,?,?,?,?,?)");
            queryAddMonster.setInt(1, 0); // id Database
            queryAddMonster.setString(2, monster.getName());
            queryAddMonster.setInt(3, monster.getPower());
            queryAddMonster.setInt(4, monster.getDefense());
            queryAddMonster.setInt(5, monster.getSpeed());
            queryAddMonster.setInt(6, monster.getAwareness());
            queryAddMonster.setString(7, monster.getAvatar());
            queryAddMonster.executeUpdate();

            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Updates an existing monster
     *
     * @param monster the monster to be updated
     * @return true if monster is updated, false if an error occured
     */
    public Boolean updateMonster(Monster monster) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryUpdateMonster = conn.prepareStatement(
                    "UPDATE MONSTERS SET NAME = ?,POWER = ?, DEFENSE = ?, SPEED = ?, AWARENESS = ?, AVATAR = ? WHERE ID = ?");
            //queryUpdateSchat.setString(1, monster.getOmschrijving());
            queryUpdateMonster.setString(1, monster.getName());
            queryUpdateMonster.setInt(2, monster.getPower());
            queryUpdateMonster.setInt(3, monster.getDefense());
            queryUpdateMonster.setInt(4, monster.getSpeed());
            queryUpdateMonster.setInt(5, monster.getAwareness());
            queryUpdateMonster.setString(6, monster.getAvatar());
            queryUpdateMonster.setInt(7, monster.getId());
            queryUpdateMonster.executeUpdate();

            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * delete an existing monster
     *
     * @param monster the monster to be deleted
     * @return uses the {@link #deleteMonster(int id)} method for deleting
     * monster
     */
    public boolean deleteMonster(Monster monster) {
        return deleteMonster(monster.getId());
    }

    /**
     * Delete monster by id
     *
     * <p>
     * All links with the treasures it guards will first be broken</p>
     *
     * @param id id of the monster to be deleted
     * @return true if deleted, false if an error occurred
     */
    public boolean deleteMonster(int id) {          //Kijk naar return-waarde executeUpdate om te zien of alle schatten wegzijn
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryDeleteMonster = conn.prepareStatement("DELETE FROM MONSTERS WHERE id = ?");
            //
            //
            /*
             Remove treasure first
             */
            List<Treasure> treasures = searchAllTreasuresFromMonster(id);
            for (Treasure t : treasures) {
                removeTreasureFromMonster(id, t.getId());
            }
            /*
             End removing treasures
             */
            //Remove monster
            queryDeleteMonster.setInt(1, id);
            queryDeleteMonster.executeUpdate();
            //End remove monster
            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Search for all monsters
     *
     * @return a list of type {@link <Monster>} containing all found monsters
     */
    public List<Monster> searchAllMonsters() {
        List<Monster> monsters = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllMonsters = conn.prepareStatement("SELECT * FROM MONSTERS");
            try (ResultSet rs = queryAllMonsters.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");
                    monsters.add(new Monster(id, name, power, defense, speed, awareness, avatar));
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return monsters;
    }

    /**
     * Give the boss. Searches for the monster whose avatar is Boss.gif
     *
     * @return the final boss in the game
     * @author Robin
     */
    public Monster searchBoss() {
        Monster boss = null;

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement querySearch = conn.prepareStatement("SELECT * FROM MONSTERS WHERE AVATAR = ?");
            querySearch.setString(1, "Boss.gif");
            try (ResultSet rs = querySearch.executeQuery()) {
                if (rs.next()) { // Als er een resultaat gevonden is.
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");
                    boss = new Monster(id, name, power, defense, speed, awareness, avatar);
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return boss;
    }

    /**
     * Search for all the treasures of a certain monster
     *
     * @param monster the monster used to search all treasures bound to it.
     * @return uses the method {@link #searchAllTreasuresFromMonster(int id)}
     */
    public List<Treasure> searchAllTreasuresFromMonster(Monster monster) {
        return searchAllTreasuresFromMonster(monster.getId());
    }

    /**
     * Search for all the treasures of a certain monster
     *
     * @param id id of the monster to search all treasures bound to it.
     * @return a list of type {@link <Treasure>} containing all treasures of the
     * monster, false if an error occured
     */
    public List<Treasure> searchAllTreasuresFromMonster(int id) {
        List<Treasure> treasures = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllMonsters = conn.prepareStatement(
                    "SELECT t.* from Treasures t\n"
                    + "INNER JOIN Treasures_has_Monsters tm ON tm.Treasure_id=t.id\n"
                    + "WHERE Monster_id=?");

            queryAllMonsters.setInt(1, id);
            try (ResultSet rs = queryAllMonsters.executeQuery()) {
                while (rs.next()) {
                    int treasureid = rs.getInt("id");
                    String name = rs.getString("name");
                    int goldvalue = rs.getInt("goldvalue");
                    String description = rs.getString("description");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");

                    treasures.add(new Treasure(treasureid, name, goldvalue, description,
                            power, defense, speed, awareness, avatar));
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return treasures;
    }

    /**
     * Add a treasure to a monster
     *
     * @param monster monster where a treasure should be added to
     * @param treasure treasure that should be added to the monster
     * @return uses the method
     * {@link #addTreasureToMonster(int monsterId, int treasureId)} to add a
     * monster by id
     */
    public Boolean addTreasureToMonster(Monster monster, Treasure treasure) {
        return addTreasureToMonster(monster.getId(), treasure.getId());
    }

    /**
     * Add a treasure to a monster
     *
     * @param monsterId id of monster where a treasure should be added to
     * @param treasureId id of treasure that should be added to the monster
     * @return true if succesful, false if an error occured
     */
    public Boolean addTreasureToMonster(int monsterId, int treasureId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryLink = conn.prepareStatement("INSERT INTO Treasures_has_Monsters VALUES (?,?)");
            queryLink.setInt(1, treasureId);
            queryLink.setInt(2, monsterId);
            queryLink.executeUpdate();

            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Remove a treasure from a monster
     *
     * @param monster monster where a treasure should be removed from
     * @param treasure treasure that should be removed from the monster
     * @return uses the method
     * {@link #addTreasureToMonster(int monsterId, int treasureId)} to remove a
     * treasure from a monster by id
     */
    public Boolean removeTreasureFromMonster(Monster monster, Treasure treasure) {
        return removeTreasureFromMonster(monster.getId(), treasure.getId());
    }

    /**
     * Remove a treasure from a monster
     *
     * @param monsterId id of monster where a treasure should be removed from
     * @param treasureId id of treasure that should be removed from the monster
     * @return true if removal was succesfull, false if an error occurred
     */
    public Boolean removeTreasureFromMonster(int monsterId, int treasureId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryRemoveLink = conn.prepareStatement("DELETE FROM Treasures_has_Monsters WHERE Treasure_id=? AND Monster_id=?");
            queryRemoveLink.setInt(1, treasureId);
            queryRemoveLink.setInt(2, monsterId);
            queryRemoveLink.executeUpdate();
            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Test if a monster is connected to treasure(s)
     *
     * @param id the id of the monster whose connection is tested
     * @return 0 if is connected, 1 if it isn't and -1 if an unexpected error
     * occurred
     */
    public int isUnconnected(int id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllLinks = conn.prepareStatement(
                    "select * from Treasures_has_Monsters\n"
                    + "WHERE Monster_id=?");

            queryAllLinks.setInt(1, id);
            try (ResultSet rs = queryAllLinks.executeQuery()) {
                if (rs.next()) {
                    return 0;
                }
                return 1;
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return -1;              //-1 bij een error
        }
    }
}
