package commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Predicate;

import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class PostCollectionsCidMoviesTest {

    public void clearDataBase() throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Has");
            stmt.executeUpdate("DELETE FROM Rating");
            stmt.executeUpdate("DELETE FROM Review");
            stmt.executeUpdate("DELETE FROM Collection");
            stmt.executeUpdate("DELETE FROM Movie");
            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.close();
        }
    }

    //@Before
    public void insertIntoDatabase(boolean hasMovieInCollection) throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('TestColection','Collections for testing')");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            if(hasMovieInCollection)
                stmt.executeUpdate("INSERT INTO Has VALUES (1,1)");
            stmt.close();
        }
    }

    //@After
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
    public void PostCollectionsExecute() throws Exception {
        clearDataBase();
        insertIntoDatabase(false);
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> line1 = new ArrayList<>(); line1.add("Success");

        data.add(line1);

        ResultInfo result = new ResultInfo(null,title,data);

        HashMap<String, String> param = new HashMap<>();
        param.put("cid","1");
        param.put("mid","1");

        /* --- */

        PostCollectionsCidMovies stuff = new PostCollectionsCidMovies();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
        removeInserts();
    }

    @Test
    public void shouldGetInvalidCommandParametersException(){
        Predicate<Exception> pe = e -> e instanceof InvalidCommandParametersException;
        testExceptions(null,pe);
        testExceptions(new HashMap<>(),pe);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("cid", "1");
        testExceptions(map,pe);
        map.clear();
        map.put("mid","12w");
        testExceptions(map,pe);
    }

    @Test
    public void shouldGetPostException(){
        try {
            clearDataBase();
            insertIntoDatabase(true);
            Predicate<Exception> pe = e -> e instanceof PostException;
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("cid", "123");
            map.put("mid","1");
            testExceptions(map,pe);
            map.replace("cid", "1");
            map.put("mid","142");
            testExceptions(map,pe);
            map.put("mid","1");
            testExceptions(map,pe);
            removeInserts();
        } catch (Exception e) {
            fail();
        }
    }

    private void testExceptions(HashMap<String, String> data, Predicate<Exception> pred){
        PostCollectionsCidMovies postcidmid = new PostCollectionsCidMovies();
        try{
            postcidmid.execute(data);
        }catch (Exception e){
            assertTrue(pred.test(e));
            return;
        }
        fail();
    }


}
