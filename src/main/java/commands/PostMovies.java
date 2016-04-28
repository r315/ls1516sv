package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandParameters;
import sqlserver.ConnectionFactory;

public class PostMovies implements ICommand {
	public static final String INFO = "POST /movies - creates a new movie, given the parameters \"title\" and \"releaseYear\"";
	private static final String INSERT = "insert into Movie(title,release_year) values(?,?)";

	// TODO: Rollback
	
	@Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
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
        	System.out.println("Movie inserted with ID: "+ rs.getInt(1));
        }
    }
    
    private String dateParser(String year){
    	return "01-01-"+year+" 00:00:00";
    }

}
