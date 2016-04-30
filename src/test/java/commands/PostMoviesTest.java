package commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Strutures.CommandInfo;
import Strutures.ResultInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;

public class PostMoviesTest {

	@Before
	public void init() throws SQLException {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('x','20000101')");

			stmt.executeUpdate("DELETE Movie");

			stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

			stmt.close();
		}
	}

	@After
	public void removeInserts() throws Exception {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();

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
    	assertEquals(1,ri.getValues().size());
	}

	@After
	public void clean() throws SQLException{
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();
			stmt.execute("delete Rating");
			stmt.execute("delete Movie");
			stmt.close();
		}
	}
}
