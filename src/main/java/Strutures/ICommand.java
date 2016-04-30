package Strutures;

import java.util.HashMap;

public interface ICommand {
    ResultInfo execute (HashMap<String,String> prmts) throws Exception;
    String getInfo();
}
