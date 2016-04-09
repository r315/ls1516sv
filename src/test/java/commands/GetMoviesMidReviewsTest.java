package commands;

import Strutures.CNode;
import logic.MapManager;
import org.junit.Before;
import org.junit.Test;

public class GetMoviesMidReviewsTest {
    CommandInfo cmdInfo;

    @Before
    public void init(){
        cmdInfo = new CommandInfo("GET","/movies/1/reviews",null);
    }

    @Test
    public void GetMoviesMidConsoleOutputTest()throws Exception{
        CNode cnode= MapManager.getCNode(cmdInfo);
        cnode.getCommand().execute(cmdInfo.getResources(),cmdInfo.getParameters());
    }
}
