package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandParametersException;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Luigi Sekuiya on 29/04/2016.
 */
public class PostCollectionsCidMovies implements ICommand {
    private static final String INFO = "POST /collections/{cid}/movies/ - adds a movie to the cid collection, given \"mid\".";
    private final String TITLE = "Movie inserted into Collection";


    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        int cid, mid;

        if (data == null) throw new InvalidCommandParameters();

        try {
            cid = Utils.getInt(data.get("cid"));
            mid = Utils.getInt(data.get("mid"));
        } catch (NumberFormatException e) {
            throw new InvalidCommandParametersException();
        }

        try(Connection conn = ConnectionFactory.getConn()) {
            PreparedStatement pstmt = conn.prepareStatement(getQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, cid);
            pstmt.setInt(2, mid);

            // TODO: Handle if cid or mid don't exist
            // TODO: Handle if adding the same movie again to a collection
            pstmt.executeUpdate();


            ResultSet rs = pstmt.getGeneratedKeys();

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
        return "INSERT INTO Has (collection_id, movie_id) VALUES (?,?)";
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie inserted");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line = new ArrayList<>();

        line.add(Long.toString(rs.getLong(1)));

        data.add(line);

        return new ResultInfo(TITLE, columns, data);

    }

}
