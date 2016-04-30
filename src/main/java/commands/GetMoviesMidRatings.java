package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import utils.Utils;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.*;


public class GetMoviesMidRatings implements ICommand {
    private static final String INFO = "GET /movies/{mid}/ratings - returns the rating information for the movie identified by mid.";
    private final String TITLE = "'s Ratings"; //Adicionar titulo ao retornar

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        // TODO: data doesn't exist (nullpointerexception) 
        
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID;

            try {
                mID = Utils.getInt(data.get("mid"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);

            ResultSet rs = pstmt.executeQuery();

            ResultInfo result = createRI(rs);

            pstmt.close();

            return result;
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "SELECT title, release_year, COALESCE((one + [1]), one, [1]) as one, COALESCE((two + [2]), two, [2]) as two, COALESCE((three + [3]), three, [3]) as three, COALESCE((four + [4]), four, [4]) as four, COALESCE((five + [5]), five, [5]) as five " +
                "FROM " +
                "(" +
                "SELECT Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5] " +
                "FROM Movie " +
                "LEFT JOIN Rating ON Movie.movie_id = Rating.movie_id " +
                "LEFT JOIN ( " +
                        "SELECT movie_id, [1], [2], [3], [4], [5] " +
                        "FROM " +
                        "(SELECT movie_id, rating FROM Review GROUP BY rating, movie_ID) AS SourceTable " +
                        "PIVOT " +
                        "( " +
                                "COUNT(SourceTable.rating) " +
                                "FOR rating IN ([1], [2], [3], [4], [5]) " +
                        ") AS SourceTable) AS reviewRatings ON reviewRatings.movie_id = Movie.movie_id " +
                "WHERE Movie.movie_id = ? " +
                "GROUP BY Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5] " +
                ") AS average";
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Titulo");
        columns.add("Ano de Lançamento");
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

        Float average = (float) (rs.getInt("one") + rs.getInt("two") * 2 + rs.getInt("three") * 3 + rs.getInt("four") * 4 + rs.getInt("five") * 5)
                / (rs.getInt("one") + rs.getInt("two") + rs.getInt("three") + rs.getInt("four") + rs.getInt("five"));

        line.add(rs.getString("title"));
        line.add(Integer.toString(calendar.get(Calendar.YEAR)));
        line.add(String.format("%.2f", average));
        line.add(rs.getString("one"));
        line.add(rs.getString("two"));
        line.add(rs.getString("three"));
        line.add(rs.getString("four"));
        line.add(rs.getString("five"));

        data.add(line);

        return new ResultInfo(rs.getString("title") + TITLE, columns, data);

    }
}
