package commands;

import Strutures.CNode;
import logic.MapManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Luigi Sekuiya on 09/04/2016.
 */
public class GetTopsNReviewsHigherCountTest {
    CommandInfo cmdInfo;

    @Before
    public void init(){
        cmdInfo = new CommandInfo("GET","/tops/2/reviews/higher/count",null);
    }

    @Test
    public void GetMoviesMidConsoleOutputTest()throws Exception{
        CNode cnode= MapManager.getCNode(cmdInfo);
        cnode.getCommand().execute(cmdInfo.getResources(),cmdInfo.getParameters());
    }
}