package commands;

import Strutures.CNode;
import Strutures.CommandInfo;
import logic.MapManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Red on 09/04/2016.
 */
public class GetMoviesTest {
    CommandInfo cmdInfo;


    @Before
    public void init(){
        cmdInfo = new CommandInfo("GET","/movies",null);
    }

    @Test
    public void GetMoviesConsoleOutputTest()throws Exception{
        CNode cnode= MapManager.getCNode(cmdInfo);
        cnode.getCommand().execute(cmdInfo.getResources(),cmdInfo.getParameters());
    }
}
