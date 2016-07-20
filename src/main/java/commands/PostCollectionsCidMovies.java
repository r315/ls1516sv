package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import sqlserver.ConnectionFactory;

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
            cid = Integer.parseInt(data.get("cid"));
            mid = Integer.parseInt(data.get("mid"));
            pstmt.setInt(1, cid);
            pstmt.setInt(2, mid);
            pstmt.executeUpdate();
            return createRI();
        }catch (SQLException e){
            int errorCode= e.getErrorCode();
            switch(errorCode){
                case PostException.ENTRY_EXISTS:
                    throw new PostException(errorCode,"Movie already exists in collection!");
                case PostException.ENTRY_NOT_FOUND:
                    throw new PostException(errorCode,"Movie not found!");
                default:
                    throw e;
            }
        }catch (NumberFormatException e){
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

    private ResultInfo createRI() {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie inserted");

        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> line = new ArrayList<>();

        line.add("Success");
        data.add(line);

        return new ResultInfo(TITLE, columns, data);

    }

}
