package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PostCollections implements ICommand {
    private static final String INFO = "POST /collections - creates a new collection and returns its identifier, given the parameters \"name\" and \"description\".";
    private final String TITLE = "Collection Created";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        String name = data.get("name");
        String desc = data.get("description");

        if(name == null || desc == null)
            throw new InvalidCommandParametersException();

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            pstmt.setString(1, name);
            pstmt.setString(2, desc);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            ResultInfo result = createRI(rs);

            return result;
        }catch (SQLException e){
            int err = e.getErrorCode();
            if(err == PostException.ENTRY_EXISTS)
                throw new PostException(err,"Collection Already Exists!");
            else
                throw new PostException(err,e.getMessage());
        }catch (NullPointerException | NumberFormatException e){
            throw new InvalidCommandParametersException("Bad parameters");
        }
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery() {
        return "INSERT INTO Collection (name, description) VALUES (?,?)";
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Collection created with ID");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line = new ArrayList<>();

        rs.next();

        line.add(Long.toString(rs.getLong(1)));

        data.add(line);

        return new ResultInfo(TITLE, columns, data);

    }

}
