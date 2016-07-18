package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import sqlserver.ConnectionFactory;
import utils.Pair;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetCollections extends CommandBase {
    private static final String INFO = "GET /collections - returns the list of collections, using the insertion order.";
    private final String TITLE = "Collections list";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        int skip = Utils.getSkip(data.get("skip"));
        int top = Utils.getTop(data.get("top"));

        try(
                Connection conn = ConnectionFactory.getConn();
                PreparedStatement pstmt = conn.prepareStatement(getQuery(top))
        ){
            pstmt.setInt(1, skip);

            ResultSet rs = pstmt.executeQuery();

            return createRI(rs);
        }

    }

    @Override
    public String getInfo() {
        return INFO;
    }

    private String getQuery(int top) {
        String query = "SELECT * FROM Collection ORDER BY collection_id OFFSET ? ROWS";
        if (top > 0) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ID");
        columns.add("Name");
        columns.add("Description");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();

            line.add(rs.getString("collection_id"));
            line.add(rs.getString("name"));
            line.add(rs.getString("description"));

            data.add(line);
        }

        return new ResultInfo(TITLE, columns, data);
    }
}
