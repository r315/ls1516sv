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
        int mID, rID;

        Iterator<String> it = args.iterator();
        it.next();

        try {
            mID = Integer.parseInt(args.iterator().next());
            it.next();
            rID = Integer.parseInt(args.iterator().next());
        } catch (NumberFormatException e) {
            throw new CommandWrongVariableException();
        }

        Connection conn = ConnectionFactory.getConn();

        PreparedStatement pstmt = conn.prepareStatement(getQuery());
        pstmt.setInt(1,mID);
        pstmt.setInt(2,rID);

        ResultSet rs = pstmt.executeQuery();

        printRS(rs);

        pstmt.close();
        ConnectionFactory.closeConn();

    }

    private String getQuery() {
        return "SELECT Review.review_id, Review.user, Review.summary, Review.review, Review.rating" +
                "FROM Review" +
                "INNER JOIN Movie ON Review.movie_id=Movie.movie_id" +
                "WHERE Movie.movie_id = ? AND Review.review_id = ?";
    }

    private void printRS(ResultSet rs) throws SQLException {
        System.out.println(rs.getInt("review_id") + " " + rs.getString("user") + " " + rs.getInt("rating") + "\n" +
                "Summary: " + rs.getString("summary") + "\n" +
                "Review: " + rs.getString("review"));
    }

}
