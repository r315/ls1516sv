package console;

import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Luigi Sekuiya on 13/06/2016.
 */
public class HerokuMainApp {

    private static final Logger _logger = LoggerFactory.getLogger(HerokuMainApp.class);

    public static void main(String [] args){
        System.setProperty("org.slf4j.simpleLogger.levelInBrackets","true");
        String port = System.getenv().get("PORT");
        try{
            Manager.ServerCreate(port);
        }catch(NumberFormatException e){
            _logger.error(String.format("Ending Application. - %s",e.getMessage()));
            return;
        }

        //Create a handler for each functionality
        Manager.ServerSetHandler(new ServletHandler());

        //Starts listening to requests
        Manager.ServerStart();
        Manager.ServerJoin();
        _logger.info("Application Started!");
    }
}
