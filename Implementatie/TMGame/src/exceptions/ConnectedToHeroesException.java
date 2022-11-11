/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Robin
 */
public class ConnectedToHeroesException extends IllegalArgumentException {

    public ConnectedToHeroesException() {
        super("Treasure is still connected to heroes!");
    }

    public ConnectedToHeroesException(String message) {
        super(message);
    }

}
