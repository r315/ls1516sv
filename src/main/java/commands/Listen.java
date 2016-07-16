package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import Strutures.Server.*;
import console.Manager;
import exceptions.InvalidCommandException;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Red on 18/05/2016.
 */
public class Listen extends CommandBase {
    private static final String INFO= "Listen - Application starts listening to http requests";
    private static final Logger _logger = LoggerFactory.getLogger(Listen.class);

    @Override
    public ResultInfo execute(HashMap<String, String> prmts) throws InvalidCommandException, SQLException {
        int port;
        try{
            port = Utils.getInt(prmts.get("port"));
        }catch(NullPointerException e){
            _logger.error("Missing port parameter. Server won't start.");
            return new ResultInfo(false);
        }catch (NumberFormatException n){
            _logger.error("Invalid port parameter. Server won't start.");
            return new ResultInfo(false);
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
        handler.addServletWithMapping(favIconServlet.class, "/favicon.ico");
        handler.addServletWithMapping(HomeServlet.class, "");
        handler.addServletWithMapping(Servlet.class, "/*");
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
