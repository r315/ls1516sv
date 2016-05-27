package commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResponseFormat.ResultInfo;

public class PostMoviesTest {

	@Before
	public void init() throws SQLException {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('x','20000101')");

			stmt.executeUpdate("DELETE Rating");

			stmt.executeUpdate("DELETE Movie");

			stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

			stmt.close();
		}
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
