package commands;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
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

public class GetMoviesMidReviewsRidTest {

    @Before
    public void init() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5')");

            stmt.executeUpdate("DELETE FROM Review");
            stmt.executeUpdate("DELETE FROM Movie");

            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Review, RESEED, 0)");

            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");

            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5')");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Review WHERE movie_id=1");

            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars IV'");

            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Review, RESEED, 0)");

            stmt.close();
        }
    }

    @Test
    public void GetMoviesMidReviewRidExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();

        line1.add("1");
        line1.add("Star Wars IV");
        line1.add("1");
        line1.add("Luis");
        line1.add("5");
        line1.add("Gostei");
        line1.add("Muito Bom");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");
        param.put("rid","1");

        /* --- */

        GetMoviesMidReviewsRid stuff = new GetMoviesMidReviewsRid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test(expected= InvalidCommandVariableException.class)
    public void GetMoviesMidReviewRidExecute_midIsntNumber()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("mid","one");
        param.put("rid","1");

        /* --- */

        GetMoviesMidReviewsRid stuff = new GetMoviesMidReviewsRid();
        stuff.execute(param);
    }

    @Test
    public void GetMoviesMidReviewRidExecute_invalidMid()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","10");
        param.put("rid","1");

        /* --- */

        GetMoviesMidReviewsRid stuff = new GetMoviesMidReviewsRid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test(expected= InvalidCommandVariableException.class)
    public void GetMoviesMidReviewRidExecute_ridIsntNumber()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");
        param.put("rid","one");

        /* --- */

        GetMoviesMidReviewsRid stuff = new GetMoviesMidReviewsRid();
        stuff.execute(param);
    }

    @Test
    public void GetMoviesMidReviewRidExecute_invalidRid()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");
        param.put("rid","10");

        /* --- */

        GetMoviesMidReviewsRid stuff = new GetMoviesMidReviewsRid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }
}
