package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringJoiner;


public class GetMoviesMidRatings implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
        int mID = Integer.parseInt(args.iterator().next());

        Connection conn = ConnectionFactory.getConn();

        PreparedStatement pstmt = conn.prepareStatement(getQuery());
        pstmt.setInt(1, mID);

        ResultSet rs = pstmt.executeQuery();

        printRS(rs);

        pstmt.close();
        ConnectionFactory.closeConn();

    }

    //Mising ratings from Review table
    private String getQuery() {
        return "SELECT Movie.title, Movie.release_year, ((Rating.one * 1 + Rating.two * 2 + Rating.three * 3 + Rating.four * 4 + Rating.five * 5) / (Rating.one + Rating.two + Rating.three + Rating.four + Rating.five)) AS ratavg, AVG(Review.rating) AS revavg" +
        "FROM Movie" +
        "LEFT JOIN Rating ON Movie.movie_id = Rating.movie_id" +
        "LEFT JOIN Review ON Review.movie_id = Movie.movie_id" +
        "WHERE Movie.movie_id = 3" +
        "GROUP BY Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five";
    }

    private void printRS(ResultSet rs) throws SQLException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rs.getDate("release_year"));

        int avg = 0;

        if (rs.getInt("ratavg") != 0 || rs.getInt("revavg") != 0)
            if (rs.getInt("ratavg") == 0) avg = rs.getInt("revavg");
            else if (rs.getInt("revavg") == 0) avg = rs.getInt("ratavg");
            else avg = (rs.getInt("revavg") + rs.getInt("ratavg")) / 2;

        System.out.println(rs.getString("title") + " (" +calendar.get(Calendar.YEAR) + "): " + avg);
    }
}
