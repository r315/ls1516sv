package commands;

import java.util.HashMap;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;

/**
 * Created by Red on 26/04/2016.
 */
public class Exit implements ICommand{
    private final String INFO = "EXIT / - ends the application.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
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
