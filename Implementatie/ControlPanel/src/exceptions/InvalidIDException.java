package exceptions;
import localization.Localization;
/**
 * Exception for not filling in a correct ID
 *
 * @deprecated ID's don't have to be filled in manually anymore
 * @author Robin
 */
//Exception wordt niet meer gebruikt
public class InvalidIDException extends IllegalArgumentException {

    /**
     * Default constructor for InvalidIDException.
     */
    public InvalidIDException() {
        super(Localization.getExeptionMesage("ex_invalidId"));
    }

    /**
     * Constructor for InvalidException in which you can choose the exception's
     * message
     *
     * @param message information given to the user when exception is thrown
     */
    public InvalidIDException(String message) {
        super(message);
    }

}
