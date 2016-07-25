package utils;

import exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;


/**
 * Created by hmr on 18/07/2016.
 */
public class Decoder {

    public static final String HEADER_PAIR_SEPARATOR_TOKEN= ":";
    public static final String HEADER_SEPARATOR_TOKEN= "\\|";
    public static final String PARAMETERS_PAIR_SEPARATOR_TOKEN= "=";
    public static final String PARAMETERS_SEPARATOR_TOKEN= "&";
    public static final String WHITESPACE_TOKEN= " ";
    public static final String PATH_SEPARATOR_TOKEN = "/";

    public static HashMap<String, String> decodeHeaders(String[] args) throws InvalidCommandException{
        if(args.length > 4) throw new InvalidCommandException("Invalid number of arguments!");
        if(args.length < 3) return new HashMap<>();
        if(args.length==4 && !args[2].contains(HEADER_PAIR_SEPARATOR_TOKEN)) throw new InvalidCommandHeadersException();
        return decode(args[2], HEADER_PAIR_SEPARATOR_TOKEN, HEADER_SEPARATOR_TOKEN, InvalidCommandHeadersException::new);
    }

    public static HashMap<String, String> decodeParameters(String args) throws InvalidCommandException{
        if(!args.contains(PARAMETERS_PAIR_SEPARATOR_TOKEN))throw new InvalidCommandParametersException();
        return decode(args, PARAMETERS_PAIR_SEPARATOR_TOKEN, PARAMETERS_SEPARATOR_TOKEN, InvalidCommandParametersException::new);
    }

    public static HashMap<String, String> decodeParameters(String[] args) throws InvalidCommandException{
        if(args.length > 4) throw new InvalidCommandException("Invalid number of arguments!");
        if(args.length < 3) return new HashMap<>();
        if(args.length==4 && !args[3].contains(PARAMETERS_PAIR_SEPARATOR_TOKEN)) throw new InvalidCommandParametersException();
        return decode(args[args.length-1], PARAMETERS_PAIR_SEPARATOR_TOKEN, PARAMETERS_SEPARATOR_TOKEN, InvalidCommandParametersException::new);
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
            String[] pathS = path.split(PATH_SEPARATOR_TOKEN);
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

    private static HashMap<String, String> decode(String args, String pairSeparator, String separator,
                                                  Supplier<InvalidCommandException> e) throws InvalidCommandException
    {
        HashMap<String, String> map = new HashMap<>();
        if(!args.contains(pairSeparator)) return map;
        for(String s: args.split(separator)){
            String[] sh = s.split(pairSeparator);
            if(sh.length > 2 || sh.length < 2 || sh[0].isEmpty()) throw e.get();
            map.put(sh[0], sh[1]);
        }
        return map;
    }
}
