package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PostCollectionsCidMovies extends CommandBase {
    private static final String INFO = "POST /collections/{cid}/movies/ - adds a movie to the cid collection, given \"mid\".";
    private final String TITLE = "Movie inserted into Collection";


    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        int cid, mid;

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(), PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            cid = Utils.getInt(data.get("cid"));
            mid = Utils.getInt(data.get("mid"));

            pstmt.setInt(1, cid);
            pstmt.setInt(2, mid);

            pstmt.executeUpdate();

            return createRI();

        }catch (SQLException e){
            int err = e.getErrorCode();
            if(err == PostException.ENTRY_EXISTS)
                throw new PostException(err,"Movie Already Exist in collection!");
            else
                throw new PostException(err,"Movie not found!");
        }catch (NullPointerException | NumberFormatException e){
            throw new InvalidCommandParametersException();
        }
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
