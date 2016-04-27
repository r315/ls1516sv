package logic;

import Strutures.CommandInfo;
import Strutures.CommandMap;
import Strutures.ICommand;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import commands.*;
import Strutures.CNode;

public class CommandMapTest {

	CommandMap map;

	@Before
	public void before() throws Exception{
		map= CommandMap.createMap();
	}

	@Test
	public void OptionsCommandTest() throws Exception{
		CommandInfo command=new CommandInfo(new String[]{"OPTION","/"});
		map.get(command).execute(command.getData());
		//map.g
	}

	@Test
	public void shouldBeAbleToGetCnodePostMovies()throws Exception{
		CommandInfo cmdInfo = new CommandInfo(new String[]{"POST","/movies","title=filme1&release_year=2014"});
		Assert.assertTrue(map.get(cmdInfo) instanceof PostMovies);
	}

//	@Test
//	public void shouldBeAbleToGetCnodePostMovies()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("POST","/movies","title=filme1&release_year=2014");
//		CNode expected = new CNode(cmdInfo.getResources(), new PostMovies());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof PostMovies);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodePostRatings()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("POST","/movies/123/ratings","rating=3");
//		CNode expected = new CNode(cmdInfo.getResources(),new PostMoviesMidRatings());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof PostMoviesMidRatings);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodePostReviews()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("POST","/movies/123/reviews",
//				"reviewerName=Afonso&reviewSummary=reviewhere&review=fullreview&rating=4");
//		CNode expected = new CNode(cmdInfo.getResources(),new PostMoviesMidReviews());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof PostMoviesMidReviews);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetMovies() throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/movies",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetMovies());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetMovies);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetMoviesById()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMid());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetMoviesMid);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetRatingsById()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/ratings",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMidRatings());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetMoviesMidRatings);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetReviewsById()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/reviews",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMidReviews());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetMoviesMidReviews);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetReviesByIdAndReview()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/movies/123/reviews/3",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetMoviesMidReviewsRid());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetMoviesMidReviewsRid);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetTopsHigherAvg()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/tops/ratings/higher/average",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetTopsRatingsHigherAverage());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetTopsRatingsHigherAverage);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetTopsHigherAvgWithCount()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/tops/5/ratings/higher/average",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetTopsNRatingsHigherAverage());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetTopsNRatingsHigherAverage);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetTopsLowerAvg()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/tops/ratings/lower/average",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetTopsRatingsLowerAverage());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetTopsRatingsLowerAverage);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetTopsLowerAvgWithCount()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/tops/5/ratings/lower/average",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetTopsNRatingsLowerAverage());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetTopsNRatingsLowerAverage);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetTopsMostReviews()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/tops/reviews/higher/count",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetTopsReviewsHigherCount());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetTopsReviewsHigherCount);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
//
//	@Test
//	public void shouldBeAbleToGetCnodeGetTopsMostReviewsCount()throws Exception{
//		CommandInfo cmdInfo = new CommandInfo("GET","/tops/5/reviews/higher/count",null);
//		CNode expected = new CNode(cmdInfo.getResources(),new GetTopsNReviewsHigherCount());
//		CNode cnode= MapManager.getCNode(cmdInfo);
//		Assert.assertTrue(cnode.getCommand() instanceof GetTopsNReviewsHigherCount);
//		Assert.assertTrue(cmdInfo.getResources().equals(expected.collection()));
//	}
}
