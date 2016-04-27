package Strutures;

import decoders.DecodeHeaders;

import java.util.HashMap;
import java.util.Map;

public class HeaderInfo {

	private Map<String,String> headers;

	//TODO
	public HeaderInfo(String[] h){
		HashMap<String,String> map=DecodeHeaders.decode(h);
		if(map==null){
			headers.put("accept","text/plain");
			return;
		}
		//TODO finish Ctor for other cases
	}
	
	public Map<String,String> getHeaders(){
		return headers;
	}
}
