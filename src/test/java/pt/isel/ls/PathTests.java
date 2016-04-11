package pt.isel.ls;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by pedro on 06/04/16.
 */
public class PathTests {

    @Test
    public void path_contains_all_segments() throws InvalidPathException {
        Path p = new Path("/a/bb/ccc");
        assertEquals(3, p.size());
        assertEquals("a", p.get(0));
        assertEquals("bb", p.get(1));
        assertEquals("ccc", p.get(2));
    }
}
