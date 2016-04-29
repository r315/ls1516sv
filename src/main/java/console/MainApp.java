package console;

import Strutures.*;
import commands.*;

import java.util.Scanner;

public class MainApp {

	public static void main(String [] args){
		String[] userArgs=args;
		boolean interactive_mode=false;
		Scanner scanner = new Scanner(System.in);
		if(args.length==0)
			interactive_mode=true;
		do {
			try {
				if(interactive_mode)
					userArgs= scanner.nextLine().split(" ");
				HeaderInfo headerInfo = new HeaderInfo(userArgs);
				CommandInfo command = new CommandInfo(userArgs);
				ResultInfo result = createMap().get(command).execute(command.getData());
				HeaderMap.createMap().getResponseMethod(headerInfo).display(result);
			} catch(Exception e){
				scanner.close();
				System.out.println(e.getMessage());
				return;
			}
		}while(interactive_mode);
		scanner.close();
	}

	public static CommandMap createMap() throws Exception{
		CommandMap map=new CommandMap();
		map.add("POST /movies",new PostMovies());
		map.add("POST /movies/{mid}/ratings",new PostMoviesMidRatings());
		map.add("POST /movies/{mid}/reviews",new PostMoviesMidReviews());

		map.add("GET /movies",new GetMovies());
		map.add("GET /movies/{mid}",new GetMoviesMid());
		map.add("GET /movies/{mid}/ratings",new GetMoviesMidRatings());
		map.add("GET /movies/{mid}/reviews",new GetMoviesMidReviews());
		map.add("GET /movies/{mid}/reviews/{rid}",new GetMoviesMidReviewsRid());

		map.add("GET /tops/ratings/higher/average",new GetTopsRatingsHigherAverage());
		map.add("GET /tops/{n}/ratings/higher/average",new GetTopsNRatingsHigherAverage());
		map.add("GET /tops/ratings/lower/average",new GetTopsRatingsLowerAverage());
		map.add("GET /tops/{n}/ratings/lower/average",new GetTopsNRatingsLowerAverage());
		map.add("GET /tops/reviews/higher/count",new GetTopsReviewsHigherCount());
		map.add("GET /tops/{n}/reviews/higher/count",new GetTopsNReviewsHigherCount());

		map.add("EXIT /",new Exit());
		map.add("OPTION /",new Options());
		return map;
	}
}
