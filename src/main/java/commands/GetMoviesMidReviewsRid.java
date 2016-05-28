package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sqlserver.ConnectionFactory;
import utils.Utils;
import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandVariableException;

public class GetMoviesMidReviewsRid implements ICommand {
    private static final String INFO = "GET /movies/{mid}/reviews/{rid} - returns the full information for the review rid of the movie identified by mid.";
    private final String TITLE = "'s Review by "; //Movie title before, Username after

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID, rID;

            try {
                mID = Utils.getInt(data.get("mid"));
                rID = Utils.getInt(data.get("rid"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);
            pstmt.setInt(2, rID);

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
        return "SELECT Movie.movie_id, Movie.title, Review.review_id, Review.name, Review.summary, Review.review, Review.rating " +
                "FROM Review " +
                "INNER JOIN Movie ON Review.movie_id=Movie.movie_id " +
                "WHERE Movie.movie_id = ? AND Review.review_id = ?";
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie's ID");
        columns.add("Movie's Title");
        columns.add("Review's ID");
        columns.add("Username");
        columns.add("Rating");
        columns.add("Summary");
        columns.add("Review");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);

        ArrayList<String> line = new ArrayList<>();

        line.add(rs.getString("movie_id"));
        line.add(rs.getString("title"));
        line.add(rs.getString("review_id"));
        line.add(rs.getString("name"));
        line.add(rs.getString("rating"));
        line.add(rs.getString("summary"));
        line.add(rs.getString("review"));

        data.add(line);

        return new ResultInfo(rs.getString("title") + TITLE + rs.getString("name"),columns,data);
    }

}
