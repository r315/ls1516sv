package console;

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
	}
		
}
