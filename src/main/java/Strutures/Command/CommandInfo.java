package Strutures.Command;

import decoders.DecodeMethod;
import decoders.DecodeParameters;
import decoders.DecodePath;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandMethodException;
import exceptions.InvalidCommandPathException;

import java.util.Collection;
import java.util.HashMap;

public class CommandInfo {	
	private String method;			//GET
	private String table;			//movie
	private HashMap<String, String> parameters;		// name=filme&release_year=2016
	private Collection<String> resources;	//path

	public CommandInfo(String m, String path,String params) throws InvalidCommandException {
		if(m==null)throw  new InvalidCommandMethodException();
		if(path==null)throw  new InvalidCommandPathException();
		if(params==null)parameters=new HashMap<>();
		else{
			parameters = DecodeParameters.decodeParameters(params);
		}
		method = DecodeMethod.decode(m);
		resources = DecodePath.decodePath(path);
		if(resources.size()!=0)
			table = resources.iterator().next();
	}

	public CommandInfo(String[] command) throws InvalidCommandException{
		method = DecodeMethod.decode(command);
		resources = DecodePath.decode(command);
		parameters = DecodeParameters.decode(command);
		if(resources.size()!=0)
			table = resources.iterator().next();
	}
	
	public String getMethod() { return method;}

	public String getTable(){ return table; }
	
	public Collection<String> getResources(){ return resources;}	
	
	public HashMap<String,String> getData(){ return parameters;}

	public void addToMapData(String key, String value){	parameters.put(key,value); }
	
	public int getResourcesSize() { return resources.size();}
}

