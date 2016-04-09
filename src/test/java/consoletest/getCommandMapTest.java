package consoletest;

import java.util.Arrays;
import java.util.Iterator;

import commands.GetMovies;
import org.junit.Test;

import commands.CommandInfo;
import logic.MapManager;
import Strutures.CNode;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class getCommandMapTest {
	
	
	
	@Test
	public void shouldBeAbleToGetCnode(){
		CommandInfo cmdInfo = new CommandInfo("GET","/movies","");
		CNode cnodeExpected = new CNode(Arrays.asList(new String[]{}),new GetMovies());
		CNode cnodeActual = null;
		try {
			cnodeActual = MapManager.getCNode(cmdInfo);
		} catch (Exception e) {
			fail();
		}		
		assertTrue(cnodeCmp(cnodeExpected,cnodeActual));		
	}
	
	
	private boolean cnodeCmp(CNode c1, CNode c2){
		Iterator<String> it1 = c1.iterator();
		Iterator<String> it2 = c2.iterator();
		
		if(c1.getCollectionSize() != c2.getCollectionSize())
			return false;		
		
		while(it1.hasNext())
			if(! it1.next().equals(it2.next()))
				return false;

		return true;
	}

}
