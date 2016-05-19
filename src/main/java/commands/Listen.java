package commands;

import Strutures.ExampleServlet;
import Strutures.ICommand;
import Strutures.Manager;
import Strutures.ResultInfo;

import org.eclipse.jetty.server.Server;
import pt.isel.ls.apps.http.TimeServlet;
import org.eclipse.jetty.servlet.ServletHandler;
import utils.Utils;


import java.util.HashMap;

/**
 * Created by Red on 18/05/2016.
 */
public class Listen implements ICommand{
    private static final String INFO= "Listen - Application starts listening to http requests";

    @Override
    public ResultInfo execute(HashMap<String, String> prmts) throws Exception {
        int port = Utils.getInt(prmts.get("port"));
        Manager.ServerCreate(port);

        //Create a handler for each functionality
        ServletHandler handler = new ServletHandler();
        Manager.ServerSetHandler(handler);
        handler.addServletWithMapping(ExampleServlet.class, "/*");

        //Starts listening to requests
        Manager.ServerStart();
        System.out.println("Http Server started listening requests on port: "+port);
        
        //// TODO: 19/05/2016
        //Change return to ResultInfo
        return null;
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
