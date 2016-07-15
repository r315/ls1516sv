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
import java.util.ArrayList;
import java.util.HashMap;

public class GetMoviesMidReviews implements ICommand {
    private static final String INFO = "GET /movies/{mid}/reviews - returns all the reviews for the movie identified by mid.";
    private final String TITLE = "'s Reviews"; //Adicionar titulo ao retornar

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
            int mid = Utils.getInt(data.get("mid"));

            pstmt.setInt(1, mid);
            pstmt.setInt(2, skip);

            ResultSet rs = pstmt.executeQuery();

            return createRI(rs);

        } catch (NumberFormatException e) {
            throw new InvalidCommandVariableException();
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery(Boolean topB, int top) {
        String query = "SELECT Movie.movie_id, Movie.title, Review.review_id, Review.name, Review.summary, Review.rating " +
                        "FROM Review " +
                        "RIGHT JOIN Movie ON Review.movie_id=Movie.movie_id " +
                        "WHERE Movie.movie_id = ? " +
                        "ORDER BY Movie.movie_id " +
                        "OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie's ID");
        columns.add("Movie's Title");
        columns.add("Review's ID");
        columns.add("Username");
        columns.add("Rating");
        columns.add("Summary");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (!rs.next()) return new ResultInfo(TITLE, columns, data);
        String title = rs.getString("title");

        if((rs.getString("review_id") != null)) {
            do {
                ArrayList<String> line = new ArrayList<>();

                line.add(rs.getString("movie_id"));
                line.add(rs.getString("title"));
                line.add(rs.getString("review_id"));
                line.add(rs.getString("name"));
                line.add(rs.getString("rating"));
                line.add(rs.getString("summary"));

                data.add(line);
            } while (rs.next());
        }
        return new ResultInfo(title + TITLE, columns, data);
    }
}
