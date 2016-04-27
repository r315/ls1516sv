package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Red on 26/04/2016.
 */
public class Exit implements ICommand{
    private final String INFO = "ends the application.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        System.exit(0);
        return null;
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
