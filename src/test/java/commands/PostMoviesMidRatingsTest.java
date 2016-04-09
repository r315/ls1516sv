package commands;

import Strutures.CNode;
import logic.MapManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Red on 09/04/2016.
 */
public class PostMoviesMidRatingsTest {

    CommandInfo cmdInfo;

    @Before
    public void init(){
        cmdInfo = new CommandInfo("POST","/movies/2/ratings","rating=5");
    }

    @Test
    public void GetMoviesMidConsoleOutputTest()throws Exception{
        CNode cnode= MapManager.getCNode(cmdInfo);
        cnode.getCommand().execute(cmdInfo.getResources(),cmdInfo.getParameters());
    }

}
