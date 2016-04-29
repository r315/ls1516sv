package Strutures;

import decoders.DecodeHeaders;

import java.util.HashMap;
import java.util.Map;

public class HeaderInfo {

	private Map<String,String> headers;

	//TODO
	public HeaderInfo(String[] h){
		headers=DecodeHeaders.decode(h);
		if(headers==null){
			headers=new HashMap<>(1);
			headers.put("accept","text/plain");
		}
	}
	
	public Map<String,String> getHeaders(){
		return headers;
	}
}
