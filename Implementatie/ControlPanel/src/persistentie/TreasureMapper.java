//Iteratie 2
package persistentie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domein.Treasure;

/**
 * Mapper for treasures
 *
 * @author Robin De Haes
 * @see Treasure
 */
public class TreasureMapper {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/treasuremaniac?user=tm&password=tm";

    /**
     * Adds a treasure to the database
     *
     * @param treasure treasure to be added
     * @return true if adding is succesful, false if an error occurred
     */
    public boolean addTreasure(Treasure treasure) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryNewTreasure = conn.prepareStatement(
                    "INSERT INTO TREASURES VALUES (?,?,?,?,?,?,?,?,?)");
            queryNewTreasure.setInt(1, 0);
            queryNewTreasure.setString(2, treasure.getName());
            queryNewTreasure.setInt(3, treasure.getValue());
            queryNewTreasure.setString(4, treasure.getDescription());
            queryNewTreasure.setInt(5, treasure.getPower());
            queryNewTreasure.setInt(6, treasure.getDefense());
            queryNewTreasure.setInt(7, treasure.getSpeed());
            queryNewTreasure.setInt(8, treasure.getAwareness());
            queryNewTreasure.setString(9, treasure.getAvatar());

            queryNewTreasure.executeUpdate();
            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }

    }

    /**
     * Update an existing treasure in the database
     *
     * @param treasure treasure to be updated
     * @return true if update is succesful, false if an error occurred
     */
    public boolean updateTreasure(Treasure treasure) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryUpdateTreasure = conn.prepareStatement("UPDATE TREASURES SET name = ?, goldvalue = ?,"
                    + "description = ?, power = ?, defense = ?, speed = ?, awareness = ?, avatar = ? WHERE ID = ?");
            queryUpdateTreasure.setInt(9, treasure.getId());
            queryUpdateTreasure.setString(1, treasure.getName());
            queryUpdateTreasure.setInt(2, treasure.getValue());
            queryUpdateTreasure.setString(3, treasure.getDescription());
            queryUpdateTreasure.setInt(4, treasure.getPower());
            queryUpdateTreasure.setInt(5, treasure.getDefense());
            queryUpdateTreasure.setInt(6, treasure.getSpeed());
            queryUpdateTreasure.setInt(7, treasure.getAwareness());
            queryUpdateTreasure.setString(8, treasure.getAvatar());
            queryUpdateTreasure.executeUpdate();
            return true;

        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Delete a treasure from the database
     *
     * @param treasure the treasure to be deleted
     * @return true if deletion is succesful, false if an error occurred
     */
    public boolean deleteTreasure(Treasure treasure) {
        return deleteTreasure(treasure.getId());
    }

    /**
     * Delete a treasure from the database
     *
     * @param id id of treasure to be deleted
     * @return true if deletion is succesful, false if an error occurred
     */
    public boolean deleteTreasure(int id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryDeleteTreasure = conn.prepareStatement("DELETE FROM TREASURES WHERE ID = ?");

            //First remove all possible connections with treasures from monster
            deleteAllLinks(id);
            deleteAllHeroLinks(id);         //delete all links with monster
            //End removing connections from monster

            queryDeleteTreasure.setInt(1, id);
            queryDeleteTreasure.executeUpdate();
            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Search for all treasures in the database
     *
     * @return a list of all treasures
     */
    public List<Treasure> searchAllTreasures() {
        List<Treasure> treasures = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllTreasures = conn.prepareStatement("SELECT * FROM TREASURES");
            try (ResultSet rs = queryAllTreasures.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int value = rs.getInt("goldvalue");
                    String description = rs.getString("description");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");
                    treasures.add(new Treasure(id, name, value, description, power, defense, speed, awareness, avatar));
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
     * Search for a treasure in the database
     *
     * @param id id of treasure that should be searched for
     * @return the found treasure or null if an error occurred
     */
    public Treasure searchTreasure(int id) {
        Treasure treasure = null;

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement querySearchTreasure = conn.prepareStatement("SELECT * FROM TREASURES WHERE ID = ?");
            querySearchTreasure.setInt(1, id);
            try (ResultSet rs = querySearchTreasure.executeQuery()) {
                if (rs.next()) { // Als er een resultaat gevonden is.
                    int treasureId = rs.getInt("id");
                    String name = rs.getString("name");
                    int value = rs.getInt("goldvalue");
                    String description = rs.getString("description");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");
                    treasure = new Treasure(treasureId, name, value, description, power, defense, speed, awareness, avatar);
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }

        return treasure;
    }

    /**
     * Test if a treasure is connected to monster(s)
     *
     * @param id the id of the treasure whose connection is tested
     * @return 0 if is connected, 1 if it isn't and -1 if an unexpected error
     * occurred
     */
    public int isUnconnectedToMonsters(int id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllLinks = conn.prepareStatement(
                    "select * from Treasures_has_Monsters\n"
                    + "WHERE Treasure_id=?");

            queryAllLinks.setInt(1, id);
            try (ResultSet rs = queryAllLinks.executeQuery()) {
                if (rs.next()) {
                    return 0;                   //Er bestaat zeker één connectie met een treasure
                }
                return 1;                       //Er is geen connectie met een treasure gevonden
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return -1;              //-1 bij een error
        }
    }

    /**
     * Test if a treasure is connected to hero(es)
     *
     * @param id the id of the treasure whose connection is tested
     * @return 0 if is connected, 1 if it isn't and -1 if an unexpected error
     * occurred
     */
    public int isUnconnectedToHeroes(int id) {                              //Als het nog verbonden is met heroes (apart checken)
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllLinks = conn.prepareStatement(
                    "select * from Heroes_has_Treasures\n"
                    + "WHERE Treasure_id=?");

            queryAllLinks.setInt(1, id);
            try (ResultSet rs = queryAllLinks.executeQuery()) {
                if (rs.next()) {
                    return 0;                   //Er bestaat zeker één connectie met een treasure
                }
                return 1;                       //Er is geen connectie met een treasure gevonden
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return -1;              //-1 bij een error
        }
    }

    /**
     * Break all links between a treasure and the monster(s) connected to it
     *
     * @param id the id of the treasure whose links will be broken
     * @return true if breaking the links was succesful, false if not
     */
    private Boolean deleteAllLinks(int id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryDeleteAllLinks = conn.prepareStatement(
                    "delete from Treasures_has_Monsters\n"
                    + "WHERE Treasure_id=?");

            queryDeleteAllLinks.setInt(1, id);
            queryDeleteAllLinks.executeUpdate();
            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Break all links between a treasure and the hero(es) connected to it
     *
     * @param id the id of the treasure whose links will be broken
     * @return true if breaking the links was succesful, false if not
     */
    private Boolean deleteAllHeroLinks(int id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryDeleteAllLinks = conn.prepareStatement(
                    "delete from Heroes_has_Treasures\n"
                    + "WHERE Treasure_id=?");

            queryDeleteAllLinks.setInt(1, id);
            queryDeleteAllLinks.executeUpdate();
            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }
}
