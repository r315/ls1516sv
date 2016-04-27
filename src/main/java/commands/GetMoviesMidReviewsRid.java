package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import utils.Utils;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GetMoviesMidReviewsRid implements ICommand {
    private final String INFO = "returns the full information for the review rid of the movie identified by mid.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID, rID;

            try {
                mID = Utils.getInt(data.get("mID"));
                rID = Utils.getInt(data.get("rID"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);
            pstmt.setInt(2, rID);

            ResultSet rs = pstmt.executeQuery();

            printRS(rs);

            pstmt.close();
        }

        //Builderino stuff
        ResultInfo stuff = new ResultInfo();
        return stuff;
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "SELECT Review.review_id, Review.name, Review.summary, Review.review, Review.rating " +
                "FROM Review " +
                "INNER JOIN Movie ON Review.movie_id=Movie.movie_id " +
                "WHERE Movie.movie_id = ? AND Review.review_id = ?";
    }

    private void printRS(ResultSet rs) throws SQLException {
        rs.next();

        System.out.println(rs.getInt("review_id") + " " + rs.getString("name") + " " + rs.getInt("rating") + "\n" +
                "Summary: " + rs.getString("summary") + "\n" +
                "Review: " + rs.getString("review"));
    }

}
