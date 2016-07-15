package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;
import utils.Pair;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsNReviewsLowerCount implements ICommand {
    private static final String INFO = "GET /tops/{n}/reviews/lower/count - returns a list with the n movies with lower review count.";
    private final String TITLE = " Movies with lower review count"; //Add n before

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        String topS = data.get("top");

        Boolean topB = (topS != null);
        int skip, top;

        Pair<Integer, Integer> skiptop = Utils.getSkipTop(data.get("skip"), topS);

        skip = skiptop.value1;
        top = skiptop.value2;

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top))
        ) {

            int n = Utils.getInt(data.get("n"));
            if (n < 0) throw new InvalidCommandVariableException();

            pstmt.setInt(1, n);
            pstmt.setInt(2, skip);

            ResultSet rs = pstmt.executeQuery();

            return GetTopsNReviewsHigherCount.createRI(rs, n, TITLE);

        } catch (NumberFormatException e) {
            throw new InvalidCommandVariableException();
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery(Boolean topB, int top) {
        String query = "SELECT movie_id, title, release_year, revcount FROM ( \n" +
                "SELECT Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount, Movie.movie_id\n" +
                "FROM Movie\n" +
                "LEFT JOIN Review ON Review.movie_id = Movie.movie_id\n" +
                "GROUP BY Movie.title, Movie.release_year, Movie.movie_id\n" +
                "ORDER BY revcount\n" +
                "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY\n" +
                ") AS tudo ORDER BY revcount DESC, movie_id OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

}