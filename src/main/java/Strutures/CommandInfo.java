package Strutures;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class CommandInfo {	
	private String method;			//GET
	private String table;
	private HashMap<String, String> parameters;		// name=filme&release_year=2016
	private Collection<String> resources;	


	//TODO (see MainApp.java for details)
	public CommandInfo(String[] args){
		throw new NotImplementedException();
	}

	public CommandInfo(String method, String path, String param) {
		
		if(method == null || path == null)
			return;
		
		this.method = method;

		resources = new ArrayList<String>();
		String [] tmpresources = path.split("/");		
		table=tmpresources[1];
		for(int i = 1; i < tmpresources.length ; i++) // i=1 skip initial ""
			resources.add(tmpresources[i]);
		if(param == null) return;
		
		parameters = new HashMap<String,String>();
		String [] tmpparam = param.split("&");
		
		for(int i = 0; i < tmpparam.length; i++){			
			String [] aux = tmpparam[i].split("=");	
			parameters.put(aux[0],aux[1]);
		}		
	}
	
	/**
	 * @return method POST or GET
	 */
	public String getMethod() { return method;}

	public String getTable(){
		return table;
	}
	/**
	 * @return 
	 */
	public Collection<String> getResources(){ return resources;}
	
	/**
	 * 
	 */
	public HashMap<String,String> getData(){ return parameters;}

	public void addToMapData(String key, String value){
		parameters.put(key,value);
	}
	 /*
	  * 
	  */
	public int getResourcesSize() { return resources.size();}
	
}

