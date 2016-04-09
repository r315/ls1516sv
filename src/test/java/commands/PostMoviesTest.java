package commands;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;
import exceptions.InvalidCommandParameters;

public class PostMoviesTest {
	CommandInfo cmdInf;
	@Before
	public void removeIfExists(){
		try
		{	
			Connection conn = ConnectionFactory.getConn();
			Statement stmt= conn.createStatement();
	        stmt.executeUpdate("delete from Movie where title='filme' and release_year='2016-01-01 00:00:00'");
	        stmt.close();	        				
		}catch(Exception ex){
			
		}
		
	}
	@Test
	public void shouldPostMovieOnDataBase(){
		PostMovies pm = new PostMovies();
		cmdInf = new CommandInfo("POST","/movies","title=filme&release_year=2016");		
		try {
			pm.execute(cmdInf.getResources(),cmdInf.getParameters());
		} catch (Exception e) {
			fail();
		}
		
	}

}
