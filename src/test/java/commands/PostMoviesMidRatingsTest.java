package commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Strutures.ResultInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;

/**
 * Created by Red on 09/04/2016.
 */
public class PostMoviesMidRatingsTest {
	// TODO: 30/04/2016
	private static int movieid;

	@Before
	public static void PostMoviesMidRatingsTest0() throws Exception{
		HashMap<String,String> data = new HashMap<String,String>();    	
    	data.put("title", "Speed");    	
    	data.put("release_year", "2000");

    	ResultInfo ri = new PostMoviesMidRatings().execute(data);
    	Iterator<ArrayList<String>> it = ri.getValues().iterator();
    	while(it.hasNext()){
    		List<String> s = (List<String>) it.next();
    		movieid = Integer.parseInt(s.get(0));
    	}
    	
	}
	
   
    @Test
    public void GetMoviesMidConsoleOutputTest()throws Exception{
    	
    	
    	
    }
    
    @After
    public void clean() throws SQLException{
    	try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.execute("delete from Movie where title = 'Speed'");
            stmt.close();
        }
    }

}
