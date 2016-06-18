package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;

import java.util.ArrayList;
import java.util.HashMap;

public class Options implements ICommand{
    public final String INFO = "OPTION / - presents a list of available commands and their characteristics.";
    private final String title="List of available commands and their characteristics";
    @Override
    public ResultInfo execute(HashMap<String, String> data) {
        ArrayList<String> rtitle=new ArrayList<String>();rtitle.add("Commands Info:");
        ArrayList<ArrayList<String>> rdata=new ArrayList<>();
        for (ICommand cmd: Manager.commandMap.getCommands()){
            ArrayList<String> aux=new ArrayList<>(1);
            aux.add(cmd.getInfo());
            rdata.add(aux);
        }
        return new ResultInfo(title,null,rdata);
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
