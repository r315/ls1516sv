package commands;

import java.util.HashMap;

import Strutures.ICommand;
import Strutures.Manager;
import Strutures.ResultInfo;

/**
 * Created by Red on 26/04/2016.
 */
public class Exit implements ICommand{
    private final String INFO = "EXIT / - ends the application.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        Manager.ServerJoin();
        System.exit(0);
        return null;
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
