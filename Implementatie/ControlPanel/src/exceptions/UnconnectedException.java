package exceptions;
import localization.Localization;
/**
 *
 * @author Simon
 */
//Als er geen connectie bestaat
public class UnconnectedException extends IllegalArgumentException {

    /**
     * Constructor for UnconnectedException in which you can choose the
     * exception's message.
     */
    public UnconnectedException() {
        super(Localization.getExeptionMesage("ex_unconnected"));
    }

    /**
     * Constructor for UnconnectedException in which you can choose the
     * exception's message
     *
     * @param message information given to the user when exception is thrown
     */
    public UnconnectedException(String message) {
        super(message);
    }

}
