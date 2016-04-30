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

import static org.junit.Assert.assertEquals;

/**
 * Created by Red on 09/04/2016.
 */
public class PostMoviesMidRatingsTest {

	private static int movieid;

//	POST /movies/{mid}/ratings - submits a new rating for the movie identified by mid, given the following parameters
//
//	rating - integer between 1 and 5.

	@Before
	public void init() throws SQLException {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();

			stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('x','20000101')");

			stmt.executeUpdate("DELETE FROM Movie WHERE title='x'");

			stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");

			stmt.executeUpdate("DBCC CHECKIDENT (Rating, RESEED, 0)");

			stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('awesomeJack','20000101')");

			stmt.executeUpdate("INSERT INTO Rating (movie_id,one,two,three,four,five) VALUES ('1','0','0','0','0','0')");

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

			stmt.executeUpdate("DBCC CHECKIDENT (Rating, RESEED, 0)");

		}
	}

	@Test
	public void PostMoviesMidRatingsTest0() throws Exception{
		HashMap<String,String> data = new HashMap<String,String>();    	
    	data.put("rating", "5");
		data.put("mid", "1");

		ArrayList<ArrayList<String>> resdata = new ArrayList<>();

		ResultInfo result = new ResultInfo(null,new ArrayList<>(1),resdata);

		ArrayList<String> line1 = new ArrayList<>(); line1.add("Success");

		resdata.add(line1);

    	ResultInfo ri = new PostMoviesMidRatings().execute(data);

		assertEquals(ri.getValues(),result.getValues());

	}

}
