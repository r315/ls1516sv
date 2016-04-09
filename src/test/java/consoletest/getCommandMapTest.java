package consoletest;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import Commands.ICommand;
import Commands.CommandInfo;
import Logic.MapManager;
import Strutures.CNode;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class getCommandMapTest {
	
	
	
	@Test
	public void shouldBeAbleToGetCnode(){
		CommandInfo cmdInfo = new CommandInfo("POST","/movie","title=ls1516&release_year=2016");
		CNode cnodeExpected = new CNode(Arrays.asList(new String[]{}),(ICommand)null);
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
		
		while(it1.hasNext() && it2.hasNext()){
			if(! it1.next().equals(it2.next()))
				return false;
		}
		return true;
	}

}
