package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sqlserver.ConnectionFactory;
import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandParametersException;
import exceptions.SqlInsertionException;

public class PostMovies implements ICommand {
	private final String INFO = "POST /movies - creates a new movie, given the parameters \"title\" and \"releaseYear\"";
	private static final String INSERT_MOVIE = "insert into Movie(title,release_year) values(?,?)";
	private static final String INSERT_RATING = "insert into Rating(movie_id,one,two,three,four,five) values(?,0,0,0,0,0)";
	private static final String TITLE = "Movie Insertion";
	int mid;
	
	@Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
		ResultInfo ri = null;	
		if(data == null)
			throw new InvalidCommandParametersException("Data is null");
		
		String title = data.get("title");
		String date = data.get("releaseYear");
		PreparedStatement movieinsert = null;
		PreparedStatement ratinginsert = null;
		Connection conn = null;

		if(title == null || date == null)
			throw new InvalidCommandParametersException();
		
		try{
			conn = ConnectionFactory.getConn();
			conn.setAutoCommit(false);
			movieinsert = conn.prepareStatement(INSERT_MOVIE,PreparedStatement.RETURN_GENERATED_KEYS);
			ratinginsert = conn.prepareStatement(INSERT_RATING);
			
			movieinsert.setString(1,title);			
			movieinsert.setString(2,date+"0101");        
			mid = movieinsert.executeUpdate();		

			if(mid != 0){				
				ri = createResultInfo(movieinsert.getGeneratedKeys());				
				ratinginsert.setInt(1,mid);
				ratinginsert.executeUpdate();	
				conn.commit();
			}else{
				conn.rollback();
			}			
		}catch(SQLException e){
			if (conn != null) {
	            try {	                
	                conn.rollback();
	            } catch(SQLException excep) {
	                throw excep;
	            }
	        }
			// TODO: 01/06/2016 Clean Commands. Use Try with resources. re-throw sql exceptions and interpret them above. 
			if(e.getErrorCode()==2627){
				throw new SqlInsertionException("Information already exists");
			}
		}finally{
			if(movieinsert != null)
				movieinsert.close();
			if(ratinginsert != null)
				ratinginsert.close();
		}
		
		if(ri == null)
			throw new SqlInsertionException("Movie insertion Failed");
		return ri;
    }

	@Override
	public String getInfo() {
		return INFO;
	}
    
    private ResultInfo createResultInfo(ResultSet rs) throws SQLException{
    	ArrayList<String> columns = new ArrayList<>();
    	columns.add("Movie ID");
    	ArrayList<ArrayList<String>> rdata=new ArrayList<>();
    	rs.next();
		ArrayList<String> line = new ArrayList<String>();
		mid = rs.getInt(1);
		line.add(Integer.toString(mid));
		rdata.add(line);

    	return new ResultInfo(TITLE,columns,rdata);
    }
}
