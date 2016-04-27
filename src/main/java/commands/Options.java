package commands;

import Strutures.CommandMap;
import Strutures.ICommand;
import Strutures.ResultInfo;

import java.util.HashMap;

public class Options implements ICommand{

    //TODO
    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        CommandMap.createMap().getCommands().forEach(cmd->System.out.println(cmd.toString()));
        return null;
    }

    @Override
    public String getInfo(){
        return "EXIT / - ends the application.";
    }
}
