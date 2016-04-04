package commands;

import sqlserver.ConnectionFactory;

import java.sql.SQLException;
import java.util.HashMap;


public interface ICommand {

    void execute (Iterable<Object> args, HashMap<String,String> prmts, ConnectionFactory cf) throws SQLException;
}
