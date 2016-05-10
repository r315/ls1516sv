package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sqlserver.ConnectionFactory;
import utils.Utils;
import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;

public class GetMoviesMidReviews implements ICommand {
    private static final String INFO = "GET /movies/{mid}/reviews - returns all the reviews for the movie identified by mid.";
    private final String TITLE = "'s Reviews"; //Adicionar titulo ao retornar

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
            int mID;

            try {
                mID = Utils.getInt(data.get("mid"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top));
            pstmt.setInt(1, mID);
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

    private String getQuery(Boolean topB, int top) {
        String query = "SELECT Movie.title, Review.review_id, Review.name, Review.summary, Review.rating " +
                        "FROM Review " +
                        "INNER JOIN Movie ON Review.movie_id=Movie.movie_id " +
                        "WHERE Movie.movie_id = ? " +
                        "ORDER BY Movie.movie_id " +
                        "OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Review's ID");
        columns.add("username");
        columns.add("Rating");
        columns.add("Summary");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);
        String title = rs.getString("title");

        do {
            ArrayList<String> line = new ArrayList<>();

            line.add(rs.getString("review_id"));
            line.add(rs.getString("name"));
            line.add(rs.getString("rating"));
            line.add(rs.getString("summary"));

            data.add(line);
        } while(rs.next());
        return new ResultInfo(title + TITLE, columns, data);
    }
}
