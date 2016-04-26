package Strutures;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import decoders.DecodeMethod;
import decoders.DecodeParameters;
import decoders.DecodePath;

public class CommandInfo {	
	private String method;			//GET
	private String table;			//movie
	private HashMap<String, String> parameters;		// name=filme&release_year=2016
	private Collection<String> resources;	

	
	public CommandInfo(String[] command) throws Exception{
		method = DecodeMethod.decode(command);
		resources = DecodePath.decode(command);
		parameters = DecodeParameters.decode(command);		
		table = ((List<String>)resources).get(0);		
	}
	
	public String getMethod() { return method;}

	public String getTable(){ return table; }
	
	public Collection<String> getResources(){ return resources;}	
	
	public HashMap<String,String> getData(){ return parameters;}

	public void addToMapData(String key, String value){	parameters.put(key,value); }
	
	public int getResourcesSize() { return resources.size();}	
}

