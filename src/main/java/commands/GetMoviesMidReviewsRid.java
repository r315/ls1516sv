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
import java.util.HashMap;

public class GetMoviesMidReviewsRid extends CommandBase {
    private static final String INFO = "GET /movies/{mid}/reviews/{rid} - returns the full information for the review rid of the movie identified by mid.";
    private final String TITLE = "'s Review by "; //Movie title before, Username after

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery())
        ) {
            int mid = Integer.parseInt(data.get("mid")), rid = Integer.parseInt(data.get("rid"));

            pstmt.setInt(1, mid);
            pstmt.setInt(2, rid);

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
