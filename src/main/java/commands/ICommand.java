package commands;



import java.util.Collection;
import java.util.HashMap;

public interface ICommand {

    void execute (Collection<String> args, HashMap<String,String> prmts) throws Exception;
}
