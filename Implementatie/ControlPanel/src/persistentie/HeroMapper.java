/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistentie;

import domein.Hero;
import domein.Treasure;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the heroes
 *
 * @author Robin
 */
public class HeroMapper {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/treasuremaniac?user=tm&password=tm";

    /**
     * Add a hero to the database
     *
     * @param hero hero to be added to the database
     * @return true if addition was succesful, false if not
     */
    public Boolean addHero(Hero hero) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAddHero = conn.prepareStatement("INSERT INTO HEROES VALUES (?,?,?,?,?,?,?,?,?,?)");
            queryAddHero.setInt(1, 0); // id Database
            queryAddHero.setString(2, hero.getName());
            queryAddHero.setInt(3, hero.getPower());
            queryAddHero.setInt(4, hero.getDefense());
            queryAddHero.setInt(5, hero.getSpeed());
            queryAddHero.setInt(6, hero.getAwareness());
            queryAddHero.setString(7, hero.getAvatar());
            queryAddHero.setInt(8, hero.getScore());
            queryAddHero.setInt(9, hero.getRound());
            queryAddHero.setBoolean(10, hero.getAlive());
            queryAddHero.executeUpdate();

            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Give the hero that was last added to the database
     *
     * @return last added hero
     */
    public Hero getLastHero() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryLastHero = conn.prepareStatement("SELECT * FROM HEROES ORDER BY ID DESC LIMIT 0, 1");
            try (ResultSet rs = queryLastHero.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");
                    int score = rs.getInt("score");
                    int round = rs.getInt("round");
                    boolean alive = rs.getBoolean("alive");
                    Hero hero = new Hero(id, name, avatar, power, defense, speed, awareness, score, round, alive);
                    return hero;
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return null;            //null als er geen heroes aanwezig waren
    }

    public List<Hero> searchAllHeroes() {
        List<Hero> heroes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllHeroes = conn.prepareStatement("SELECT * FROM HEROES");
            try (ResultSet rs = queryAllHeroes.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");
                    int score = rs.getInt("score");
                    int round = rs.getInt("round");
                    boolean alive = rs.getBoolean("alive");
                    Hero hero = new Hero(id, name, avatar, power, defense, speed, awareness, score, round, alive);
                    heroes.add(hero);
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return heroes;
    }

    /**
     * Check if hero is present in the database.
     *
     * @param hero hero whose presence in the database will be checked
     * @return true is the hero is present in the database, false if not
     */
    public boolean isPresent(Hero hero) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryPresent = conn.prepareStatement("SELECT * FROM HEROES WHERE ID=?");

            queryPresent.setInt(1, hero.getId());
            try (ResultSet rs = queryPresent.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
                return false;
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;              //-1 bij een error
        }
    }

    /**
     * Update hero.
     *
     * @param hero hero to be updated
     * @return true if update was succesful, false if not
     */
    public boolean updateHero(Hero hero) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryUpdate = conn.prepareStatement("UPDATE HEROES SET name = ?, round = ?,"
                    + "score = ?, power = ?, defense = ?, speed = ?, awareness = ?, avatar = ?,alive=? WHERE ID = ?");
            queryUpdate.setInt(10, hero.getId());
            queryUpdate.setString(1, hero.getName());
            queryUpdate.setInt(2, hero.getRound());
            queryUpdate.setInt(3, hero.getScore());
            queryUpdate.setInt(4, hero.getPower());
            queryUpdate.setInt(5, hero.getDefense());
            queryUpdate.setInt(6, hero.getSpeed());
            queryUpdate.setInt(7, hero.getAwareness());
            queryUpdate.setString(8, hero.getAvatar());
            queryUpdate.setBoolean(9, hero.getAlive());
            queryUpdate.executeUpdate();
            return true;

        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Check if hero has a given treasure.
     *
     * @param treasure whose connection with hero will be checked
     * @return true if hero is connected to the treasure, false if not
     */
    public boolean heroHasTreasure(Treasure treasure) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryFind = conn.prepareStatement(
                    "SELECT t.* from Treasures t\n"
                    + "INNER JOIN Heroes_has_Treasures tm ON tm.Treasure_id=t.id\n"
                    + "WHERE Treasure_id=?");

            queryFind.setInt(1, treasure.getId());
            try (ResultSet rs = queryFind.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Create a link between a treasure and a hero.
     *
     * @param heroId id of the hero to be linked
     * @param treasureId id of the treasure to be linked
     * @param hasTreasure true if hero already has the specified treasure, false
     * if not
     * @return
     */
    public Boolean addTreasureToHero(int heroId, int treasureId, boolean hasTreasure) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {

            int amount = 1;
            PreparedStatement queryAmount = conn.prepareStatement("SELECT amount FROM heroes_has_treasures where Treasure_id=? and Hero_id=?");
            queryAmount.setInt(1, treasureId);
            queryAmount.setInt(2, heroId);
            try (ResultSet rs = queryAmount.executeQuery()) {
                if (rs.next()) {
                    amount = rs.getInt("amount");
                }
            }

            if (hasTreasure) {
                PreparedStatement queryLink = conn.prepareStatement("UPDATE Heroes_has_Treasures SET amount=? where Treasure_id=? and Hero_id=?");
                queryLink.setInt(1, amount + 1);
                queryLink.setInt(2, treasureId);
                queryLink.setInt(3, heroId);
                queryLink.executeUpdate();
            } else {
                PreparedStatement queryLink = conn.prepareStatement("INSERT INTO Heroes_has_Treasures VALUES (?,?,?)");
                queryLink.setInt(3, amount);
                queryLink.setInt(2, treasureId);
                queryLink.setInt(1, heroId);
                queryLink.executeUpdate();
            }

            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Give all the hero's treasures.
     *
     * @param id id of the hero whose treasures will be searched for
     * @return a list containing all the hero's treasures
     */
    public List<Treasure> searchAllTreasuresFromHero(int id) {
        List<Treasure> treasures = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryAllTreasures = conn.prepareStatement(
                    "SELECT t.* from Treasures t\n"
                    + "INNER JOIN Heroes_has_Treasures tm ON tm.Treasure_id=t.id\n"
                    + "WHERE Hero_id=?");

            PreparedStatement queryAmount = conn.prepareStatement("SELECT * FROM heroes_has_treasures where Treasure_id=? and Hero_id=?");

            queryAllTreasures.setInt(1, id);
            try (ResultSet rs = queryAllTreasures.executeQuery()) {
                while (rs.next()) {
                    int treasureId = rs.getInt("id");
                    String name = rs.getString("name");
                    int goldvalue = rs.getInt("goldvalue");
                    String description = rs.getString("description");
                    int power = rs.getInt("power");
                    int defense = rs.getInt("defense");
                    int speed = rs.getInt("speed");
                    int awareness = rs.getInt("awareness");
                    String avatar = rs.getString("avatar");

                    Treasure treasure = new Treasure(treasureId, name, goldvalue, description,
                            power, defense, speed, awareness, avatar);

                    int amount = 1;
                    queryAmount.setInt(1, treasureId);
                    queryAmount.setInt(2, id);

                    ResultSet amountRs = queryAmount.executeQuery();

                    if (amountRs.next()) {
                        amount = amountRs.getInt("amount");
                    }

                    for (int i = 0; i < amount; i++) {
                        treasures.add(treasure);
                    }
                };
            }
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
        }
        return treasures;
    }

    /**
     * Remove all treasures from a hero.
     *
     * @param id id of the hero whose treasures will be removed
     * @return true if deletion is succeful, false if not
     */
    public boolean removeAllTreasuresFromHero(int id) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryDeleteAllLinks = conn.prepareStatement(
                    "delete from Heroes_has_Treasures\n"
                    + "WHERE Hero_id=?");

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
     * Kill the heroes before a given one and below a certain limit
     *
     * @param id the id of the hero whose predecessors will be killed
     * @param limit the roundlimit below which all previous heroes will be
     * killed
     * @return true if operation was succesful, false if not
     */
    public boolean disablePreviousHeroes(int id, int limit) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryUpdate = conn.prepareStatement("UPDATE HEROES SET alive=? where round<? and id<?");
            queryUpdate.setBoolean(1, false);
            queryUpdate.setInt(2, limit + 1);
            queryUpdate.setInt(3, id);
            queryUpdate.executeUpdate();
            return true;

        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Delete a hero.
     *
     * @param id id of the hero to be deleted
     * @return true if deletion was succesful, false if not
     */
    public boolean deleteHero(int id) {          //Kijk naar return-waarde executeUpdate om te zien of alle schatten wegzijn
        try (Connection conn = DriverManager.getConnection(JDBC_URL)) {
            PreparedStatement queryDelete = conn.prepareStatement("DELETE FROM HEROES WHERE id = ?");

            //Remove treasure first
            removeAllTreasuresFromHero(id);

            //Remove hero
            queryDelete.setInt(1, id);
            queryDelete.executeUpdate();

            return true;
        } catch (SQLException ex) {
            for (Throwable t : ex) {
                t.printStackTrace();
            }
            return false;
        }
    }
}
