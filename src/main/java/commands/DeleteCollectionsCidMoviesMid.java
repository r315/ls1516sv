package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
            
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Has WHERE movie_id = ? AND collection_id = ?");
            pstmt.setInt(1,mid);
            pstmt.setInt(2,cid);

            pstmt.executeUpdate();

            ResultInfo result = createRI();

            pstmt.close();

            return result;
            
        }
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private ResultInfo createRI() throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie removed");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line = new ArrayList<>();

        line.add("Success");

        data.add(line);

        return new ResultInfo(TITLE, columns, data);

    }

}
