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
import exceptions.InvalidCommandVariableException;

public class GetCollectionsCidTest {

    @Before
    public void init() throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('x','y')");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");

            stmt.executeUpdate("DELETE FROM Movie");
            stmt.executeUpdate("DELETE FROM Collection");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('Best Movies','y')");
            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('SomethingHappened','x')");

            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars V','19800629');");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars VI','19830525');");

            stmt.executeUpdate("INSERT INTO Has (collection_id,movie_id) VALUES ('1','1')");
            stmt.executeUpdate("INSERT INTO Has (collection_id,movie_id) VALUES ('1','2')");
            stmt.executeUpdate("INSERT INTO Has (collection_id,movie_id) VALUES ('1','3')");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Has");
            stmt.executeUpdate("DELETE FROM Movie");
            stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");
            stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=2");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

        }
    }

    @Test
    public void GetCollectionsCidExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("1"); line1.add("1"); line1.add("Star Wars IV"); line1.add("1977");
        ArrayList<String> line2 = new ArrayList<>(); line2.add("1"); line2.add("2"); line2.add("Star Wars V"); line2.add("1980");
        ArrayList<String> line3 = new ArrayList<>(); line3.add("1"); line3.add("3"); line3.add("Star Wars VI"); line3.add("1983");

        data.add(line1); data.add(line2); data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("cid","1");

        /* --- */

        GetCollectionsCid stuff = new GetCollectionsCid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetCollectionsCidExecute_InvalidCid()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("cid","10");

        /* --- */

        GetCollectionsCid stuff = new GetCollectionsCid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test(expected= InvalidCommandVariableException.class)
    public void GetCollectionsCidExecute_CidIsntNumber()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("cid","one");

        /* --- */

        GetCollectionsCid stuff = new GetCollectionsCid();
        ResultInfo rs = stuff.execute(param);
    }

    @Test
    public void GetCollectionsCidSomethingHappened()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("cid","2");

        /* --- */

        GetCollectionsCid stuff = new GetCollectionsCid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }
}
