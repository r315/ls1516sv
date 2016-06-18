package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GetCollectionsCid implements ICommand {
    private static final String INFO = "GET /collections/{cid} - returns the details for the cid collection, namely all the movies in that collection.";
    private final String TITLE = "'s Collection"; //Adicionar titulo ao retornar

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        Boolean topB = false;
        int skip = 0, top = 1;
        String orderBy = "title";

        if (data != null) {
            topB = (data.get("top") != null);
            HashMap<String, Integer> skiptop = Utils.getSkipTop(data.get("skip"), data.get("top"));

            skip = skiptop.get("skip");
            top = skiptop.get("top");
        }

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top, orderBy))
        ){
            int cid;

            try {
                cid = Utils.getInt(data.get("cid"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }


            pstmt.setInt(1, cid);
            pstmt.setInt(2, skip);

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

    private String getQuery(Boolean topB, int top, String orderBy) {
        String query = "SELECT Collection.*, Movie.* FROM Movie " +
                "INNER JOIN Has ON Has.movie_id = Movie.movie_id " +
                "RIGHT JOIN Collection ON Collection.collection_id = Has.collection_id " +
                "WHERE Collection.collection_id = ? " +
                "ORDER BY Movie.title " +
                "OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie ID");
        columns.add("Movie Name");
        columns.add("Release Year");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);
        String name = rs.getString("name");

        if((rs.getString("movie_id") != null)) {
            do {
                ArrayList<String> line = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getDate("release_year"));
                line.add(rs.getString("movie_id"));
                line.add(rs.getString("title"));
                line.add(Integer.toString(calendar.get(Calendar.YEAR)));
                data.add(line);
            } while (rs.next());
        }

        return new ResultInfo(name + TITLE, columns, data);
    }
}
