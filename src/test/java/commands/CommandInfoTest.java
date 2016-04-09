package commands;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class CommandInfoTest {
	
	
	
	@Test
	public void shouldGetResourcesSize(){		
			CommandInfo cmdInfo = new CommandInfo("POST","/movies/123",null);
			assertEquals(2,cmdInfo.getResourcesSize());		
	}
	
	@Test
	public void shouldGetMehod(){		
			CommandInfo cmdInfo = new CommandInfo("GET","/movies/123",null);
			assertEquals("GET",cmdInfo.getMethod());		
	}

	@Test
	public void shouldGetResources(){
		Collection<String> expected = new ArrayList<String>();
		expected.addAll(Arrays.asList(new String[]{"movies","123","reviews"}));
		try{
			CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/reviews",null);
			Iterator<String> it1 = expected.iterator();
			Iterator<String> it2 = cmdInfo.getResources().iterator();
			
			while(it1.hasNext() && it2.hasNext()){
				if(!it1.next().equals(it2.next()))
					fail();
			}
			
		}catch(Exception e){
			fail();
		}
	}	
	
	@Test
	public void shouldGetParameters(){		
			CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/reviews","title=filme&release_year=2016");
			Map<String,String> pmap= cmdInfo.getParameters();
			assertEquals("filme",pmap.get("title"));
			assertEquals("2016",pmap.get("release_year"));			
	}
	
	
	
}
