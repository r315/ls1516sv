package commands;

import Strutures.Command.CommandBase;

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

/**
 * Created by Luigi Sekuiya on 25/05/2016.
 */
public class GetTopsNReviewsLowerCount extends CommandBase {
    private static final String INFO = "GET /tops/{n}/reviews/lower/count - returns a list with the n movies with lower review count.";
    private final String TITLE = " Movies with lower review count"; //Add n before

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        Boolean topB = false;
        int skip = 0, top = 1, n;

        try {
            n = Utils.getInt(data.get("n"));
        } catch (NumberFormatException e) {
            throw new InvalidCommandVariableException();
        }

        if (n < 0) throw new InvalidCommandVariableException();

        if (data != null) {
            topB = (data.get("top") != null);
            HashMap<String, Integer> skiptop = Utils.getSkipTop(data.get("skip"), data.get("top"));

            skip = skiptop.get("skip");
            top = skiptop.get("top");
        }

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top));
        ) {

            pstmt.setInt(1, n);
            pstmt.setInt(2, skip);

            ResultSet rs = pstmt.executeQuery();

            ResultInfo result = createRI(rs, n);

            return result;
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery(Boolean topB, int top) {
        String query = "SELECT movie_id, title, release_year, revcount FROM ( \n" +
                "SELECT Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount, Movie.movie_id\n" +
                "FROM Movie\n" +
                "LEFT JOIN Review ON Review.movie_id = Movie.movie_id\n" +
                "GROUP BY Movie.title, Movie.release_year, Movie.movie_id\n" +
                "ORDER BY revcount\n" +
                "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY\n" +
                ") AS tudo ORDER BY revcount DESC, movie_id OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs, int n) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Title");
        columns.add("Release Year");
        columns.add("Review Count");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            line.add(rs.getString("movie_id"));
            line.add(rs.getString("title"));
            line.add(Integer.toString(calendar.get(Calendar.YEAR)));
            line.add(rs.getString("revcount"));

            data.add(line);
        }

        return new ResultInfo(n + TITLE, columns, data);
    }

}