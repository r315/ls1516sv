package utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import exceptions.InvalidCommandPathException;

public class CommandDecoder {	

	
	
	public static Collection<String> parsePath(String path) throws InvalidCommandPathException{
		if(path.charAt(0) != '/'){
            throw new InvalidCommandPathException();
        }
        return  Arrays.asList(path.substring(1).split("/"));
	}	
	
	public static String getMethod(String path){
		return path.split(" ")[0];
	}	
	
	public static String getSegment(String path, int index){		
		try {
			return (String) ((List<String>) parsePath(path)).get(0);
		} catch (InvalidCommandPathException e) {
			return "";
		} 
	}
}

