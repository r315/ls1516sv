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

public class GetMoviesMidReviews implements ICommand {
    private final String INFO = "returns all the reviews for the movie identified by mid.";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID;

            try {
                mID = Utils.getInt(data.get("mID"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);

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
        return "SELECT Review.review_id, Review.name, Review.summary, Review.rating " +
                "FROM Review " +
                "INNER JOIN Movie ON Review.movie_id=Movie.movie_id " +
                "WHERE Movie.movie_id = ?";
    }

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
            System.out.println(rs.getInt("review_id") + " " + rs.getString("name") + " " + rs.getInt("rating") + " \n" +
                    "Summary: " + rs.getString("summary"));
        }
    }
}
