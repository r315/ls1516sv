package console;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
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

        String[] userArgs = {"LISTEN", "/", String.format("port=%s",port)};

        Manager.Init();
        try {
            HeaderInfo headerInfo = new HeaderInfo(userArgs);
            CommandInfo command = new CommandInfo(userArgs);
            String result= Manager.executeCommand(command,headerInfo);
            if(result!=null)Manager.displayResponse(result,headerInfo);

        }catch(Exception e){

            System.out.println(e.getMessage());
            return;
        }

        _logger.info("Application Started!!");

        while (true);
    }
}
