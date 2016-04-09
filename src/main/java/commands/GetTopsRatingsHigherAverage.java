package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

public class GetTopsRatingsHigherAverage implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
        Connection conn = ConnectionFactory.getConn();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(getQuery());

        printRS(rs);

        stmt.close();
        ConnectionFactory.closeConn();
    }

    private String getQuery() {
        return "SELECT TOP 1 title, release_year, COALESCE ((ratavg + revavg) / 2, ratavg, revavg) AS average " +
                "FROM " +
                "(" +
                "SELECT Movie.title, Movie.release_year, ((Rating.one * 1 + Rating.two * 2 + Rating.three * 3 + Rating.four * 4 + Rating.five * 5) / (Rating.one + Rating.two + Rating.three + Rating.four + Rating.five)) AS ratavg, AVG(Review.rating) AS revavg " +
                "FROM Movie " +
                "LEFT JOIN Rating ON Movie.movie_id = Rating.movie_id " +
                "LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
                "GROUP BY Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five " +
                ") AS average " +
                "ORDER BY average DESC";
    }

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            System.out.println(rs.getString("title") + " (" + calendar.get(Calendar.YEAR) + ")");
        }

    }

}