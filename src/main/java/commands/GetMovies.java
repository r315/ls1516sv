package Commands;

import SQLServer.ConnectionFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;


public class GetMovies implements ICommand {

    @Override
    public void execute(Iterable<Object> args, HashMap<String, String> prmts, ConnectionFactory cf) throws SQLException {
        Connection conn = cf.getConn();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT title, release_year FROM Movie ORDER BY title");

        printRS(rs);

        stmt.close();
        cf.closeConn();
    }

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            System.out.println(rs.getString("title") + " (" +calendar.get(Calendar.YEAR) + ")");
        }
    }

}
