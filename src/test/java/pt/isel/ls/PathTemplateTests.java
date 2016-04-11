package pt.isel.ls;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by pedro on 06/04/16.
 */
public class PathTemplateTests {

    @Test
    public void PathTemplate_matches_and_extracts_with_one_var() throws InvalidPathException {
        PathTemplate pt = new PathTemplate("/movies/{mid}");
        Optional<Map<String,String>> vars = pt.match(new Path("/movies/123"));
        assertNotNull(vars);
        assertEquals(1, vars.get().size());
        assertEquals("123", vars.get().get("mid"));
    }

    @Test
    public void PathTemplate_matches_and_extracts_with_all_var() throws InvalidPathException {
        PathTemplate pt = new PathTemplate("/{controller}/{mid}");
        Optional<Map<String,String>> vars = pt.match(new Path("/movies/123"));
        assertTrue(vars.isPresent());
        assertEquals(2, vars.get().size());
        assertEquals("123", vars.get().get("mid"));
        assertEquals("movies", vars.get().get("controller"));
    }

    @Test
    public void PathTemplate_fail_matches_on_mismatch() throws InvalidPathException {
        PathTemplate pt = new PathTemplate("/movies/{mid}");
        Optional<Map<String,String>> vars = pt.match(new Path("/Movies/123"));
        assertFalse(vars.isPresent());
    }

    @Test
    public void PathTemplate_fail_matches_on_empty() throws InvalidPathException {
        PathTemplate pt = new PathTemplate("//");
        Optional<Map<String,String>> vars = pt.match(new Path("/Movies/123"));
        assertFalse(vars.isPresent());
    }
}
