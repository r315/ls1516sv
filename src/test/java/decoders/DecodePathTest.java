package decoders;

import exceptions.InvalidCommandPathException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

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
        Collection<String> path = DecodePath.decode("GET /movies/1/reviews/4");
        assertEquals(resources,path);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_Method() throws Exception {
        Collection<String> path = DecodePath.decode("EXIT");
    }

    @Test
    public void PathExecute_Array() throws Exception {
        String [] aux = new String[]{"GET","/movies/1/reviews/4"};
        Collection<String> path = DecodePath.decode(aux);
        assertEquals(resources,path);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"GET",""};
        Collection<String> path = DecodePath.decode(aux);
    }

    @Test(expected=InvalidCommandPathException.class)
    public void PathExecute_isNull() throws Exception {
        String [] aux = new String[]{null,null};
        Collection<String> path = DecodePath.decode(aux);
    }
}
