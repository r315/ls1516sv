package console;

public class CommandInfo {
	
	private String method;			//GET
	private String fullpath;		// /movie/{mid}/review
	private String fullparam;		// name=filme&ano=2016
	
	private String[] resources;
	private int ridx;
	
	
	
	
	public CommandInfo(String method, String path, String param){
		this.method = method;
		this.fullpath = path;
		this.fullparam = param;
		
		this.resources = fullpath.split("/");
		ridx = 1;  // skip initial '/'
		
		
		
	}
	
	
	public String[] getResources(){ return resources;}
	public String getMethod() { return method;}
	
	public int getInt(){
		if(ridx < this.resources.length) return -1;
		try{
			int val = Integer.parseInt(resources[ridx]);
			ridx++;
			return val;
		}catch (NumberFormatException ex){
			return -1;		
		}
	}
	
	
	public boolean isString(){		
		try{
			int val = Integer.parseInt(resources[ridx]);			
		}catch (NumberFormatException ex){
			return true;			
		}		
		return false;
	}
	
	
	
	public String getString(){
		if(ridx < this.resources.length) return null;		
		return resources[ridx++];		
	}
	
	
	
	
	
	
	
	
	
	
}

