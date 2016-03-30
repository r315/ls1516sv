package commands;

import sqlserver.ConnectionFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Luigi Sekuiya on 30/03/2016.
 */
public interface Command {

    void execute (List<Object> args, ConnectionFactory cf) throws SQLException;
}
