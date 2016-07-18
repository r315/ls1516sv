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
import java.util.HashMap;

public class GetCollectionMoviesMid extends CommandBase {

    private static final String INFO = "POST /collections/having/movie/{mid2}";
    private final String TITLE = "Movie Inserted";

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

        } catch (NumberFormatException e) {
            throw new InvalidCommandVariableException();
        }
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "SELECT Collection.collection_id, Collection.name FROM Has\n" +
                "INNER JOIN Collection ON Has.collection_id = Collection.collection_id\n" +
                "WHERE movie_id=?";
    }
    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Name");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();

            line.add(rs.getString("collection_id"));
            line.add(rs.getString("name"));

            data.add(line);
        }

        return new ResultInfo(TITLE, columns, data);
    }
}
