package console;

import Strutures.Command.CommandBase;
import Strutures.Command.CommandInfo;
import Strutures.Command.CommandMap;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import Strutures.Server.Servlet;
import Strutures.Server.favIconServlet;
import commands.*;
import exceptions.InvalidCommandException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import templates.*;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Red on 18/05/2016.
 */
public class Manager {
    private static final Logger _logger = LoggerFactory.getLogger(Manager.class);
    public static CommandMap commandMap;
    private static Server server;

    public static void Init() throws InvalidCommandException{
        commandMap = createMap();
    }

    public static String executeCommand(CommandInfo commandInfo, HeaderInfo headerInfo)
            throws SQLException, InvalidCommandException {
        CommandBase cmdbase = commandMap.get(commandInfo);
        ResultInfo result = cmdbase.execute(commandInfo.getData());
        return cmdbase.getResult(commandInfo,headerInfo,result);
    }


    //// TODO: 19/07/2016 Fazer isto de novo or test
    public static void displayResponse(String response, HeaderInfo headerinfo){
        if(headerinfo.hasKey(HeaderInfo.FILENAME_TOKEN)){
            try{
                Utils.writeToFile(headerinfo.getHeaderValue(HeaderInfo.FILENAME_TOKEN),response);
            }catch(IOException e){
                _logger.error(String.format("Error writing into file: %s",e.getMessage()));
            }
        }else{
            System.out.println(response);
        }
    }

    public static void ServerCreate(String port) throws NumberFormatException{
        server=new Server(Integer.parseInt(port));
    }

    public static void ServerJoin(){
        try{
            server.join();
        }catch (InterruptedException e){
            _logger.error(String.format("Failed to block Thread - %s",e.getMessage()));
        }
    }

    public static void ServerStart() {
        try{
            server.start();
        }catch(Exception e){
            _logger.error(String.format("Failed to start server - %s",e.getMessage()));
        }
    }

    public static void ServerStop(){
        try{
            if (server != null) server.stop();
        }catch(Exception e){
            _logger.error(String.format("Failed to stop server - %s",e.getMessage()));
        }
    }

    public static void ServerSetHandler(ServletHandler handler){
        server.setHandler(handler);
        AssociateHandlers(handler);
    }

    private static void AssociateHandlers(ServletHandler handler){
        handler.addServletWithMapping(favIconServlet.class, "/favicon.ico");
        handler.addServletWithMapping(Servlet.class, "/*");
    }

    public static CommandMap createMap() throws InvalidCommandException{
        CommandMap map=new CommandMap();

        map.add("POST /movies",commandWithTemplate(new PostMovies(),new PostMoviesHtml()));
        map.add("POST /movies/{mid}/ratings",commandWithTemplate(new PostMoviesMidRatings(), new PostMoviesMidRatingsHtml()));
        map.add("POST /movies/{mid}/reviews",commandWithTemplate(new PostMoviesMidReviews(), new PostMoviesMidReviewsHtml()));
        map.add("POST /collections",commandWithTemplate(new PostCollections(), new PostCollectionsHtml()));
        map.add("POST /collections/{cid}/movies/",commandWithTemplate(new PostCollectionsCidMovies(), new PostCollectionsCidMoviesHtml()));

        map.add("GET /",commandWithOnlyHtmlTemplate(new Home(), new GetHomeHtml()));
        map.add("GET /movies",commandWithTemplate(new GetMovies(), new GetMoviesHtml()));
        map.add("GET /movies/{mid}",commandWithTemplate(new GetMoviesMid(),new GetMoviesMidHtml()));
        map.add("GET /movies/{mid}/ratings",commandWithTemplate(new GetMoviesMidRatings(),new GetMoviesMidRatingsHtml()));
        map.add("GET /movies/{mid}/reviews",commandWithTemplate(new GetMoviesMidReviews(),new GetMoviesMidReviewsHtml()));
        map.add("GET /movies/{mid}/reviews/{rid}",commandWithTemplate(new GetMoviesMidReviewsRid(),new GetMoviesMidReviewsRidHtml()));
        
        map.add("GET /collections",commandWithTemplate(new GetCollections(), new GetCollectionsHtml()));
        map.add("GET /collections/{cid}",commandWithTemplate(new GetCollectionsCid(), new GetCollectionsCidHtml()));

        map.add("GET /tops/ratings",commandWithOnlyHtmlTemplate(new TopsRatings(),new GetTopsRatingsHtml()));
        map.add("GET /tops/ratings/higher/average",commandWithGenericTemplates(new GetTopsRatingsHigherAverage()));
        map.add("GET /tops/{n}/ratings/higher/average",commandWithTemplate(new GetTopsNRatingsHigherAverage(), new GetTopsNHtml()));
        map.add("GET /tops/ratings/lower/average",commandWithGenericTemplates(new GetTopsRatingsLowerAverage()));
        map.add("GET /tops/{n}/ratings/lower/average",commandWithTemplate(new GetTopsNRatingsLowerAverage(), new GetTopsNHtml()));
        map.add("GET /tops/reviews/higher/count",commandWithGenericTemplates(new GetTopsReviewsHigherCount()));
        map.add("GET /tops/{n}/reviews/higher/count",commandWithTemplate(new GetTopsNReviewsHigherCount(), new GetTopsNHtml()));
        map.add("GET /tops/{n}/reviews/lower/count", commandWithTemplate(new GetTopsNReviewsLowerCount(), new GetTopsNHtml()));

        map.add("DELETE /collections/{cid}/movies/{mid}",commandWithGenericTemplates(new DeleteCollectionsCidMoviesMid()));

        map.add("OPTION /", commandWithGenericTemplates(new Options()));
        map.add("LISTEN /", commandWithOnlyTextTemplate(new Listen()));
        map.add("EXIT /",new Exit());
        return map;
    }

    private static CommandBase commandWithTemplate(CommandBase cb, IResultFormat rf){
        cb.addResultFormat("text/plain",TextResult.getInstance());
        cb.addResultFormat("text/html", rf);
        return cb;
    }

    private static CommandBase commandWithGenericTemplates(CommandBase cb){
        cb.addResultFormat("text/plain",TextResult.getInstance());
        cb.addResultFormat("text/html", GetGenericHtml.getInstance());
        return cb;
    }

    private static CommandBase commandWithOnlyTextTemplate(CommandBase cb){
        cb.addResultFormat("text/plain",TextResult.getInstance());
        return cb;
    }

    private static CommandBase commandWithOnlyHtmlTemplate(CommandBase cb, IResultFormat rf){
        cb.addResultFormat("text/html",rf);
        return cb;
    }
}
