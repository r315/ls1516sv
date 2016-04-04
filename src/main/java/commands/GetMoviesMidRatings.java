package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;


public class GetMoviesMidRatings implements ICommand {

    @Override
    public void execute(Iterable<Object> args, HashMap<String, String> prmts, ConnectionFactory cf) throws SQLException {
        int mID = (int) args.iterator().next();

        Connection conn = cf.getConn();

        PreparedStatement pstmt = conn.prepareStatement("" +
                "SELECT Movie.title, Movie.release_year, ((Ratings.one * 1 + Ratings.two * 2 + Ratings.three * 3 + Ratings.four * 4 + Ratings.five * 5) / " +
                    "(Ratings.one + Ratings.two + Ratings.three + Ratings.four + Ratings.five)) AS avg " +
                "FROM Ratings " +
                "INNER JOIN Movie ON Movie.movie_id = Ratings.rating_id " +
                "WHERE rating_id = ?");
        pstmt.setInt(1, mID);

        ResultSet rs = pstmt.executeQuery();

        printRS(rs);

        pstmt.close();
        cf.closeConn();

    }

    private void printRS(ResultSet rs) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rs.getDate("release_year"));

        System.out.println(rs.getString("title") + " (" +calendar.get(Calendar.YEAR) + "): " + rs.getFloat("avg"));
    }
}
