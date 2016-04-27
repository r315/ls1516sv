package commands;

import Strutures.CommandMap;
import Strutures.ICommand;
import Strutures.ResultInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;

public class Options implements ICommand{
    private final String INFO = "presents a list of available commands and their characteristics.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        CommandMap.createMap().getCommands().forEach(cmd->System.out.println(cmd.getInfo()));
        return null;
    }

    @Override
    public String getInfo() {
        return INFO;
    }

}
