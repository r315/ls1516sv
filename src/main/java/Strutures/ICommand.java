package Strutures;

import java.util.HashMap;

public interface ICommand {

    String INFO = null;
    ResultInfo execute (HashMap<String,String> prmts) throws Exception;

}
