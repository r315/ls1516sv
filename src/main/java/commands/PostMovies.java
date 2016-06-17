package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PostMovies implements ICommand {
	private final String INFO = "POST /movies - creates a new movie, given the parameters \"title\" and \"releaseYear\"";
	private static final String INSERT_MOVIE = "insert into Movie(title,release_year) values(?,?)";
	private static final String INSERT_RATING = "insert into Rating(movie_id,one,two,three,four,five) values(?,0,0,0,0,0)";
	private static final String TITLE = "Movie Insertion";
	int mid;
	
	@Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
		ResultInfo ri = null;
		if (data == null)
			throw new InvalidCommandParametersException();

		String title = data.get("title");
		String date = data.get("releaseYear");

		if (title == null || date == null)
			throw new InvalidCommandParametersException();

		try (Connection conn = ConnectionFactory.getConn();
			 PreparedStatement movieInsert = conn.prepareStatement(INSERT_MOVIE, PreparedStatement.RETURN_GENERATED_KEYS);
			 PreparedStatement ratingInsert = conn.prepareStatement(INSERT_RATING)
		){

			conn.setAutoCommit(false);
			movieInsert.setString(1, title);
			movieInsert.setString(2, date + "0101");
			mid = movieInsert.executeUpdate();

			if (mid != 0) {
				ri = createResultInfo(movieInsert.getGeneratedKeys());
				ratingInsert.setInt(1, mid);
				ratingInsert.executeUpdate();
				conn.commit();
			}else{
				conn.rollback();
				throw new SQLException("Invalid mID");
			}

		}catch(SQLException e){
			int errorCode= e.getErrorCode();
			if(errorCode == PostException.ENTRY_EXISTS)
				throw new PostException(errorCode,"Entry already exists!");
		}

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
