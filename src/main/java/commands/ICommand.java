package commands;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;


public interface ICommand {

    void execute (Collection<String> args, HashMap<String,String> prmts) throws SQLException;
}
