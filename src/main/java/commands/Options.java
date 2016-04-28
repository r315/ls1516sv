package commands;

import Strutures.CommandMap;
import Strutures.ICommand;
import Strutures.ResultInfo;
import console.MainApp;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Options implements ICommand{
    public final String INFO = "OPTION / - presents a list of available commands and their characteristics.";
    private final String title="List of available commands and their characteristics";
    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        ArrayList<String> rtitle=new ArrayList<String>();rtitle.add("Commands Info:");
        ArrayList<ArrayList<String>> rdata=new ArrayList<>();
        for (ICommand cmd:MainApp.createMap().getCommands()){
            ArrayList<String> aux=new ArrayList<>(1);
            aux.add(cmd.getInfo());// TODO: Further test needed
            rdata.add(aux);
        }
        /*
        MainApp.createMap().getCommands().forEach(cmd->{
            ArrayList<String> aux=new ArrayList<String>(1);
            aux.add(cmd.INFO);// TODO: Further test needed
            rdata.add(aux);
        });*/
        return new ResultInfo(title,null,rdata);
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
