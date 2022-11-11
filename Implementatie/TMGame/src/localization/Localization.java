package localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Pieter-Jan Geeroms
 */
public class Localization {

    public static final Locale NEDERLANDS = new Locale("nl");
    public static final Locale FRANS = Locale.FRENCH;
    public static final Locale ENGELS = Locale.ENGLISH;

    private static final Localization instance = new Localization();

    private static final String MESSAGES_BUNDLE = "MessagesBundle";
    private ResourceBundle messages;
    private static final String EXCEPTIONS_BUNDLE = "ExceptionsBundle";
    private ResourceBundle exceptions;

    private Localization() {
        initLanguage(Locale.getDefault());
    }

    private void initLanguage(Locale locale) {
        messages = ResourceBundle.getBundle(MESSAGES_BUNDLE, locale);
        exceptions = ResourceBundle.getBundle(EXCEPTIONS_BUNDLE, locale);
    }

    public static void setLanguage(Locale locale) {
        instance.initLanguage(locale);
    }

    public static String getMessage(String key) {
        return instance.messages.getString(key);
    }

    public static String getExeptionMesage(String key) {
        return instance.exceptions.getString(key);
    }
}
