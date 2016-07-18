package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class GetMoviesMid extends CommandBase {
    private static final String INFO = "GET /movies/{mid} - returns the detailed information for the movie identified by mid.";
    private final String TITLE = " Information"; //Adicionar titulo ao retornar

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery()))
        {
            int mid = Integer.parseInt(data.get("mid"));

            pstmt.setInt(1, mid);

            ResultSet rs = pstmt.executeQuery();

            return createRI(rs);

        } catch (NumberFormatException e) {
            throw new InvalidCommandVariableException();
        }
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "SELECT * FROM Movie WHERE movie_id = ?";
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Title");
        columns.add("Release Year");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);

        ArrayList<String> line = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rs.getDate("release_year"));

        line.add(rs.getString("movie_id"));
        line.add(rs.getString("title"));
        line.add(Integer.toString(calendar.get(Calendar.YEAR)));

        data.add(line);

        return new ResultInfo(rs.getString("title") + TITLE, columns, data);

    }

}
