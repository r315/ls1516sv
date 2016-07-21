package commands;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import exceptions.InvalidCommandException;
import exceptions.PostException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResponseFormat.ResultInfo;
import utils.DataBase;

/**
 * Created by Luigi Sekuiya on 30/04/2016.
 */
public class PostCollectionsTest {
    private PostCollections post;

    @Before
    public void init() throws SQLException {
        post = new PostCollections();
        DataBase.clear();
    }

    @After
    public void finish()throws SQLException{
        DataBase.clear();
    }

    @Test
    public void shouldPostCollection() throws SQLException, InvalidCommandException {
        DataBase.clear();
        ResultInfo rs = post.execute(createParamMap());
        assertEquals(getResultInfo().getValues(), rs.getValues());
    }

    @Test(expected = PostException.class)
    public void shouldGetExceptionOnDuplicatedCollection() throws SQLException, InvalidCommandException {
        HashMap<String, String> param = createParamMap();
        DataBase.clear();
        post.execute(param);
        post.execute(param);
    }

    private ResultInfo getResultInfo() {
        Collection<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String> line1 = new ArrayList<>();
        line1.add("1");
        data.add(line1);
        return new ResultInfo(null, title, data);
    }

    private HashMap<String, String> createParamMap(){
        HashMap<String, String> param = new HashMap<>();
        param.put("name","TheBest");
        param.put("description","The+greatest+movies+of+all+time");
        return param;
    }
}
