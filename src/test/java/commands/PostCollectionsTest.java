package commands;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class PostCollectionsTest {

    @Before
    public void init() throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('x','y')");

            stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");

        }
    }

    @Test
    public void PostCollectionsExecute() throws Exception {
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("1");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("name","TheBest");
        param.put("description","The+greatest+movies+of+all+time");

        /* --- */

        PostCollections command = new PostCollections();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }
}
