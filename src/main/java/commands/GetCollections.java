package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
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
public class GetCollections implements ICommand {
    private static final String INFO = "GET /collections - returns the list of collections, using the insertion order.";
    private final String TITLE = "Collections list";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        Boolean topB = false;
        int skip = 0, top = 1;
        String orderBy = "title";

        if (data != null) {
            topB = (data.get("top") != null);
            HashMap<String, Integer> skiptop = Utils.getSkipTop(data.get("skip"), data.get("top"));

            skip = skiptop.get("skip");
            top = skiptop.get("top");
        }



        try(Connection conn = ConnectionFactory.getConn()) {
            PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top));
            pstmt.setInt(1, skip);

            ResultSet rs = pstmt.executeQuery();

            ResultInfo result = createRI(rs);

            pstmt.close();

            return result;
        }

    }

    @Override
    public String getInfo() {
        return null;
    }

    private String getQuery(Boolean topB, int top) {
        String query = "SELECT * FROM Collection ORDER BY collection_id OFFSET ? ROWS";
        if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
        return query;
    }

    private ResultInfo createRI(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Name");
        columns.add("Description");

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        while(rs.next()) {
            ArrayList<String> line = new ArrayList<>();

            line.add(rs.getString("name"));
            line.add(rs.getString("description"));

            data.add(line);
        }

        return new ResultInfo(TITLE, columns, data);
    }
}
