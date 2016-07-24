package commands;

import Strutures.ResponseFormat.ResultInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sqlserver.ConnectionFactory;
import utils.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PostMoviesTest {

	@Before
	public void init() throws SQLException {
		DataBase.clear();
	}

	@After
	public void removeInserts() throws Exception {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DELETE Rating");
			stmt.executeUpdate("DELETE Movie");
			stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
		}
	}

	@Test
	public void shouldPostMovieOnDataBase() throws Exception{
		HashMap<String,String> data = new HashMap<String,String>();    	
    	data.put("title", "Speed");    	
    	data.put("releaseYear", "2000");
    	ResultInfo ri = new PostMovies().execute(data);
    	assertNotNull(ri);    	
    	assertEquals(1,Integer.parseInt(ri.getValues().iterator().next().iterator().next()));
	}
}
