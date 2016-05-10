package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sqlserver.ConnectionFactory;
import utils.Utils;
import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class DeleteCollectionsCidMoviesMid implements ICommand {
    private static final String INFO = "DELETE /collections/{cid}/movies/{mid} - removes the movie mid from the collections cid.";
    private final String TITLE = "Movie deleted from Collection";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mid, cid;

            try {
                mid = Utils.getInt(data.get("mid"));
                cid = Utils.getInt(data.get("cid"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            // TODO: cid & mid doesn't exist

            PreparedStatement pstmt = conn.prepareStatement("SELECT Movie.title, Collection.name FROM Has " +
                    "INNER JOIN Movie ON Movie.movie_id = Has.movie_id " +
                    "INNER JOIN Collection ON Collection.collection_id = Has.collection_id " +
                    "WHERE Collection.collection_id = ? AND Movie.movie_id = ?");
            pstmt.setInt(1,cid);
            pstmt.setInt(2,mid);

            ResultSet rs = pstmt.executeQuery();
            
            pstmt = conn.prepareStatement("DELETE FROM Has WHERE movie_id = ? AND collection_id = ?");
            pstmt.setInt(1,mid);
            pstmt.setInt(2,cid);

            pstmt.executeUpdate();

            ResultInfo result = createRI(rs);

            pstmt.close();

            return result;
            
        }
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();

        rs.next();

        columns.add(rs.getString("title") + " removed from collection " + rs.getString("name"));

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line = new ArrayList<>();

        line.add("Success");

        data.add(line);

        return new ResultInfo(TITLE, columns, data);

    }

}
