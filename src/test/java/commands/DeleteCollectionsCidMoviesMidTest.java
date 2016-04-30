package commands;

import Strutures.ResultInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class DeleteCollectionsCidMoviesMidTest {

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
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            stmt.executeUpdate("INSERT INTO Has (collection_id,movie_id) VALUES ('1','1')");

            stmt.close();
        }
    }

    @After
    public void removeInserts() throws Exception {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Has WHERE collection_id=1");
            stmt.executeUpdate("DELETE FROM Movie WHERE movie_id=1");
            stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");

            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

        }
    }

    @Test
    public void DeleteCollectionsCidMoviesMidExecute() throws Exception {
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("Success");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("cid","1");
        param.put("mid","1");

        /* --- */

        DeleteCollectionsCidMoviesMid stuff = new DeleteCollectionsCidMoviesMid();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
    }
}
