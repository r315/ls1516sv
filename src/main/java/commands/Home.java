package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.util.HashMap;

/**
 * Created by Red on 16/07/2016.
 */
public class Home extends CommandBase {

    private static final String INFO= "Home - Display Home page";

    @Override
    public ResultInfo execute(HashMap<String, String> prmts) throws InvalidCommandException {
        return new ResultInfo(false);
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
