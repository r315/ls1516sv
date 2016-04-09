package commands;

import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class GetMoviesMidReviews implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
        int mID = Integer.parseInt(args.iterator().next());

        Connection conn = ConnectionFactory.getConn();

        PreparedStatement pstmt = conn.prepareStatement(getQuery());
        pstmt.setInt(1,mID);

        ResultSet rs = pstmt.executeQuery();

        printRS(rs);

        pstmt.close();
        ConnectionFactory.closeConn();

    }

    private String getQuery() {
        return "SELECT Review.review_id, Review.user, Review.summary, Review.rating" +
                "FROM Reviews" +
                "INNER JOIN Movie ON Review.movie_id=Movie.movie_id" +
                "WHERE Movie.movie_id = ?";
    }

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
            System.out.println(rs.getInt("review_id") + " " + rs.getString("user") + " " + rs.getInt("rating") + " \n" +
                    "Summary: " + rs.getString("summary"));
        }
    }
}
