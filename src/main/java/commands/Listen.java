package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import Strutures.Server.Servlet;
import Strutures.Server.favIconServlet;
import console.Manager;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import org.eclipse.jetty.servlet.ServletHandler;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Red on 18/05/2016.
 */
public class Listen extends CommandBase {
    private static final String INFO= "LISTEN / - Application starts listening to http requests";

    @Override
    public ResultInfo execute(HashMap<String, String> prmts) throws InvalidCommandException, SQLException {
        String aux_port= prmts.get("port");
        if(aux_port==null){
            throw new InvalidCommandParametersException("Invalid port parameter. Server won't start.");
        }

        int port;
        try{
            port = Integer.parseInt(aux_port);
        }catch (NumberFormatException n){
            throw new InvalidCommandParametersException("Invalid port parameter. Server won't start.");
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
        handler.addServletWithMapping(Servlet.class, "/*");
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
