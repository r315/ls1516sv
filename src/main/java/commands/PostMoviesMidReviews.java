package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import sqlserver.ConnectionFactory;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
POST /movies/{mid}/reviews - creates a new review for the movie identified by mid, given the following parameters
reviewerName - the reviewer name
reviewSummary - the review summary
review - the complete review
rating - the review rating
*/

public class PostMoviesMidReviews extends CommandBase {
	private static final String TITLE = "Review insertion";
	private static final String INFO = "POST /movies/{mid}/reviews - creates a new review for the movie identified by mid, given the parameters \"reviewerName\", \"reviewSummary\", \"review\" and \"rating\"";
	private static final String INSERT = "insert into Review(movie_id,name,review,summary,rating) values(?,?,?,?,?)";
	private static final int NPARAM = 4;

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException{
		ResultInfo ri;
		try(
				Connection conn = ConnectionFactory.getConn();
				PreparedStatement pstmt = conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS)
		){
			List<String> values = new ArrayList<String>();
			values.add(data.get("reviewerName"));
			values.add(data.get("review"));
			values.add(data.get("reviewSummary"));
			values.add(data.get("rating"));
			
			// all parameters are NOTNULL in database
			if(values.size() != NPARAM)
				throw new InvalidCommandParametersException();

			pstmt.setInt(1, Integer.parseInt(data.get("mid")));
			pstmt.setString(2, values.get(0));
			pstmt.setString(3,values.get(1));
			pstmt.setString(4,values.get(2));
			pstmt.setInt(5,Integer.parseInt(values.get(3)));
			int res = pstmt.executeUpdate();
			ri = createResultInfo( pstmt.getGeneratedKeys());
		}catch (SQLException e){
			int errorCode= e.getErrorCode();
			if(errorCode == PostException.STRING_IS_TOO_LONG)
				throw new PostException(errorCode,"At least one field has too many characters!");
			else throw e;
		}
		return ri;
	}

	@Override
	public String getInfo() {
		return INFO;
	}
	
    private ResultInfo createResultInfo(ResultSet rs) throws SQLException{
    	ArrayList<String> columns = new ArrayList<>();
    	columns.add("Review ID");
    	ArrayList<ArrayList<String>> rdata = new ArrayList<>();		
    	while(rs.next()) {
    		ArrayList<String> line = new ArrayList<String>();
    		line.add(Integer.toString(rs.getInt(1)));
    		rdata.add(line);		        	
    	}
    	return new ResultInfo(TITLE,columns,rdata);
    }

}
