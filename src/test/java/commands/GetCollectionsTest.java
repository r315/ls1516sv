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
public class GetCollectionsTest {

    @Before
    public void init() throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('x','y')");

            stmt.executeUpdate("DELETE FROM Collection");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");

            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('Best Movies','y')");
            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('x','z')");
            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('a','b')");
            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('c','d')");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Collection");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

        }
    }

    @Test
    public void GetCollectionsExecute_NoParameters()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("1"); line1.add("Best Movies"); line1.add("y");
        ArrayList<String> line2 = new ArrayList<>(); line2.add("2"); line2.add("x"); line2.add("z");
        ArrayList<String> line3 = new ArrayList<>(); line3.add("3"); line3.add("a"); line3.add("b");
        ArrayList<String> line4 = new ArrayList<>(); line4.add("4"); line4.add("c"); line4.add("d");

        data.add(line1); data.add(line2); data.add(line3); data.add(line4);

        ResultInfo result = new ResultInfo(null,title,data);

        /* --- */

        GetCollections stuff = new GetCollections();
        ResultInfo rs = stuff.execute(null);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetCollectionsExecute_top()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("1"); line1.add("Best Movies"); line1.add("y");
        ArrayList<String> line2 = new ArrayList<>(); line2.add("2"); line2.add("x"); line2.add("z");

        data.add(line1); data.add(line2);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("top","2");

        /* --- */

        GetCollections stuff = new GetCollections();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetCollectionsExecute_skip()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line3 = new ArrayList<>(); line3.add("3"); line3.add("a"); line3.add("b");
        ArrayList<String> line4 = new ArrayList<>(); line4.add("4"); line4.add("c"); line4.add("d");

        data.add(line3); data.add(line4);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("skip","2");

        /* --- */

        GetCollections stuff = new GetCollections();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetCollectionsExecute_skipTop()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line2 = new ArrayList<>(); line2.add("2"); line2.add("x"); line2.add("z");
        ArrayList<String> line3 = new ArrayList<>(); line3.add("3"); line3.add("a"); line3.add("b");

        data.add(line2); data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("top","2");
        param.put("skip","1");

        /* --- */

        GetCollections stuff = new GetCollections();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesExecute_NoCollections()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("skip","5");

        /* --- */

        GetCollections stuff = new GetCollections();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }
}
