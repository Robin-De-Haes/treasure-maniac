package exceptions;

import localization.Localization;

/**
 * Exception for not entering a value in the valid range
 *
 * @author Robin
 */
//Als er buiten de grenzen wordt gegaan
public class OutOfRangeException extends IllegalArgumentException {

    /**
     * Default constructor for OutOfRangeException.
     */
    public OutOfRangeException() {
        super(Localization.getExeptionMesage("ex_outOfRange"));
        //super("Out of range: Respect the boundaries!");
    }

    /**
     * Constructor for OutOfRangeException in which you can choose the
     * exception's message
     *
     * @param message information given to the user when exception is thrown
     */
    public OutOfRangeException(String message) {
        super(message);
    }

}
