package commands;

import Strutures.ResponseFormat.ResultInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlserver.ConnectionFactory;
import utils.DataBase;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Created by Luigi Sekuiya on 09/04/2016.
 */
public class GetTopsRatingsHigherAverageTest {

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
    public void GetTopsRatingsHigherAverageExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line3 = new ArrayList<>(); line3.add("Star Wars VI"); line3.add("1983"); line3.add("3,73");

        data.add(line3);

        ResultInfo result = new ResultInfo(null,title,data);

        /* --- */

        GetTopsRatingsHigherAverage command = new GetTopsRatingsHigherAverage();
        ResultInfo rs = command.execute(null);
        assertEquals(result.getValues(),rs.getValues());
    }
}
