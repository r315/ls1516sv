package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;


public class GetMovies implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
        try(Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(getQuery());

            printRS(rs);

            stmt.close();
        }
    }

    private String getQuery() {
        return "SELECT title, release_year FROM Movie ORDER BY title";
    }

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            System.out.println(rs.getString("title") + " (" +calendar.get(Calendar.YEAR) + ")");
        }
    }

}
