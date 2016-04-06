package console;

import java.util.HashMap;

import org.junit.Test;

import Strutures.CNode;
import Strutures.CommandMap;
import Strutures.DataNode;

public class getCommandMapTest {
	private static HashMap commandMap;
	
	@Before
	private void createCommandMap(){
		commandMap = CommandMap.createMap();
		
		
		
		CNode cnode = MapManager.getCnode(cmdinfo);	
		
	}
	
	
	@Test
	public void shouldBeAbleToGetExecutePost(){
		CommandInfo cmdinfo = new CommandInfo("POST","/movie","title=ls1516&release_year=2016");
		
	
		DataNode datanode = commandMap.get(cmdinfo.getMethod());
		
		
		
		
	}

}
