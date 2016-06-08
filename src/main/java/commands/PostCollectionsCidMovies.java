package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import exceptions.PostException;
import sqlserver.ConnectionFactory;
import utils.Utils;
import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandParametersException;

/**
 * Created by Luigi Sekuiya on 29/04/2016.
 */
public class PostCollectionsCidMovies implements ICommand {
    private static final String INFO = "POST /collections/{cid}/movies/ - adds a movie to the cid collection, given \"mid\".";
    public static final int ENTRY_EXISTS = 2627;
    public static final int ENTRY_NOT_FOUND = 547;
    private final String TITLE = "Movie inserted into Collection";


    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        int cid, mid;
        PreparedStatement pstmt;

        if (data == null) throw new InvalidCommandParametersException();

        try {
            cid = Utils.getInt(data.get("cid"));
            mid = Utils.getInt(data.get("mid"));
        } catch (NumberFormatException e) {
            throw new InvalidCommandParametersException();
        }

        try(Connection conn = ConnectionFactory.getConn()) {
            pstmt = conn.prepareStatement(getQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, cid);
            pstmt.setInt(2, mid);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            ResultInfo result = createRI();
            pstmt.close();
            return result;
        }catch (SQLException e){//TODO: check bad id entry ex: wr324
            switch(e.getErrorCode()){
                case ENTRY_EXISTS:
                    throw new PostException("Movie Already Exists");
                case ENTRY_NOT_FOUND:
                    throw new PostException("Movie not found on database");
            }
        }
        return new ResultInfo(TITLE, null,null);
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "INSERT INTO Has (collection_id, movie_id) VALUES (?,?)";
    }

    private ResultInfo createRI() throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie inserted");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line = new ArrayList<>();

        line.add("Success");

        data.add(line);

        return new ResultInfo(TITLE, columns, data);

    }

}
