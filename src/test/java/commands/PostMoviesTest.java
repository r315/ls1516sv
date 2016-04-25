package commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Strutures.CommandInfo;
import org.junit.Before;
import org.junit.Test;

import sqlserver.ConnectionFactory;

public class PostMoviesTest {
	CommandInfo cmdInf;
	@Before
	public void removeIfExists() throws SQLException{		
			Connection conn = ConnectionFactory.getConn();
			Statement stmt= conn.createStatement();
	        stmt.executeUpdate("delete from Movie where title='filme' and release_year='2016-01-01 00:00:00'");
	        stmt.close();		
	}
	
	@Test
	public void shouldPostMovieOnDataBase() throws Exception{
		PostMovies pm = new PostMovies();
		cmdInf = new CommandInfo("POST","/movies","title=filme&release_year=2016");		
		pm.execute(cmdInf.getResources(),cmdInf.getParameters());		
	}

}
