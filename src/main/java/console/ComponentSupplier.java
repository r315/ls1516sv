package console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import utils.CommandDecoder;
import commands.CommandInfo;
import commands.HeaderInfo;
import exceptions.InvalidCommandPathException;

public class ComponentSupplier {
	
	private CommandInfo cmdinfo = null;	
	private HeaderInfo headerInfo = null;	

	public ComponentSupplier(String[] components)throws InvalidCommandPathException{
		
		//cmdinfo = new CommandInfo(components[0],CommandDecoder.parsePath(components[1]),null);
		
		for(String comp : components){			
			Map<String,String> opt = parseOptions(comp,"\\|",":");
			if(opt!=null && headerInfo == null)
				headerInfo = new HeaderInfo(opt);
			
			opt = parseOptions(comp,"&","=");
			if(opt!=null)
				headerInfo = new HeaderInfo(opt);
			
		}		
		
	}

	public CommandInfo getCommadInfo(){ 
		return cmdinfo;
	}	
	
	public HeaderInfo getHeader(){
		return headerInfo;
	}	
	
	private Map<String,String> parseOptions(String opt, String optseparator, String keyseparator){
		if(!opt.contains(keyseparator)) 
			return null;		
		String [] options = opt.split(optseparator);
		Map<String,String> mapoptions = new HashMap<String,String>();
		for(String option : options){
			String [] aux = option.split(keyseparator);
			mapoptions.put(aux[0],aux[1]);
		}
		return mapoptions;
	}
}
