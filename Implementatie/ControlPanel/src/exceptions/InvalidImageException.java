package exceptions;
import localization.Localization;
/**
 * Exception for not filling in a valid image-name
 *
 * @deprecated Images don't have to be written manually anymore
 * @author Robin
 */
//Exception wordt niet meer gebruikt
public class InvalidImageException extends IllegalArgumentException {

    /**
     * Default constructor for InvalidImageException.
     */
    public InvalidImageException() {
        super(Localization.getExeptionMesage("ex_invalidImage"));
    }

    /**
     * Constructor for InvalidImageException in which you can choose the
     * exception's message
     *
     * @param message information given to the user when exception is thrown
     */
    public InvalidImageException(String message) {
        super(message);
    }

}
