package console;

import Strutures.*;

public class MainApp {

	public static void main(String [] args){

		CommandInfo command = new CommandInfo(args);
		HeaderInfo headerInfo = new HeaderInfo(args);
		ResultInfo result;
		try{
			result= CommandMap.createMap().get(command).execute(command.getData());
			HeaderMap.createMap().getResponseMethod(headerInfo).display(result);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}		
}
