package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
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
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        int cid, mid;
        PreparedStatement pstmt;

        try(Connection conn = ConnectionFactory.getConn()) {
            cid = Utils.getInt(data.get("cid"));
            mid = Utils.getInt(data.get("mid"));
            pstmt = conn.prepareStatement(getQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, cid);
            pstmt.setInt(2, mid);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            ResultInfo result = createRI();
            pstmt.close();
            return result;
        }catch (SQLException e){
            throw new PostException(e.getErrorCode());
        }catch (NullPointerException | NumberFormatException e){
            throw new InvalidCommandParametersException();
        }
        //return new ResultInfo(TITLE, null,null);
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
