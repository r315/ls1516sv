package commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResultInfo;
import exceptions.InvalidCommandParametersException;

public class PostMoviesMidReviewsTest {
	private static int mid;
	//POST /movies/3/reviews reviewerName=Toni&reviewSummary=filmesobrevidadetoni&review=todaahistoria&rating=2
	@BeforeClass
	public static void init() throws SQLException {
		try (Connection conn = ConnectionFactory.getConn()) {
			cleanDatabase();
			PreparedStatement pstmt = conn.prepareStatement("insert into Movie(title,release_year) values('teste',2016)",Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next(); mid = rs.getInt(1);			
			pstmt.close();
		}
	}

	@After
	public void removeInserts() throws Exception {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");
			stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
		}
	}

	@Test
	public void shouldPostReview() throws Exception{
		HashMap<String,String> data = new HashMap<String,String>();    	
		data.put("reviewerName", "Afonso");    	
		data.put("reviewSummary", "sumario");
		data.put("review", "sumario_completo");
		data.put("rating", "3");
		data.put("mid", Integer.toString(mid));	

		PostMoviesMidReviews pm = new PostMoviesMidReviews();		
		ResultInfo ri = pm.execute(data);	
		assertNotNull(ri);
		assertEquals(1,ri.getValues().size());		
	} 		

	@Test
	public void shoudGetParamException(){
		HashMap<String,String> data = new HashMap<String,String>();    	
		data.put("Name", "Afonso");    	//reviewerName
		data.put("reviewSummary", "sumario");
		data.put("review", "sumario_completo");
		data.put("rating", "3");		

		PostMoviesMidReviews pm = new PostMoviesMidReviews();		

		try {
			pm.execute(data);
			fail();
		} catch (InvalidCommandParametersException e) {

		} catch (Exception e) {

		}		
	}

	@Test
	public void shoudGetNullPointerException() throws Exception{
		HashMap<String,String> data = new HashMap<String,String>();    	
		data.put("reviewerName", "Afonso");    	
		data.put("reviewSummary", "sumario");
		data.put("review", "sumario_completo");
		data.put("rating", "3");	
		//data.put("mid", Integer.toString(mid));	//fails convertion

		PostMoviesMidReviews pm = new PostMoviesMidReviews();		

		try {
			pm.execute(data);
			fail();			
		} catch (Exception e) {

		}		
	}	

	@AfterClass
	public static void clean() throws SQLException{
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();
			stmt.execute("delete from Review where name = 'Afonso'");
			stmt.execute("delete from Movie where title = 'teste'");
			stmt.close();
		}
	}    
	private static void cleanDatabase() throws SQLException{
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();
			stmt.execute("delete from Rating");
			stmt.execute("delete from Review");
			stmt.execute("delete from Movie");
			stmt.close();
		}
	}

}
