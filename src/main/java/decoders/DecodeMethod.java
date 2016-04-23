package decoders;

import exceptions.InvalidCommandPathException;

/**
 * Created by Luigi Sekuiya on 23/04/2016.
 */
public class DecodeMethod {

    public static String decode(String line) throws Exception {
        return decode(line.split(" "));
    }

    public static String decode (String [] args) throws Exception {
        if (args[0] != null && args[0] != "") return args[0];
        else throw new InvalidCommandPathException();

    }

}
