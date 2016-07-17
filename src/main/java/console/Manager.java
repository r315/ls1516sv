package console;

import Strutures.Command.*;
import Strutures.ResponseFormat.IResultFormat;
import templates.*;
import Strutures.ResponseFormat.ResultInfo;
import commands.*;
import exceptions.InvalidCommandException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Red on 18/05/2016.
 */
public class Manager {
    private static final Logger log = LoggerFactory.getLogger(Manager.class);
    public static CommandMap commandMap;
    private static Server server;
    private static boolean isActive;

    public static void Init(){
        try {
            commandMap = createMap();
        }catch(Exception e){
           log.error("Fail to initialize Manager");
        }
    }

    public static String executeCommand(CommandInfo commandInfo, HeaderInfo headerInfo)
            throws SQLException, InvalidCommandException {
        CommandBase cmdbase = commandMap.get(commandInfo);
        ResultInfo result = cmdbase.execute(commandInfo.getData());
        return cmdbase.getResult(commandInfo,headerInfo,result);
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
            log.error("Fail to start server!");
        }finally {
            isActive=true;
        }
    }

    public static void ServerStop(){
        try{
            server.stop();
        }catch(Exception e){
            log.error("Fail to stop server!");
        }finally {
            isActive=false;
        }
    }

    public static void ServerSetHandler(ServletHandler handler){
        server.setHandler(handler);
    }

    private static CommandBase commandWithTemplate(CommandBase cb, IResultFormat rf ){
        cb.addResultFormat("text/plain",new TextResult());
        cb.addResultFormat("text/html", rf);
        return cb;
    }

    public static CommandMap createMap() throws Exception{
        CommandMap map=new CommandMap();

        map.add("POST /movies",new PostMovies());
        map.add("POST /movies/{mid}/ratings",new PostMoviesMidRatings());
        map.add("POST /movies/{mid}/reviews",new PostMoviesMidReviews());
        map.add("POST /collections",new PostCollections());
        map.add("POST /collections/{cid}/movies/",new PostCollectionsCidMovies());

        map.add("GET /",commandWithTemplate(new Home(), new HomeHtml()));
        map.add("GET /movies",commandWithTemplate(new GetMovies(), new GetMoviesHtml()));
        map.add("GET /movies/{mid}",new GetMoviesMid());
        map.add("GET /movies/{mid}/ratings",new GetMoviesMidRatings());
        map.add("GET /movies/{mid}/reviews",new GetMoviesMidReviews());
        map.add("GET /movies/{mid}/reviews/{rid}",new GetMoviesMidReviewsRid());
        
        map.add("GET /collections",commandWithTemplate(new GetCollections(), new CollectionsHtml()));
        map.add("GET /collections/{cid}",commandWithTemplate(new GetCollectionsCid(), new CollectionsCidHtml()));

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
