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
                System.out.println("Error writing into file: " + e.getMessage());
                log.error(e.getMessage());
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
        }
    }

    public static void ServerStop(){
        try{
            if (server != null) server.stop();
        }catch(Exception e){
            log.error("Fail to stop server!");
        }
    }

    public static void ServerSetHandler(ServletHandler handler){
        server.setHandler(handler);
    }

    private static CommandBase commandWithTemplate(CommandBase cb, IResultFormat rf){
        cb.addResultFormat("text/plain",new TextResult());
        cb.addResultFormat("text/html", rf);
        return cb;
    }

    private static CommandBase commandTemplateText(CommandBase cb){
        cb.addResultFormat("text/plain",new TextResult());
        return cb;
    }

    private static CommandBase commandTemplateHtml(CommandBase cb, IResultFormat rf){
        cb.addResultFormat("text/html",rf);
        return cb;
    }

    public static CommandMap createMap() throws Exception{
        CommandMap map=new CommandMap();

        map.add("POST /movies",commandWithTemplate(new PostMovies(),new PostMoviesHtml()));
        map.add("POST /movies/{mid}/ratings",commandWithTemplate(new PostMoviesMidRatings(), new PostMoviesMidRatingsHtml()));
        map.add("POST /movies/{mid}/reviews",commandWithTemplate(new PostMoviesMidReviews(), new PostMoviesMidReviewsHtml()));
        map.add("POST /collections",commandWithTemplate(new PostCollections(), new PostCollectionsHtml()));
        map.add("POST /collections/{cid}/movies/",commandWithTemplate(new PostCollectionsCidMovies(), new PostCollectionsCidMoviesHtml()));

        map.add("GET /",commandTemplateHtml(new Home(), new GetHomeHtml()));
        map.add("GET /movies",commandWithTemplate(new GetMovies(), new GetMoviesHtml()));
        map.add("GET /movies/{mid}",commandWithTemplate(new GetMoviesMid(),new GetMoviesMidHtml()));
        map.add("GET /movies/{mid}/ratings",commandWithTemplate(new GetMoviesMidRatings(),new GetMoviesMidRatingsHtml()));
        map.add("GET /movies/{mid}/reviews",commandWithTemplate(new GetMoviesMidReviews(),new GetMoviesMidReviewsHtml()));
        map.add("GET /movies/{mid}/reviews/{rid}",commandWithTemplate(new GetMoviesMidReviewsRid(),new GetMoviesMidReviewsRidHtml()));
        
        map.add("GET /collections",commandWithTemplate(new GetCollections(), new GetCollectionsHtml()));
        map.add("GET /collections/{cid}",commandWithTemplate(new GetCollectionsCid(), new GetCollectionsCidHtml()));

        map.add("GET /tops/ratings",commandTemplateHtml(new TopsRatings(),new GetTopsRatingsHtml()));
        map.add("GET /tops/ratings/higher/average",commandWithTemplate(new GetTopsRatingsHigherAverage(), new GetGenericHtml()));
        map.add("GET /tops/{n}/ratings/higher/average",commandWithTemplate(new GetTopsNRatingsHigherAverage(), new GetTopsNHtml()));
        map.add("GET /tops/ratings/lower/average",commandWithTemplate(new GetTopsRatingsLowerAverage(), new GetGenericHtml()));
        map.add("GET /tops/{n}/ratings/lower/average",commandWithTemplate(new GetTopsNRatingsLowerAverage(), new GetTopsNHtml()));
        map.add("GET /tops/reviews/higher/count",commandWithTemplate(new GetTopsReviewsHigherCount(), new GetGenericHtml()));
        map.add("GET /tops/{n}/reviews/higher/count",commandWithTemplate(new GetTopsNReviewsHigherCount(), new GetTopsNHtml()));
        map.add("GET /tops/{n}/reviews/lower/count", commandWithTemplate(new GetTopsNReviewsLowerCount(), new GetTopsNHtml()));

        map.add("DELETE /collections/{cid}/movies/{mid}",commandWithTemplate(new DeleteCollectionsCidMoviesMid(), new GetGenericHtml()));

        map.add("OPTION /", commandWithTemplate(new Options(),new GetGenericHtml()));
        map.add("LISTEN /", commandWithTemplate(new Listen(),new GetGenericHtml()));
        map.add("EXIT /",commandWithTemplate(new Exit(),new GetGenericHtml()));
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
