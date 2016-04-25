package Strutures;



import Strutures.ResultInfo;
import java.util.HashMap;

public interface ICommand {

    ResultInfo execute (HashMap<String,String> prmts) throws Exception;
}
