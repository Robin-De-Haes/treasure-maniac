package domein;

import java.util.Random;

/**
 *
 * @author pieterjan
 */
public class Utility {

    // Usually this can be a field rather than a method variable
    private static final Random rand = new Random();

    public enum OS {

        Macintosh, Windows, Unix, Unknown;
    };

    public static int generateRandom(int min, int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static Enum getOperatingSystem() {
        String sOS = System.getProperty("os.name").toLowerCase();
        Enum eOS;
        if (sOS.contains("mac")) {
            eOS = OS.Macintosh;
        } else if (sOS.contains("win")) {
            eOS = OS.Windows;
        } else if (sOS.contains("nix") || sOS.contains("nux") || sOS.contains("aix")) {
            eOS = OS.Unix;
        } else {
            eOS = OS.Unknown;
        }
        return eOS;
    }
}
