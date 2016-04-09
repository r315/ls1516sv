package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import exceptions.InvalidCommandParameters;
import sqlserver.ConnectionFactory;

public class PostMovies implements ICommand {
	private static final String INSERT = "insert into Movie(title,release_year) values(?,?)";
	
	@Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception {		
		try(Connection conn = ConnectionFactory.getConn())
		{			
			String title = prmts.get("title");
			String date = dateParser(prmts.get("release_year"));					
					
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
