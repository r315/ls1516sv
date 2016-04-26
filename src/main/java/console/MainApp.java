package console;

import Strutures.*;

public class MainApp {

	public static void main(String [] args){

		CommandInfo command = null;
		try {
			command = new CommandInfo(args);
		} catch (Exception e1) {
			System.out.println("Invalid Arguments");
			//exit APP?
		}
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
