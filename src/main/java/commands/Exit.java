package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;

import java.util.HashMap;

/**
 * Created by Red on 26/04/2016.
 */
public class Exit extends CommandBase {
    private final String INFO = "EXIT / - ends the application.";

    @Override
    public ResultInfo execute(HashMap<String, String> data)  {
        Manager.ServerStop();
        //Manager.ServerJoin();
        System.exit(0);
        return null;
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
