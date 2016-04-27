package structures;

import Strutures.CommandInfo;
import Strutures.CommandMap;
import console.MainApp;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Red on 27/04/2016.
 */
public class OptionTest {

    CommandMap map;

    @Before
    public void before() throws Exception{
        map= MainApp.createMap();
    }

    @Test
    public void OptionsCommandTest() throws Exception{
        CommandInfo command=new CommandInfo(new String[]{"OPTION","/"});
        map.get(command).execute(command.getData());
    }
}
