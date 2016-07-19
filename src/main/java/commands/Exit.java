package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import console.MainApp;
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
        MainApp.Exit();
        return new ResultInfo(false);
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
