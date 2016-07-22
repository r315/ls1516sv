package decoders;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import exceptions.InvalidCommandException;
import org.junit.Before;
import org.junit.Test;

import exceptions.InvalidCommandPathException;
import utils.Decoder;

public class DecodePathTest {
    Collection<String> testResources;

    @Before
    public void init(){
        testResources = new ArrayList<>();
        testResources.add("movies");
        testResources.add("1");
        testResources.add("reviews");
        testResources.add("4");
    }

    @Test
    public void PathExecute_MethodPath() throws InvalidCommandException {
        Collection<String> path = Decoder.decodePath("GET /movies/1/reviews/4".split(" "));
        assertEquals(testResources,path);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_Method() throws InvalidCommandException {
        Collection<String> path = Decoder.decodePathFromCommand("EXIT");
    }

    @Test
    public void PathExecute_Array() throws InvalidCommandException {
        String [] aux = new String[]{"GET","/movies/1/reviews/4"};
        Collection<String> path = Decoder.decodePath(aux);
        assertEquals(testResources,path);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_isEmpty() throws InvalidCommandException {
        String [] aux = new String[]{"GET",""};
        Collection<String> path = Decoder.decodePath(aux);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_isNull() throws InvalidCommandException {
        String [] aux = new String[]{"GET",null};
        Collection<String> path = Decoder.decodePath(aux);
    }
}
