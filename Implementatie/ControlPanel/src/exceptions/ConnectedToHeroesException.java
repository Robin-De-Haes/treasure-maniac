/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exceptions;

import localization.Localization;

/**
 *
 * @author Robin
 */
public class ConnectedToHeroesException extends IllegalArgumentException{
        public ConnectedToHeroesException() {
        super(Localization.getExeptionMesage("ex_connectedToHeroes"));
    }

    public ConnectedToHeroesException(String message) {
        super(message);
    }

}