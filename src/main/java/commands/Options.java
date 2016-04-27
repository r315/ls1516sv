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
    private final String INFO = "presents a list of available commands and their characteristics.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        ArrayList<String> rtitle=new ArrayList<String>();rtitle.add("Commands Info:");
        ArrayList<ArrayList<String>> rdata=new ArrayList<>();
        MainApp.createMap().getCommands().forEach(cmd->{
            ArrayList<String> aux=new ArrayList<>(1);
            aux.add(cmd.getInfo());//TODO change to cmd.INFO after luigi pushes
            rdata.add(aux);
        });
        return new ResultInfo(null,rtitle,rdata);
    }

    @Override
    public String getInfo() {
        return INFO;
    }

}
