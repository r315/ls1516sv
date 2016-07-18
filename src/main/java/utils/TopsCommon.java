package utils;

import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandParametersException;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class TopsCommon {

    public static ResultInfo getNRatings(HashMap<String, String> data, String title, boolean desc) throws InvalidCommandParametersException, SQLException, InvalidCommandVariableException {
        final boolean IS_RATING = true;
        return getNResultInfo(data, title, desc, IS_RATING);
    }

    public static ResultInfo getNReview(HashMap<String, String> data, String title, boolean desc) throws InvalidCommandParametersException, SQLException, InvalidCommandVariableException {
        final boolean IS_RATING = false;
        return getNResultInfo(data, title, desc, IS_RATING);
    }

    private static ResultInfo getNResultInfo(HashMap<String, String> data, String title, boolean descOrder, boolean isRating) throws InvalidCommandParametersException, SQLException, InvalidCommandVariableException {
        int skip = Utils.getSkip(data.get("skip"));
        int top = Utils.getTop(data.get("top"));

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement((isRating) ? getQueryNRating(top, descOrder) : getQueryNReview(top, descOrder))
        ){
            int n = Utils.getInt(data.get("n"));
            if (n < 0) throw new InvalidCommandVariableException();

            pstmt.setInt(1, n);
            pstmt.setInt(2, skip);

            ResultSet rs = pstmt.executeQuery();

            return createNRI(rs, n, title, isRating);

        } catch (NumberFormatException e) {
            throw new InvalidCommandVariableException();
        }
    }

    private static String getQueryNRating(int top, boolean descOrder) {
        String query = "SELECT * FROM ( " +
                "SELECT Movie.*, ((one * 1. + two * 2 + three * 3 + four * 4 + five * 5) / nullif((one + two + three + four + five),0)) as rating FROM Movie\n" +
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
                "ORDER BY rating" + (descOrder ? " DESC" : "") + " OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY" +
                ") AS tudo ORDER BY rating DESC OFFSET ? ROWS";
        if (top > 0) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private static String getQueryNReview(int top, boolean descOrder) {
        String query = "SELECT * FROM ( " +
                "SELECT Movie.movie_id, Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount " +
                "FROM Movie " +
                "LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
                "GROUP BY Movie.title, Movie.release_year, Movie.movie_id " +
                "ORDER BY revcount " + (descOrder ? "DESC " : "") +
                "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY" +
                ") AS tudo ORDER BY revcount DESC OFFSET ? ROWS";
        if (top > 0) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private static ResultInfo createNRI(ResultSet rs, int n, String title, boolean rating) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Title");
        columns.add("Release Year");
        if (rating) columns.add("Average Rating");
        else columns.add("Review Count");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            line.add(rs.getString("movie_id"));
            line.add(rs.getString("title"));
            line.add(Integer.toString(calendar.get(Calendar.YEAR)));
            if (rating) line.add(String.format(Locale.FRENCH,"%.2f", rs.getFloat("rating")));
            else line.add(rs.getString("revcount"));

            data.add(line);
        }

        return new ResultInfo(n + title, columns, data);
    }

    //Not n

    public static ResultInfo getRatings(String title, boolean desc) throws SQLException {
        final boolean IS_RATING = true;
        return getResultInfo(title, desc, IS_RATING);
    }

    public static ResultInfo getReview(String title) throws SQLException {
        final boolean IS_RATING = false;
        return getResultInfo(title, true, IS_RATING);
    }

    private static ResultInfo getResultInfo(String title, Boolean desc, Boolean rating) throws SQLException {
        try(
                Connection conn = ConnectionFactory.getConn();
                Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(rating ? getQueryRating(desc) : getQueryReview());

            return createRI(rs, title, rating);
        }
    }

    private static String getQueryRating(boolean DESC) {
        return "SELECT TOP 1 Movie.*, ((one * 1. + two * 2 + three * 3 + four * 4 + five * 5) / nullif((one + two + three + four + five),0)) as rating FROM Movie\n" +
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
                "ORDER BY rating" + (DESC ? " DESC" : "");
    }

    private static String getQueryReview() {
        return "SELECT TOP 1 Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount " +
                "FROM Movie " +
                "LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
                "GROUP BY Movie.title, Movie.release_year " +
                "ORDER BY revcount DESC, Movie.title";
    }

    private static ResultInfo createRI(ResultSet rs, String title, boolean rating) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Title");
        columns.add("Release Year");
        if (rating) columns.add("Rating");
        else columns.add("Review Count");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(title, columns, data);

        ArrayList<String> line = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rs.getDate("release_year"));

        line.add(rs.getString("title"));
        line.add(Integer.toString(calendar.get(Calendar.YEAR)));
        if (rating) line.add(String.format(Locale.FRENCH,"%.2f", rs.getFloat("rating")));
        else line.add(rs.getString("revcount"));

        data.add(line);

        return new ResultInfo(title, columns, data);
    }

}
