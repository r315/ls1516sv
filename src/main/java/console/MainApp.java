package console;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import exceptions.InvalidCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {

	private static final Logger _logger = LoggerFactory.getLogger(MainApp.class);
	private static boolean interactive_mode=false;

	public static void main(String [] args){
		String[] userArgs = args.clone();
		Scanner scanner = new Scanner(System.in);
		if(args.length==0)
			interactive_mode=true;

		try{
			Manager.Init();
		}catch(InvalidCommandException e){
			_logger.error("Failed to create CommandMap. Stoping execution.");
		}

		do {
			try {
				if(interactive_mode){
					System.out.println("[Interactive mode] Insert a command:");
					userArgs= scanner.nextLine().split(" ");
				}
				HeaderInfo headerInfo = new HeaderInfo(userArgs);
				CommandInfo command = new CommandInfo(userArgs);
				String result= Manager.executeCommand(command,headerInfo);
                Manager.displayResponse(result,headerInfo);

				}catch(SQLException | InvalidCommandException e){
					if(interactive_mode){
						_logger.error(e.getMessage());
						System.out.println("Please insert a valid command. (For more informations type:OPTION / )");
					}else{
						_logger.error(e.getMessage());
						interactive_mode=false;
					}
				}
		}while(interactive_mode);
		scanner.close();
	}

	public static void Exit(){
		interactive_mode=false;
	}
}
