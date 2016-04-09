package commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import sqlserver.ConnectionFactory;

public class PostMovies implements ICommand {
	private static final String INSERT = "insert into Movies(title,release_year) values(?,?)";
	
	@Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
		
		try(Connection conn = ConnectionFactory.getConn())
		{
			PreparedStatement pstmt = conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,prmts.get("title"));	
			String date = LocalDate.parse(prmts.get("release_year")).toString();
			pstmt.setString(2,date);        
			int res = pstmt.executeUpdate();

			if(res != 0){
				ResultSet rs = pstmt.getGeneratedKeys();
				printRS(rs);
			}			
			pstmt.close();
			
		}catch(SQLException e)
		{
			throw e;
		}	
    }   

    private void printRS(ResultSet rs) throws SQLException {
        while(rs.next()) {
        	System.out.println("Movie inserted with ID: "+ rs.getInt(1));
        }
    }

}
