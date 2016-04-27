package commands;

import Strutures.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GetMoviesTest {

    @Before
    public void init() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars V','19800629')");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars VI','19830525')");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars IV'");
            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars V'");
            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars VI'");

            stmt.close();
        }
    }

    @Test
    public void GetMoviesExecute_NoParameters()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("Star Wars IV"); line1.add("1977");
        ArrayList<String> line2 = new ArrayList<>(); line2.add("Star Wars V"); line2.add("1980");
        ArrayList<String> line3 = new ArrayList<>(); line3.add("Star Wars VI"); line3.add("1983");

        data.add(line1); data.add(line2); data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        /* --- */

        GetMovies stuff = new GetMovies();
        ResultInfo rs = stuff.execute(null);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesExecute_Top()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("Star Wars IV"); line1.add("1977");
        ArrayList<String> line2 = new ArrayList<>(); line2.add("Star Wars V"); line2.add("1980");

        data.add(line1); data.add(line2);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("top","2");

        /* --- */

        GetMovies stuff = new GetMovies();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesExecute_Skip()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line2 = new ArrayList<>(); line2.add("Star Wars V"); line2.add("1980");
        ArrayList<String> line3 = new ArrayList<>(); line3.add("Star Wars VI"); line3.add("1983");

        data.add(line2); data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("skip","1");

        /* --- */

        GetMovies stuff = new GetMovies();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesExecute_TopSkip()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line2 = new ArrayList<>(); line2.add("Star Wars V"); line2.add("1980");

        data.add(line2);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("skip","1");
        param.put("top","1");

        /* --- */

        GetMovies stuff = new GetMovies();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }
}
