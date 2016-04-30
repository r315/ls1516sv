package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class DeleteCollectionsCidMoviesMid implements ICommand {
    private static final String INFO = "DELETE /collections/{cid}/movies/{mid} - removes the movie mid from the collections cid.";
    //private final String TITLE = "Movies List";

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

            // TODO: cid doesn't exist 
            
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ? WHERE movie_id=? AND collection_id=?");
            pstmt.setString(1,"Has");
            pstmt.setInt(2,mid);
            pstmt.setInt(3,cid);

            pstmt.executeQuery();
            
        }
        return null;
    }

    @Override
    public String getInfo() {
        return INFO;
    }

}
