package console;

import Strutures.Command.*;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.Plain.TextResult;
import Strutures.ResponseFormat.ResultInfo;
import commands.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Red on 18/05/2016.
 */
public class Manager {
    public static CommandMap commandMap;
    public static HeaderMap headersMap;
    private static Server server;
    private static boolean isActive;

    public static void Init(){
        try {
            commandMap = createMap();
            headersMap = createHeadersMap();
        }catch(Exception e){
            //// TODO: 19/05/2016
        }
    }

    public static IResultFormat executeCommand(CommandInfo commandInfo, HeaderInfo headerInfo) throws Exception{
        IResultFormat resultFormat=null;
            ICommand cmd = commandMap.get(commandInfo);
            ResultInfo result =cmd.execute(commandInfo.getData());
            //ResultInfo result = commandMap.get(commandInfo).execute(commandInfo.getData());
            resultFormat = headersMap.getResponseMethod(headerInfo).apply(result);
        return resultFormat;
    }

    public static void displayResponse(String response, HeaderInfo headerinfo){
        Map<String,String> headers= headerinfo.getHeadersMap();
        String filename= headers.get("file-name");
        if(filename==null){//write to console
            System.out.println(response);
        }else {//write response into a file
            try{
                writeToFile(filename,response);
            }catch(IOException e){
                System.out.println("Error writing into file");
            }
        }
    }

    public static void ServerCreate(int port){
        server=new Server(port);
    }
    
    public static void ServerStart() {
        try{
            server.start();
        }catch(Exception e){
            //// TODO: 19/05/2016
        }finally {
            isActive=true;
        }
    }

    public static void ServerStop(){
        try{
            server.stop();
        }catch(Exception e){
            //// TODO: 25/05/2016
        }finally {
            isActive=false;
        }
    }

    public static void ServerSetHandler(ServletHandler handler){
        server.setHandler(handler);
    }



    public static void ServerJoin(){
        try{
            server.stop();
            server.join();
        }catch(Exception e){
            //// TODO: 19/05/2016
        }finally {
            isActive=false;
        }
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
        map.add("GET /tops/{n}/reviews/lower/count", new GetTopsNReviewsLowerCount());

        map.add("DELETE /collections/{cid}/movies/{mid}",new DeleteCollectionsCidMoviesMid());

        map.add("LISTEN /", new Listen());
        map.add("EXIT /",new Exit());
        map.add("OPTION /",new Options());
        return map;
    }

    public static HeaderMap createHeadersMap(){
        HeaderMap map=new HeaderMap();
        map.addResponseMethod("text/html",  ri-> new HtmlResult(ri));
        map.addResponseMethod("text/plain", ri-> new TextResult(ri));
        return map;
    }

    private static void writeToFile(String filename, String s) throws FileNotFoundException {
        if(filename == null){
            System.out.println(s);
        }else{
            try(  PrintWriter file = new PrintWriter(filename)) {
                file.println(s);
            }
        }
    }
}
