package commands;

import Strutures.CNode;
import Strutures.CommandInfo;
import Strutures.ResultInfo;
import logic.MapManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlserver.ConnectionFactory;

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

            stmt.close();
        }
    }

    @Test
    public void GetMoviesMidExecute()throws Exception{
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>();

        line1.add("Star Wars IV");
        line1.add("1977");
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

        GetMoviesMidRatings stuff = new GetMoviesMidRatings();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
        }
}
