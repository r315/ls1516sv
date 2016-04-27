package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.*;
import java.util.*;


public class GetMovies implements ICommand {
    private final String INFO = "GET /movies - returns a list with all movies.";
    private final String TITLE = "Lista de Filmes";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        Boolean topB = false;
        int skip = 0, top = 1;

        if (data != null) {
            topB = (data.get("top") != null);
            HashMap<String, Integer> skiptop = Utils.getSkipTop(data.get("skip"), data.get("top"));

            skip = skiptop.get("skip");
            top = skiptop.get("top");
        }

        try(Connection conn = ConnectionFactory.getConn()) {
            PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top));
            pstmt.setInt(1, skip);

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

    private String getQuery(Boolean topB, int top) {
        String query = "SELECT title, release_year FROM Movie ORDER BY title OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
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

        return new ResultInfo(TITLE, columns, data);
    }

}
