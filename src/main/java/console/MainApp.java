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
				if(interactive_mode){
					System.out.println("[Interactive mode] Insert a command:");
					userArgs= scanner.nextLine().split(" ");
				}
				HeaderInfo headerInfo = new HeaderInfo(userArgs);
				CommandInfo command = new CommandInfo(userArgs);
				ResultInfo result = createMap().get(command).execute(command.getData());
				createHeadersMap().getResponseMethod(headerInfo).display(result);
			} catch(Exception e){
 				if(interactive_mode){
					System.out.println(e.getMessage());
					System.out.println("Please insert a valid command. (For more informations type:OPTION / )");
				}else{
					scanner.close();
					System.out.println(e.getMessage());
					return;
				}
			}
		}while(interactive_mode);
		scanner.close();
	}

	public static CommandMap createMap() throws Exception{
		CommandMap map=new CommandMap();
		map.add("POST /movies",new PostMovies());
		map.add("POST /movies/{mid}/ratings",new PostMoviesMidRatings());
		map.add("POST /movies/{mid}/reviews",new PostMoviesMidReviews());
		map.add("POST /collections",new PostCollections());
		map.add("POST /collections/{cid}/movies/",new PostCollectionsCidMovies());

		map.add("GET /movies",new GetMovies());
		map.add("GET /movies/{mid}",new GetMoviesMid());
		map.add("GET /movies/{mid}/ratings",new GetMoviesMidRatings());
		map.add("GET /movies/{mid}/reviews",new GetMoviesMidReviews());
		map.add("GET /movies/{mid}/reviews/{rid}",new GetMoviesMidReviewsRid());
		map.add("GET /collections",new GetCollections());
		map.add("GET /collections/{cid}",new GetCollectionsCid());

		map.add("GET /tops/ratings/higher/average",new GetTopsRatingsHigherAverage());
		map.add("GET /tops/{n}/ratings/higher/average",new GetTopsNRatingsHigherAverage());
		map.add("GET /tops/ratings/lower/average",new GetTopsRatingsLowerAverage());
		map.add("GET /tops/{n}/ratings/lower/average",new GetTopsNRatingsLowerAverage());
		map.add("GET /tops/reviews/higher/count",new GetTopsReviewsHigherCount());
		map.add("GET /tops/{n}/reviews/higher/count",new GetTopsNReviewsHigherCount());

		map.add("DELETE /collections/{cid}/movies/{mid}",new DeleteCollectionsCidMoviesMid());

		map.add("EXIT /",new Exit());
		map.add("OPTION /",new Options());
		return map;
	}

	public static HeaderMap createHeadersMap(){
		HeaderMap map=new HeaderMap();
		map.addResponseMethod("text/html",new HtmlResult());
		map.addResponseMethod("text/plain",new TextResult());

		return map;
	}
}
