package commands;

import static org.junit.Assert.*;

import org.junit.Test;

public class PostMoviesTest {
	CommandInfo cmdInf;
	
	@Test
	public void shoudPostMovieOnDataBase(){
		PostMovies pm = new PostMovies();
		cmdInf = new CommandInfo("POST","/movies","title=filme&release_year=2016");		
		try {
			pm.execute(cmdInf.getResources(),cmdInf.getParameters());
		} catch (Exception e) {
			fail();
		}
		
	}

}
