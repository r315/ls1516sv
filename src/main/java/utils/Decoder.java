package utils;

import exceptions.InvalidCommandException;
import exceptions.InvalidCommandMethodException;
import exceptions.InvalidCommandPathException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;


/**
 * Created by hmr on 18/07/2016.
 */
public class Decoder {

    public static final String HEADER_PAIR_SEPARATOR_TOKEN= ":";
    public static final String HEADER_SEPARATOR_TOKEN= "\\|";
    public static final String PARAMETERS_PAIR_SEPARATOR_TOKEN= "=";
    public static final String PARAMETERS_SEPARATOR_TOKEN= "&";
    public static final String WHITESPACE_TOKEN= " ";

    public static HashMap<String, String> decodeHeaders(String[] args) throws InvalidCommandException{
        return decode(args,HEADER_PAIR_SEPARATOR_TOKEN,HEADER_SEPARATOR_TOKEN);
    }

    public static HashMap<String, String> decodeParameters(String args) throws InvalidCommandException{
        return decodeParameters(args.split(WHITESPACE_TOKEN));
    }

    public static HashMap<String, String> decodeParameters(String[] args) throws InvalidCommandException{
        return decode(args,PARAMETERS_PAIR_SEPARATOR_TOKEN,PARAMETERS_SEPARATOR_TOKEN);
    }

    public static Collection<String> decodePathFromCommand(String line) throws InvalidCommandPathException {
        return decodePath(line.split(WHITESPACE_TOKEN));
    }

    public static Collection<String> decodePath(String [] args) throws InvalidCommandPathException {
        if (args.length >= 2 && args[1] != null && !args[1].isEmpty()) return decodePath(args[1]);
        else throw new InvalidCommandPathException();
    }

    public static Collection<String> decodePath(String path) throws InvalidCommandPathException {
        if (path.charAt(0) == '/') {
            String[] pathS = path.split("/");
            Collection<String> resources = new ArrayList<>();
            if (pathS.length > 0) resources.addAll(Arrays.asList(pathS).subList(1, pathS.length));
            return resources;
        } else throw new InvalidCommandPathException();
    }

    public static String decodeMethod(String line) throws InvalidCommandException {
        if (line == null) throw new InvalidCommandMethodException();
        return decodeMethod(line.split(WHITESPACE_TOKEN));
    }

    public static String decodeMethod(String [] args) throws InvalidCommandException {
        if (args != null && args.length > 0 && args[0] != null && !args[0].isEmpty()) return args[0];
        else throw new InvalidCommandMethodException();
    }

    private static HashMap<String, String> decode(String[] args, String kvSeparator, String paramSeparator) throws InvalidCommandException
    {
        HashMap<String, String> map = new HashMap<>();
        Arrays.stream(args)
                .filter(s -> s.contains(kvSeparator))
                .map(s1 -> s1.split(paramSeparator))
                .flatMap(Arrays::stream)
                .forEach(
                    h -> {
                        String[] sh = h.split(kvSeparator);
                        map.put(sh[0], sh[1]);
                    }
                );
        return map;
    }
}
