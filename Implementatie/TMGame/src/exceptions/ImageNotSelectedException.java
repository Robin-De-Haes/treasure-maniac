package exceptions;

/**
 * Exception for not selecting an image
 *
 * @author Robin
 */
//Als er geen afbeelding geselecteerd is in de combobox
public class ImageNotSelectedException extends IllegalArgumentException {

    /**
     * Default constructor for ImageNotSelectedException.
     */
    public ImageNotSelectedException() {
        super("Choose an image!");
    }

    /**
     * Constructor for ImageNotSelectedException in which you can choose the
     * exception's message
     *
     * @param message information given to the user when exception is thrown
     */
    public ImageNotSelectedException(String message) {
        super(message);
    }

}
