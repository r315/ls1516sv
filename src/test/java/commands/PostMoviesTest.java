package commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import Strutures.CommandInfo;
import Strutures.ResultInfo;

import org.junit.After;
import org.junit.Test;

import sqlserver.ConnectionFactory;

public class PostMoviesTest {
	CommandInfo cmdInf;
	
	@Test
	public void shouldPostMovieOnDataBase() throws Exception{
		HashMap<String,String> data = new HashMap<String,String>();    	
    	data.put("title", "Speed");    	
    	data.put("release_year", "2000");
    	
    	PostMovies pm = new PostMovies();
    	ResultInfo ri = pm.execute(data);
    	
    	assertNotNull(ri);    	
    	assertEquals(1,ri.getValues().size());    	
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
