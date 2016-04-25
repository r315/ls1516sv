package console;

import Strutures.CommandInfo;
import Strutures.CommandMap;
import Strutures.ICommand;
import logic.MapManager;

public class MainApp {

	public static void main(String [] args){

		CommandInfo cmdinfo = new CommandInfo(args);
		try{
			new CommandMap().get(cmdinfo).execute(cmdinfo.getData());
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}		
}
