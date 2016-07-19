package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
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
        String port= prmts.get("port");
        if(port==null)
            throw new InvalidCommandParametersException("Invalid port parameter. Server won't start.");

        try{
            Manager.ServerCreate(port);
        }catch(NumberFormatException e){
            throw new InvalidCommandParametersException("Invalid port parameter. Server won't start.");
        }

        //Create a handler for each functionality
        Manager.ServerSetHandler(new ServletHandler());

        //Starts listening to requests
        Manager.ServerStart();
        return new ResultInfo(false);
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
