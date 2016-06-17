package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class GetMovies implements ICommand {
    private static final String INFO = "GET /movies - returns a list with all movies.";
    private final String TITLE = "Movies List";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        Boolean topB = false;
        int skip = 0, top = 1;
        String orderBy = "title";

        if (data != null) {
            topB = (data.get("top") != null);
            HashMap<String, Integer> skiptop = Utils.getSkipTop(data.get("skip"), data.get("top"));

            skip = skiptop.get("skip");
            top = skiptop.get("top");

            if (data.get("sortBy") != null) orderBy = orderBy(data.get("sortBy"));
        }



        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top, orderBy))
        ){

            pstmt.setInt(1, skip);

            ResultSet rs = pstmt.executeQuery();

            ResultInfo result = createRI(rs);

            return result;
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery(Boolean topB, int top, String orderBy) {
        String query = "SELECT Movie.*, ((one * 1. + two * 2 + three * 3 + four * 4 + five * 5) / nullif((one + two + three + four + five),0) ) as rating FROM Movie\n" +
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
                "ORDER BY " + orderBy + " OFFSET ? ROWS";

        /*String query = "SELECT * FROM Movie ORDER BY " + orderBy + " OFFSET ? ROWS";*/
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Title");
        columns.add("Release Year");
        columns.add("Rating");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rs.getDate("release_year"));

            line.add(rs.getString("movie_id"));
            line.add(rs.getString("title"));
            line.add(Integer.toString(calendar.get(Calendar.YEAR)));
            line.add(String.format(Locale.FRENCH,"%.2f", rs.getFloat("rating")));

            data.add(line);
        }

        return new ResultInfo(TITLE, columns, data);
    }

    private String orderBy(String sortBy) {

        String orderBy;

        switch(sortBy){
            case "addedDate":
                orderBy = "movie_id";
                break;
            case "addedDateDesc":
                orderBy = "movie_id DESC";
                break;
            case "year":
                orderBy = "release_year";
                break;
            case "yearDesc":
                orderBy = "release_year DESC";
                break;
            case "title":
                orderBy = "title";
                break;
            case "titleDesc":
                orderBy = "title DESC";
                break;
            case "rating":
                orderBy = "rating";
                break;
            case "ratingDesc":
                orderBy = "rating DESC";
                break;
            default: orderBy = "title";
        }

        return orderBy;
    }

}
