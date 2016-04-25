package console;

import Strutures.CommandInfo;
import Strutures.CommandMap;
import Strutures.ICommand;
import logic.MapManager;
import Strutures.CNode;

public class MainApp {

	public static void main(String [] args){
		CommandInfo cmdinfo = null;
	
		switch(args.length){
		case 2:
			cmdinfo = new CommandInfo(args[0],args[1],null);
			break;
		case 3:	
			cmdinfo = new CommandInfo(args[0],args[1],args[2]);
			break;
			default :
				System.out.println("Bad parameters");
				break;
		}

		CommandMap commandMap= CommandMap.createMap();

		ICommand command;
		try{
			command= commandMap.get(cmdinfo);
		}catch (Exception e){
			System.out.println(e.getMessage());
			return;
		}

		try {
			command.execute(cmdinfo.getData());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}		
}
