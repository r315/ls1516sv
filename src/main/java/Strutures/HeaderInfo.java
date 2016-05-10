package Strutures;

import java.util.Map;

import decoders.DecodeHeaders;

public class HeaderInfo {

	private Map<String,String> headers;

	//TODO Ctor with a String argument instead of array
	public HeaderInfo(String[] h){
		headers=DecodeHeaders.decode(h);
		if(headers.size()==0)
			headers.put("accept","text/html");
	}
	
	public Map<String,String> getHeadersMap(){
		return headers;
	}
}
