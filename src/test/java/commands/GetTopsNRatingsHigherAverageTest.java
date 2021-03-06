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

/**
 * Created by Luigi Sekuiya on 09/04/2016.
 */
public class GetTopsNRatingsHigherAverageTest {

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
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars V','19800629');");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars VI','19830525');");

            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Luis','Muito Bom','Gostei','5')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Red','Nao gostei','mau','1')");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('1','Hugo','Podia ser melhor','Meh','3')");

            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Luis','Muito Bom','Gostei','4');");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Red','Nao gostei','mau','2');");
            stmt.executeUpdate("INSERT INTO Review (movie_id,name,review,summary,rating) VALUES ('2','Hugo','Podia ser melhor','Meh','3');");

            stmt.executeUpdate("INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('1','1','2','3','4','5')");
            stmt.executeUpdate("INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('2','5','4','3','2','1');");
            stmt.executeUpdate("INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('3','3','4','1','7','11');");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Rating WHERE movie_id=1");
            stmt.executeUpdate("DELETE FROM Rating WHERE movie_id=2");
            stmt.executeUpdate("DELETE FROM Rating WHERE movie_id=3");

            stmt.executeUpdate("DELETE FROM Review WHERE movie_id=1");
            stmt.executeUpdate("DELETE FROM Review WHERE movie_id=2");

            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars IV'");
            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars V'");
            stmt.executeUpdate("DELETE FROM Movie WHERE title='Star Wars VI'");

            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Review, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Rating, RESEED, 0)");

            stmt.close();
        }
    }

    @Test
    public void GetTopsNRatingsHigherAverageExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();
        ArrayList<String> line3 = new ArrayList<>();

        line2.add("3");
        line2.add("Star Wars VI");
        line2.add("1983");
        line2.add("3,73");
        line1.add("1");
        line1.add("Star Wars IV");
        line1.add("1977");
        line1.add("3,56");
        line3.add("2");
        line3.add("Star Wars V");
        line3.add("1980");
        line3.add("2,44");


        data.add(line2); data.add(line1); data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("n","3");

        /* --- */

        GetTopsNRatingsHigherAverage command = new GetTopsNRatingsHigherAverage();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetTopsNRatingsHigherAverageExecute_top()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();
        ArrayList<String> line2 = new ArrayList<>();

        line2.add("3");
        line2.add("Star Wars VI");
        line2.add("1983");
        line2.add("3,73");
        line1.add("1");
        line1.add("Star Wars IV");
        line1.add("1977");
        line1.add("3,56");


        data.add(line2); data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("n","3");
        param.put("top","2");

        /* --- */

        GetTopsNRatingsHigherAverage command = new GetTopsNRatingsHigherAverage();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetTopsNRatingsHigherAverageExecute_skip()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();

        line1.add("1");
        line1.add("Star Wars IV");
        line1.add("1977");
        line1.add("3,56");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("n","2");
        param.put("skip","1");

        /* --- */

        GetTopsNRatingsHigherAverage command = new GetTopsNRatingsHigherAverage();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test
    public void GetTopsNRatingsHigherAverageExecute_topSkip()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();

        line1.add("1");
        line1.add("Star Wars IV");
        line1.add("1977");
        line1.add("3,56");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("n","3");
        param.put("top","1");
        param.put("skip","1");

        /* --- */

        GetTopsNRatingsHigherAverage command = new GetTopsNRatingsHigherAverage();
        ResultInfo rs = command.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }

    @Test(expected= InvalidCommandVariableException.class)
    public void GetTopsNRatingsHigherAverageExecute_invalidN()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("n","-1");

        /* --- */

        GetTopsNRatingsHigherAverage command = new GetTopsNRatingsHigherAverage();
        command.execute(param);
    }

    @Test(expected= InvalidCommandVariableException.class)
    public void GetTopsNRatingsHigherAverageExecute_nIsntNumber()throws Exception{
        HashMap<String, String> param = new HashMap<>();
        param.put("n","one");

        /* --- */

        GetTopsNRatingsHigherAverage command = new GetTopsNRatingsHigherAverage();
        command.execute(param);
    }
}
