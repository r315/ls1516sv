package structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import Strutures.CommandInfo;

/**
 * Created by Red on 30/04/2016.
 */

public class CommandInfoTest {

    @Test
    public void shouldGetResourcesSize() throws Exception{
        CommandInfo cmdInfo = new CommandInfo(new String []{"POST","/movies/123"});
        assertEquals(2,cmdInfo.getResourcesSize());
    }

    @Test
    public void shouldGetMehod() throws Exception{
        CommandInfo cmdInfo = new CommandInfo(new String []{"GET","/movies/123"});
        assertEquals("GET",cmdInfo.getMethod());
    }

    @Test
    public void shouldGetResources() throws Exception{
        Collection<String> expected = new ArrayList<String>();
        expected.addAll(Arrays.asList(new String[]{"movies","123","reviews"}));

            CommandInfo cmdInfo = new CommandInfo(new String[]{"GET","/movies/123/reviews"});
            Iterator<String> it1 = expected.iterator();
            Iterator<String> it2 = cmdInfo.getResources().iterator();

            while(it1.hasNext() && it2.hasNext()){
                if(!it1.next().equals(it2.next()))
                    fail();
            }

    }

    @Test
    public void shouldGetParameters() throws Exception{
        CommandInfo cmdInfo = new CommandInfo(new String[]{"GET","/movies/123/reviews","title=filme&release_year=2016"});
        Map<String,String> pmap= cmdInfo.getData();
        assertEquals("filme",pmap.get("title"));
        assertEquals("2016",pmap.get("release_year"));
    }
}
