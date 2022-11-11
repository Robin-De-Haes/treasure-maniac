package exceptions;

/**
 * Exception for trying to link two objects (a treasure and a monster) that are
 * already linked to each other
 *
 * @author Robin
 */
//Als een in te vullen veld leeg wordt gelaten
public class AlreadyLinkedException extends IllegalArgumentException {

    /**
     * Default constructor for AlreadyLinkedException.
     */
    public AlreadyLinkedException() {
        super("Treasure is already linked to that monster!");
    }

    /**
     * Constructor for AlreadyLinkedException in which you can choose the
     * exception's message
     *
     * @param message information given to the user when exception is thrown
     */
    public AlreadyLinkedException(String message) {
        super(message);
    }

}
