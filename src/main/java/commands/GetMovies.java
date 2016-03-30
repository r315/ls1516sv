package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by Luigi Sekuiya on 30/03/2016.
 */
public class GetMovies implements Command {

    @Override
    public void execute(List<Object> args, ConnectionFactory cf) throws SQLException {
        Connection conn = cf.getConn();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT title FROM Movie ORDER BY title");

        printRS(rs);

        stmt.close();
        conn.close();
    }

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
            System.out.println(rs.getString("title"));
        }
    }
}
