package commands;

import java.util.Map;

public class HeaderInfo {

	private Map<String,String> headers;
	
	public HeaderInfo(Map<String,String> h){
		headers = h;
	}
	
	public Map<String,String> getHeaders(){
		return headers;
	}
}
