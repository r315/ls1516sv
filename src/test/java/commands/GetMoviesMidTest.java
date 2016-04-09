package commands;

import Strutures.CNode;
import logic.MapManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Red on 09/04/2016.
 */
public class GetMoviesMidTest {
    CommandInfo cmdInfo;

    @Before
    public void init(){
        cmdInfo = new CommandInfo("GET","/movies/1",null);
    }

    @Test
    public void GetMoviesMidConsoleOutputTest()throws Exception{
        CNode cnode= MapManager.getCNode(cmdInfo);
        cnode.getCommand().execute(cmdInfo.getResources(),cmdInfo.getParameters());
    }
}
