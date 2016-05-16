package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sqlserver.ConnectionFactory;
import utils.Utils;
import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;


public class GetMoviesMid implements ICommand {
    private static final String INFO = "GET /movies/{mid} - returns the detailed information for the movie identified by mid.";
    private final String TITLE = " Information"; //Adicionar titulo ao retornar

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mid;

            try {
                mid = Utils.getInt(data.get("mid"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mid);

            ResultSet rs = pstmt.executeQuery();

            ResultInfo result = createRI(rs);

            pstmt.close();

            return result;
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
        columns.add("Titulo");
        columns.add("Ano de Lançamento");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);

        ArrayList<String> line = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rs.getDate("release_year"));

        line.add(rs.getString("title"));
        line.add(Integer.toString(calendar.get(Calendar.YEAR)));

        data.add(line);

        return new ResultInfo(rs.getString("title") + TITLE, columns, data);

    }

}
