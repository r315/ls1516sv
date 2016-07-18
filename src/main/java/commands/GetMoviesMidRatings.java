package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class GetMoviesMidRatings extends CommandBase {
    private static final String INFO = "GET /movies/{mid}/ratings - returns the rating information for the movie identified by mid.";
    private final String TITLE = "'s Ratings"; //Adicionar titulo ao retornar

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery())
        ){
            int mid = Integer.parseInt(data.get("mid"));

            pstmt.setInt(1, mid);

            ResultSet rs = pstmt.executeQuery();

            return createRI(rs);
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidCommandVariableException();
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "SELECT Movie.*, ((one * 1. + two * 2 + three * 3 + four * 4 + five * 5) / nullif((one + two + three + four + five),0) ) as rating, one, two, three, four, five\n" +
                "FROM Movie\n" +
                "LEFT JOIN (\n" +
                "SELECT movie_id, COALESCE((one + [1]), one, [1]) as one, COALESCE((two + [2]), two, [2]) as two, COALESCE((three + [3]), three, [3]) as three, COALESCE((four + [4]), four, [4]) as four, COALESCE((five + [5]), five, [5]) as five\n" +
                "FROM\n" +
                "(\n" +
                "SELECT Rating.movie_id, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5]\n" +
                "FROM Rating\n" +
                "FULL JOIN (\n" +
                "SELECT movie_id, [1], [2], [3], [4], [5]\n" +
                "FROM\n" +
                "(SELECT movie_id, rating FROM Review GROUP BY rating, movie_ID) AS SourceTable\n" +
                "PIVOT\n" +
                "(\n" +
                "COUNT(SourceTable.rating)\n" +
                "FOR rating IN ([1], [2], [3], [4], [5])\n" +
                ") AS SourceTable) AS reviewRatings ON reviewRatings.movie_id = Rating.movie_id\n" +
                "GROUP BY Rating.movie_id, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5]\n" +
                ") AS average) AS ratings ON ratings.movie_id = Movie.movie_id\n" +
                "WHERE Movie.movie_id = ?";
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Titulo");
        columns.add("Average Rating");
        columns.add("One");
        columns.add("Two");
        columns.add("Three");
        columns.add("Four");
        columns.add("Five");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);

        ArrayList<String> line = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rs.getDate("release_year"));

        line.add(rs.getString("movie_id"));
        line.add(rs.getString("title"));
        line.add(String.format(Locale.FRENCH,"%.2f", rs.getFloat("rating")));
        line.add(rs.getString("one"));
        line.add(rs.getString("two"));
        line.add(rs.getString("three"));
        line.add(rs.getString("four"));
        line.add(rs.getString("five"));

        data.add(line);

        return new ResultInfo(rs.getString("title") + TITLE, columns, data);

    }
}
