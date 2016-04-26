package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.*;


public class GetMovies implements ICommand {

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws SQLException {
        try(Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(getQuery());

            ResultInfo result = printRS(rs);

            stmt.close();

            return result;
        }

    }

    private String getQuery() {
        return "SELECT title, release_year FROM Movie ORDER BY title";
    }

    private ResultInfo printRS(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Titulo");
        columns.add("Ano de Lançamento");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            line.add(rs.getString("title"));
            line.add(Integer.toString(calendar.get(Calendar.YEAR)));

            data.add(line);
        }

        return new ResultInfo(columns, data);
    }

}
