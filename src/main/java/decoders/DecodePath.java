package decoders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import exceptions.InvalidCommandPathException;

/**
 * Created by Luigi Sekuiya on 23/04/2016.
 */
public class DecodePath {

    public static Collection<String> decode(String line) throws InvalidCommandPathException {
        return decode( line.split(" "));
    }

    public static Collection<String> decode (String [] args) throws InvalidCommandPathException {

        if (args.length >= 2 && args[1] != null && args[1].length() != 0) return decodePath(args[1]);
        else throw new InvalidCommandPathException();

    }

    public static Collection<String> decodePath(String path) {
        String [] pathS = path.split("/");
        Collection<String> resources = new ArrayList<>();

        resources.addAll(Arrays.asList(pathS).subList(1, pathS.length));
        return resources;
    }
}
