package commands;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;

import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResponseFormat.ResultInfo;
import utils.DataBase;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class PostCollectionsCidMoviesTest {

    private static PostCollectionsCidMovies post;


    @BeforeClass
    public static void setUp() throws Exception {
        post = new PostCollectionsCidMovies();
    }

    @Test
    public void PostCollectionsExecute() throws SQLException, InvalidCommandException {
        DataBase.clear();
        DataBase.createCollection(false);
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> line1 = new ArrayList<>(); line1.add("Success");
        data.add(line1);
        ResultInfo result = new ResultInfo(null,title,data);
        HashMap<String, String> param = new HashMap<>();
        param.put("cid","1");
        param.put("mid","1");
        PostCollectionsCidMovies stuff = new PostCollectionsCidMovies();
        ResultInfo rs = stuff.execute(param);
        assertEquals(result.getValues(),rs.getValues());
        DataBase.deleteCollections();
    }

    @Test(expected = InvalidCommandParametersException.class)
      public void shouldGetExceptionWithNoData() throws SQLException, InvalidCommandException {
        post.execute(new HashMap<>());
    }

    @Test(expected = InvalidCommandParametersException.class)
    public void shouldGetExceptionOnMalformedMid() throws SQLException, InvalidCommandException {
        commonExceptions(() -> {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("cid", "1");
            map.put("mid", "12w");
            return map;
        },false);
    }

    @Test(expected = PostException.class)
    public void shouldGetExceptionOnInvalidCid() throws SQLException, InvalidCommandException {
        commonExceptions(() -> {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("cid", "4");
            map.put("mid", "1");
            return map;
        },true);
    }

    @Test(expected = PostException.class)
    public void shouldGetExceptionOnInvalidMid() throws SQLException, InvalidCommandException {
        commonExceptions(() -> {
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("cid", "1");
                    map.put("mid", "4");
                    return map;
                },true);
    }

    @Test(expected = PostException.class)
    public void shouldGetExceptionOnDuplicatedMid() throws SQLException, InvalidCommandException {
            HashMap<String,String> map = new HashMap<String,String>();
            DataBase.clear();
            map.put("cid", "1");
            map.put("mid", "1");
            post.execute(map);
            post.execute(map);
            DataBase.deleteCollections();
    }

    private void commonExceptions(Supplier<HashMap<String,String>> body, boolean addMovie) throws SQLException, InvalidCommandException {
        DataBase.clear();
        DataBase.createCollection(addMovie);
        post.execute(body.get());
        DataBase.deleteCollections();
    }
}

