package commands;

import exceptions.CommandWrongVariableException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class GetMoviesMidReviewsRid implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID, rID;

            Iterator<String> it = args.iterator();
            it.next();

            try {
                mID = Integer.parseInt(it.next());
                it.next();
                rID = Integer.parseInt(it.next());
            } catch (NumberFormatException e) {
                throw new CommandWrongVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);
            pstmt.setInt(2, rID);

            ResultSet rs = pstmt.executeQuery();

            printRS(rs);

            pstmt.close();
        }

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
