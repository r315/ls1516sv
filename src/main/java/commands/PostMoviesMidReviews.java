package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import sqlserver.ConnectionFactory;
import exceptions.InvalidCommandParameters;

/*
POST /movies/{mid}/reviews - creates a new review for the movie identified by mid, given the following parameters
reviewerName - the reviewer name
reviewSummary - the review summary
review - the complete review
rating - the review rating
*/

public class PostMoviesMidReviews implements ICommand {
	private static final String INSERT = "insert into Review(movie_id,name,review,summary,rating) values(?,?,?,?,?)";
	private static final int NPARAM = 4;
	
	@Override
	public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception{
		try(Connection conn = ConnectionFactory.getConn())
		{
			Collection<String> values = new ArrayList<String>(); 
			values.add(prmts.get("reviewerName"));
			values.add(prmts.get("review"));
			values.add(prmts.get("reviewSummary"));
			values.add(prmts.get("rating"));
			
			// all parameters are NOTNULL in database
			if(values.size() != NPARAM)
				throw new InvalidCommandParameters();			

			PreparedStatement pstmt = conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);			
			pstmt.setInt(1, Integer.parseInt((String) args.toArray()[1]));
			pstmt.setString(2,(String) values.toArray()[0]);
			pstmt.setString(3,(String) values.toArray()[1]);
			pstmt.setString(4,(String) values.toArray()[2]);			
			pstmt.setInt(5,Integer.parseInt((String) values.toArray()[3]));
			
			int res = pstmt.executeUpdate();

			if(res != 0){
				ResultSet rs = pstmt.getGeneratedKeys();
				printRS(rs);
			}			
			pstmt.close();

		}
	}
	
	
	private void printRS(ResultSet rs) throws SQLException {
		while(rs.next()) {
			System.out.println("Review inserted with ID: "+ rs.getInt(1));
		}
	}

}
