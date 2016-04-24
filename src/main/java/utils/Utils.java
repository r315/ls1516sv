package utils;

/**
 * Created by Luigi Sekuiya on 22/04/2016.
 */
public class Utils {

    public static int getInt (Object value) {
        int i;
        try{
            i = Integer.parseInt(getString(value));
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }

        return i;
    }

    public static String getString (Object value) {
        return value.toString();
    }


}
