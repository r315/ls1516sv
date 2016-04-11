package pt.isel.ls;

import java.util.LinkedList;

/**
 * Created by pedro on 06/04/16.
 */
public class Path extends LinkedList<String> {

    public Path(String pathString) throws InvalidPathException{
        super(Paths.splitIntoSegments(pathString));
    }
}
