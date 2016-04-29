package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandParameters;
import sqlserver.ConnectionFactory;

public class PostMovies implements ICommand {
	private final String INFO = "creates a new movie, given the parameters \"title\" and \"releaseYear\"";
	private static final String INSERT = "insert into Movie(title,release_year) values(?,?)";
	private static final String TITLE = "Movie inserted with ID: ";
	
	@Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
		ResultInfo ri = null;
		try(Connection conn = ConnectionFactory.getConn())
		{			
			String title = data.get("title");
			String date = dateParser(data.get("release_year"));

			if(title == null || date == null)
				throw new InvalidCommandParameters();
			
			PreparedStatement pstmt = conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,title);			
			pstmt.setString(2,date);        
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
    
    private String dateParser(String year){
    	return "01-01-"+year+" 00:00:00";
    }
    
    //this could be on ResultInfo
    private ResultInfo createResultInfo(ResultSet rs) throws SQLException{
    	ResultInfo ri = new ResultInfo();
		ArrayList<ArrayList<String>> rdata=new ArrayList<>();
		 while(rs.next()) {
			 ri.setTitles(Arrays.asList(TITLE));
			 ArrayList<String> line = new ArrayList<String>();
			 line.add(Integer.toString(rs.getInt(1)));
			 rdata.add(line);
			 ri.setValues(rdata);			        	
	        }
		 return ri;
    }

}
