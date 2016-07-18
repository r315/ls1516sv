package utils;

import exceptions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by hmr on 18/07/2016.
 */
public class Decoder {


    public static HashMap<String, String> decodeHeaders(String[] args) throws InvalidCommandException{
        return decode(args,":","\\|", InvalidCommandHeadersException::new);
    }

    public static HashMap<String, String> decodeParameters(String args) throws InvalidCommandException{
        return decodeParameters(args.split(" "));

    }
    public static HashMap<String, String> decodeParameters(String[] args) throws InvalidCommandException{
        return decode(args,"=","&", InvalidCommandParametersException::new);
    }

    public static Collection<String> decodePath(String [] args) throws InvalidCommandException {
        if (args.length >= 2 && args[1] != null && args[1].length() != 0) return decodePath(args[1]);
        else throw new InvalidCommandPathException();
    }

    public static Collection<String> decodePath(String path)throws InvalidCommandException {
        if(!path.startsWith("/"))
            throw new InvalidCommandPathException();
        String [] pathS = path.split("/");
        Collection<String> resources = new ArrayList<>();
        if( pathS.length > 0) resources.addAll(Arrays.asList(pathS).subList(1,pathS.length));
        return resources;
    }

    public static String decodeMethod(String line) throws InvalidCommandException {
        if (line == null) throw new InvalidCommandMethodException();
        return decodeMethod(line.split(" "));
    }

    public static String decodeMethod(String [] args) throws InvalidCommandException {
        if (args != null && args.length > 0 && args[0] != null && args[0].length() != 0) return args[0];
        else throw new InvalidCommandMethodException();

    }

    private static HashMap<String, String> decode(String[] args, String kvSeparator, String paramSeparator,
                                                 Supplier<InvalidCommandException> ex) throws InvalidCommandException
    {
        HashMap<String, String> map = new HashMap<String, String>();
        try {

            Arrays.stream(args)
                    .filter(s -> s.contains(kvSeparator))
                    .map(sm -> sm.split(paramSeparator))    //return Stream<String[]>
                    .flatMap(Arrays::stream)    //return Stream<String>
                    .forEach(h -> {
                                String[] sh = h.split(kvSeparator);
                                map.put(sh[0], sh[1]);
                            }
                    );
        }catch (Exception e){
            throw ex.get();
        }
        return map;
    }
}
