package commands;

import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class GetMoviesMidReviews implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID;
            Iterator<String> it = args.iterator();
            it.next();
            try {
                mID = Integer.parseInt(it.next());
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);

            ResultSet rs = pstmt.executeQuery();

            printRS(rs);

            pstmt.close();
        }

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
