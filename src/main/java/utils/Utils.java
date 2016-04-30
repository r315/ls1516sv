package utils;

import exceptions.InvalidCommandParametersException;

import java.util.HashMap;

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

    public static HashMap<String, Integer> getSkipTop (String skip, String top) throws InvalidCommandParametersException {
        HashMap<String, Integer> map = new HashMap<>();
        int skipI = 0,topI = 1;

        try {
            skipI = Utils.getInt(skip);
        } catch (NumberFormatException | NullPointerException e) { //If not number OR null
            if (skip != null) throw new InvalidCommandParametersException();
        }

        try {
            topI = Utils.getInt(top);
        } catch (NumberFormatException | NullPointerException e) { //If not number OR null
            if (top != null) throw new InvalidCommandParametersException();
        }

        if (skipI < 0 || topI <= 0) throw new InvalidCommandParametersException();

        map.put("skip",skipI);
        map.put("top",topI);

        return map;
    }


}
