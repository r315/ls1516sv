package console;

import commands.CommandInfo;
import commands.ICommand;
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
		CNode cnode=null;
		try{
			cnode= MapManager.getCNode(cmdinfo);
		}catch (Exception e){
			System.out.println(e.getMessage());
			return;
		}

		ICommand command= cnode.getCommand();
		try {
			command.execute(cmdinfo.getResources(), cmdinfo.getParameters());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}		
}
