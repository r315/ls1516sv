package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandParametersException;
import sqlserver.ConnectionFactory;

public class PostMovies implements ICommand {
	private final String INFO = "POST /movies - creates a new movie, given the parameters \"title\" and \"releaseYear\"";
	private static final String INSERT = "insert into Movie(title,release_year) values(?,?)";
	private static final String TITLE = "Movie Insertion";
	
	@Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
		ResultInfo ri = null;
		try(Connection conn = ConnectionFactory.getConn())
		{			
			String title = data.get("title");
			String date = data.get("releaseYear");

			if(title == null || date == null)
				throw new InvalidCommandParametersException();
			
			PreparedStatement pstmt = conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,title);			
			pstmt.setString(2,date+"0101");        
			int res = pstmt.executeUpdate();

			if(res != 0){				
				ri = createResultInfo(pstmt.getGeneratedKeys());
			}			
			pstmt.close();			
		}		
		return ri;
    }

	@Override
	public String getInfo() {
		return INFO;
	}    
    
    //this could be on ResultInfo
    private ResultInfo createResultInfo(ResultSet rs) throws SQLException{
    	ArrayList<String> columns = new ArrayList<>();
        columns.add("Movie ID");
		ArrayList<ArrayList<String>> rdata=new ArrayList<>();
		 while(rs.next()) {			
			 ArrayList<String> line = new ArrayList<String>();
			 line.add(Integer.toString(rs.getInt(1)));
			 rdata.add(line);						        	
	        }
		 return new ResultInfo(TITLE,columns,rdata);
    }

}
