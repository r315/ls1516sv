package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import Strutures.ICommand;
import Strutures.ResultInfo;
import utils.Utils;
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
	private final String INFO = " creates a new review for the movie identified by mid, given the parameters \"reviewerName\", \"reviewSummary\", \"review\" and \"rating\"";

	private static final String INSERT = "insert into Review(movie_id,name,review,summary,rating) values(?,?,?,?,?)";
	private static final int NPARAM = 4;
	
	@Override
	public ResultInfo execute(HashMap<String, String> data) throws Exception{
		try(Connection conn = ConnectionFactory.getConn())
		{
			Collection<String> values = new ArrayList<String>(); 
			values.add(data.get("reviewerName"));
			values.add(data.get("review"));
			values.add(data.get("reviewSummary"));
			values.add(data.get("rating"));
			
			// all parameters are NOTNULL in database
			if(values.size() != NPARAM)
				throw new InvalidCommandParameters();			

			PreparedStatement pstmt = conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);			
			pstmt.setInt(1, Utils.getInt(data.get("mID")));
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

		//Builderino stuff
		ResultInfo stuff = new ResultInfo();
		return stuff;
	}

	@Override
	public String getInfo() {
		return INFO;
	}


	private void printRS(ResultSet rs) throws SQLException {
		while(rs.next()) {
			System.out.println("Review inserted with ID: "+ rs.getInt(1));
		}
	}

}
