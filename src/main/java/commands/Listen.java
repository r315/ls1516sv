package commands;

import Strutures.Server.ExampleServlet;
import Strutures.ICommand;
import Strutures.Server.favIconServlet;
import console.Manager;
import Strutures.ResultInfo;

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
        AssociateHandlers(handler);
        handler.addServletWithMapping(ExampleServlet.class, "/movies/*");
        handler.addServletWithMapping(favIconServlet.class, "/favicon.ico");
        //Starts listening to requests
        Manager.ServerStart();
        //wait for server to initialize
        try{
            Thread.sleep(10);
        }catch(InterruptedException e){
            System.out.println("Server could not initialize.");
        }
        //System.out.println("Http Server started listening requests on port: "+port);
        
        //// TODO: 19/05/2016
        //Change return to ResultInfo
        return null;
    }

    private static void AssociateHandlers(ServletHandler handler){
        handler.addServletWithMapping(ExampleServlet.class, "/movies/*");
        handler.addServletWithMapping(favIconServlet.class, "/favicon.ico");
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
