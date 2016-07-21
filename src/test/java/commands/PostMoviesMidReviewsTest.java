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

import exceptions.InvalidCommandException;
import exceptions.PostException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandParametersException;
import utils.DataBase;

public class PostMoviesMidReviewsTest {
	private static int mid;
    private static PostMoviesMidReviews post;

    //POST /movies/3/reviews reviewerName=Toni&reviewSummary=filmesobrevidadetoni&review=todaahistoria&rating=2
	@BeforeClass
	public static void init() throws SQLException {
        post = new PostMoviesMidReviews();
		try (Connection conn = ConnectionFactory.getConn()) {
			DataBase.clear();
			PreparedStatement pstmt = conn.prepareStatement("insert into Movie(title,release_year) values('teste',2016)",Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			rs.next(); mid = rs.getInt(1);			
			pstmt.close();
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

    @After
	public void removeInserts() throws SQLException {
		try (Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");
			stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
		}
	}

	@Test
	public void shouldPostReview() throws SQLException, InvalidCommandException {
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

	@Test(expected = PostException.class)
	public void shoudGetPostExceptionForInvalidId() throws SQLException, InvalidCommandException{
		HashMap<String,String> data = new HashMap<String,String>();    	
		data.put("reviewerName", "Afonso");    	
		data.put("reviewSummary", "sumario");
		data.put("review", "sumario_completo");
		data.put("rating", "3");
        data.put("mid", "5");
		post.execute(data);
	}
}
