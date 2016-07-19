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
    Collection<String> resources;

    @Before
    public void init(){
        resources = new ArrayList<>();
        resources.add("movies");
        resources.add("1");
        resources.add("reviews");
        resources.add("4");
    }

    @Test
    public void PathExecute_MethodPath() throws Exception {
        Collection<String> path = Decoder.decodePath("GET /movies/1/reviews/4".split(" "));
        assertEquals(resources,path);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_Method() throws InvalidCommandException {
        Collection<String> path = Decoder.decodePathFromCommand("EXIT");
    }

    @Test
    public void PathExecute_Array() throws Exception {
        String [] aux = new String[]{"GET","/movies/1/reviews/4"};
        Collection<String> path = Decoder.decodePath(aux);
        assertEquals(resources,path);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"GET",""};
        Collection<String> path = Decoder.decodePath(aux);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_isNull() throws Exception {
        String [] aux = new String[]{"GET",null};
        Collection<String> path = Decoder.decodePath(aux);
    }
}
