package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import Strutures.Server.*;
import console.Manager;
import exceptions.InvalidCommandException;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Red on 18/05/2016.
 */
public class Listen implements ICommand{
    private static final String INFO= "Listen - Application starts listening to http requests";
    private static final Logger _logger = LoggerFactory.getLogger(Listen.class);

    @Override
    public ResultInfo execute(HashMap<String, String> prmts) throws InvalidCommandException, SQLException {
        int port;
        try{
            String aux_port= prmts.get("port");
            if(aux_port==null){
                _logger.error("Invalid port parameter. Server won't start.");
                throw new InvalidParameterException();
            }

            port = Integer.parseInt(aux_port);
        }catch (NumberFormatException n){
            _logger.error("Invalid port parameter. Server won't start.");
            throw new InvalidParameterException();
        }
        Manager.ServerCreate(port);

        //Create a handler for each functionality
        ServletHandler handler = new ServletHandler();
        Manager.ServerSetHandler(handler);
        AssociateHandlers(handler);

        //Starts listening to requests
        Manager.ServerStart();

        return new ResultInfo(false);
    }

    private static void AssociateHandlers(ServletHandler handler){
        handler.addServletWithMapping(TopsRatingsServlet.class, "/tops/ratings");
        handler.addServletWithMapping(MoviesServlet.class, "/movies");
        handler.addServletWithMapping(favIconServlet.class, "/favicon.ico");
        handler.addServletWithMapping(CollectionsServlet.class, "/collections");
        handler.addServletWithMapping(HomeServlet.class, "");
        handler.addServletWithMapping(TopsRatingsReviewsServlet.class, "/tops/*");
        handler.addServletWithMapping(CollectionsCidServlet.class, "/collections/*");
        handler.addServletWithMapping(GenericMoviesMidServlet.class, "/movies/*");
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
