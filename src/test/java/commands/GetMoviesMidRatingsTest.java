package commands;

import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandVariableException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlserver.ConnectionFactory;
import utils.DataBase;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GetMoviesMidRatingsTest {

    @Before
    public void init() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            DataBase.clear();
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5')");
            stmt.executeUpdate("INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('1','1','2','3','4','5')");

            stmt.executeUpdate("DELETE FROM Rating");
            stmt.executeUpdate("DELETE FROM Review");
            stmt.executeUpdate("DELETE FROM Movie");

            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Review, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Rating, RESEED, 0)");

            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");

            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Red','Nao gostei','mau','1')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Hugo','Podia ser melhor','Meh','3')");

            stmt.executeUpdate("INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('1','1','2','3','4','5')");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Rating WHERE movie_id=1");

            stmt.executeUpdate("DELETE FROM Review WHERE movie_id=1");

            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars IV'");

            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Review, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Rating, RESEED, 0)");

            stmt.close();
        }
    }

    @Test
    public void GetMoviesMidRatingsExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();

        line1.add("1");
        line1.add("Star Wars IV");
        line1.add("3,56");
        line1.add("2");
        line1.add("2");
        line1.add("4");
        line1.add("4");
        line1.add("6");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","1");

        /* --- */

        GetMoviesMidRatings command = new GetMoviesMidRatings();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
        }

    @Test
    public void GetMoviesMidRatingsExecute_invalidMid()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("mid","10");

        /* --- */

        GetMoviesMidRatings command = new GetMoviesMidRatings();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test(expected=InvalidCommandVariableException.class)
    public void GetMoviesMidRatingsExecute_midIsntNumber()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("mid","one");

        /* --- */

        GetMoviesMidRatings command = new GetMoviesMidRatings();
        ResultInfo rs = command.execute(param);
    }
}
