package logic;

import java.util.Arrays;
import java.util.Iterator;

import commands.GetMovies;
import org.junit.Test;

import commands.*;
import logic.MapManager;
import Strutures.CNode;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class CommandMapTest {
	
	@Test
	public void shouldBeAbleToGetCnodePostMovies(){
		CommandInfo cmdInfo = new CommandInfo("POST","/movies","title=filme1&release_year=2014");
		CNode expected = new CNode(cmdInfo.getResources(), new PostMovies());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodePostRatings(){
		CommandInfo cmdInfo = new CommandInfo("POST","/movies/123/ratings","3");
		CNode expected = new CNode(cmdInfo.getResources(),new PostMovies());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodePostReviews(){
		CommandInfo cmdInfo = new CommandInfo("POST","/movies/123/reviews",
				"reviewerName=Afonso&reviewSummary=reviewhere&review=fullreview&rating=4");
		CNode expected = new CNode(cmdInfo.getResources(),new PostMovies());
		cnodeCmp(cmdInfo,expected);		
	}	
	
	@Test
	public void shouldBeAbleToGetCnodeGetMovies(){
		CommandInfo cmdInfo = new CommandInfo("GET","/movies",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetMovies());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetMoviesById(){
		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMid());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetRatingsById(){
		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/ratings",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMidRatings());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetReviewsById(){
		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/reviews",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMidReviews());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetReviesByIdAndReview(){
		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/reviews/3",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMidReviewsRid());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetTopsHigherAvg(){
		CommandInfo cmdInfo = new CommandInfo("GET","/tops/ratings/higher/average",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetTops());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetTopsHigherAvgWithCount(){
		CommandInfo cmdInfo = new CommandInfo("GET","/tops/5/ratings/higher/average",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetTops());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetTopsLowerAvg(){
		CommandInfo cmdInfo = new CommandInfo("GET","/tops/ratings/lower/average",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetTops());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetTopsLowerAvgWithCount(){
		CommandInfo cmdInfo = new CommandInfo("GET","/tops/5/ratings/lower/average",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetTops());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetTopsMostReviews(){
		CommandInfo cmdInfo = new CommandInfo("GET","/tops/reviews/higher/count",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetTops());
		cnodeCmp(cmdInfo,expected);		
	}
	
	@Test
	public void shouldBeAbleToGetCnodeGetTopsMostReviewsCount(){
		CommandInfo cmdInfo = new CommandInfo("GET","/tops/5/reviews/higher/count",null);
		CNode expected = new CNode(cmdInfo.getResources(),new GetTops());
		cnodeCmp(cmdInfo,expected);		
	}	
	
	private void cnodeCmp(CommandInfo cmdInfo, CNode expected){
		CNode actual = null;
		try {
			actual = MapManager.getCNode(cmdInfo);
		} catch (Exception e) {
			fail();
		}
		
		Iterator<String> it1 = expected.iterator();
		Iterator<String> it2 = actual.iterator();
		
		if(expected.getCollectionSize() != actual.getCollectionSize())
			fail();
		
		if(!actual.getCommand().getClass().isInstance(expected.getCommand()))
			fail();
		
		while(it1.hasNext())
			if(! it1.next().equals(it2.next()))
				fail();	
	}

}
