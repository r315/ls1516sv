package pt.isel.ls;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pedro on 06/04/16.
 */
public class Paths {
    public static List<String> splitIntoSegments(String path) throws InvalidPathException{
        if(path.charAt(0) != '/'){
            throw new InvalidPathException();
        }
        return Arrays.asList(path.substring(1).split("/"));
    }
}
