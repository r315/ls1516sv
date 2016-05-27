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

public class GetMoviesMidReviewsTest {

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
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Red','Nao gostei','mau','1')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Hugo','Podia ser melhor','Meh','3')");

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
    public void GetMoviesMidReviewsExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();
        ArrayList<String> line3 = new ArrayList<>();

        line1.add("1");
        line1.add("Luis");
        line1.add("5");
        line1.add("Gostei");

        line2.add("2");
        line2.add("Red");
        line2.add("1");
        line2.add("mau");

        line3.add("3");
        line3.add("Hugo");
        line3.add("3");
        line3.add("Meh");

        data.add(line1);
        data.add(line2);
        data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");

        /* --- */

        GetMoviesMidReviews stuff = new GetMoviesMidReviews();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesMidReviewsExecute_Skip()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line2 = new ArrayList<>();
        ArrayList<String> line3 = new ArrayList<>();

        line2.add("2");
        line2.add("Red");
        line2.add("1");
        line2.add("mau");

        line3.add("3");
        line3.add("Hugo");
        line3.add("3");
        line3.add("Meh");

        data.add(line2);
        data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");
        param.put("skip","1");

        /* --- */

        GetMoviesMidReviews stuff = new GetMoviesMidReviews();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesMidReviewsExecute_Top()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();

        line1.add("1");
        line1.add("Luis");
        line1.add("5");
        line1.add("Gostei");

        line2.add("2");
        line2.add("Red");
        line2.add("1");
        line2.add("mau");

        data.add(line1);
        data.add(line2);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");
        param.put("top","2");

        /* --- */

        GetMoviesMidReviews stuff = new GetMoviesMidReviews();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesMidReviewsExecute_SkipTop()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line2 = new ArrayList<>();

        line2.add("2");
        line2.add("Red");
        line2.add("1");
        line2.add("mau");

        data.add(line2);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");
        param.put("skip","1");
        param.put("top","1");

        /* --- */

        GetMoviesMidReviews stuff = new GetMoviesMidReviews();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetMoviesMidReviewsExecute_invalidMid()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","10");

        /* --- */

        GetMoviesMidReviews stuff = new GetMoviesMidReviews();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test(expected= InvalidCommandVariableException.class)
    public void GetMoviesMidReviewsExecute_midIsntNumber()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("mid","one");

        /* --- */

        GetMoviesMidReviews stuff = new GetMoviesMidReviews();
        ResultInfo rs = stuff.execute(param);
    }
}
