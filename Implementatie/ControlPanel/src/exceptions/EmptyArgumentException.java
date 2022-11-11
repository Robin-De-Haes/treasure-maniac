package exceptions;

import localization.Localization;

/**
 * Exception for not filling in mandatory fields
 *
 * @author Robin
 */
//Als een in te vullen veld leeg wordt gelaten
public class EmptyArgumentException extends IllegalArgumentException {

    /**
     * Default constructor for EmptyArgumentException.
     */
    public EmptyArgumentException() {
        super(Localization.getExeptionMesage("ex_emptyArgrument"));
    }

    /**
     * Constructor for EmptyArgumentException in which you can choose the
     * exception's message
     *
     * @param message information given to the user when exception is thrown
     */
    public EmptyArgumentException(String message) {
        super(message);
    }

}
