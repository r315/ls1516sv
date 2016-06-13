package console;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;

/**
 * Created by Luigi Sekuiya on 13/06/2016.
 */
public class HerokuMainApp {

    public static void main(String [] args){

        String port = System.getenv().get("PORT");

        String[] userArgs = {"LISTEN", "/", String.format("port=%s",port)};

        Manager.Init();
        try {
            HeaderInfo headerInfo = new HeaderInfo(userArgs);
            CommandInfo command = new CommandInfo(userArgs);
            String result= Manager.executeCommand(command,headerInfo).generate();
            if(result!=null)Manager.displayResponse(result,headerInfo);

        }catch(Exception e){

            System.out.println(e.getMessage());
            return;
        }

        while (true);
    }
}
