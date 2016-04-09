package commands;

import static org.junit.Assert.fail;

import org.junit.Test;

public class PostMoviesMidReviewsTest {
	
	
	@Test
	public void shoudPostMovieOnDataBase(){
		ICommand pm = new PostMoviesMidReviews();
		CommandInfo cmdInf = new CommandInfo("POST","/movies/2/reviews",
				"reviewerName=Toni&reviewSummary=filmesobrevidadetoni&review=todaahistoria&rating=2");		
		try {
			pm.execute(cmdInf.getResources(),cmdInf.getParameters());
		} catch (Exception e) {			
			fail();
		}		
	}

}
