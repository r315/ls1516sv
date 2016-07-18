package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DeleteCollectionsCidMoviesMid extends CommandBase {
    private static final String INFO = "DELETE /collections/{cid}/movies/{mid} - removes the movie mid from the collections cid.";
    private final String TITLE = "Movie deleted from Collection";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws SQLException, InvalidCommandVariableException {
        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement select = conn.prepareStatement(getQuerySelect());
                PreparedStatement delete = conn.prepareStatement(getQueryDelete())
        ){
            int mid = Integer.parseInt(data.get("mid"));
            int cid = Integer.parseInt(data.get("cid"));

            select.setInt(1,cid);
            select.setInt(2,mid);

            ResultSet rs = select.executeQuery();
            
            delete.setInt(1,mid);
            delete.setInt(2,cid);

            delete.executeUpdate();

            return createRI(rs);

            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuerySelect(){
        return "SELECT Movie.title, Collection.name FROM Has " +
                "INNER JOIN Movie ON Movie.movie_id = Has.movie_id " +
                "INNER JOIN Collection ON Collection.collection_id = Has.collection_id " +
                "WHERE Collection.collection_id = ? AND Movie.movie_id = ?";
    }

    private String getQueryDelete(){
        return "DELETE FROM Has WHERE movie_id = ? AND collection_id = ?";
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
