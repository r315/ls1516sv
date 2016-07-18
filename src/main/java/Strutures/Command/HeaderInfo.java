package Strutures.Command;

import java.util.HashMap;
import java.util.Map;

import exceptions.InvalidCommandException;
import utils.Decoder;

public class HeaderInfo {

	private Map<String,String> headers;

	public HeaderInfo(){
		headers= new HashMap<>();
		headers.put("accept","text/html");
	}

	public HeaderInfo(String[] h) throws InvalidCommandException {
		headers= Decoder.decodeHeaders(h);
		if(headers.size()==0)
			headers.put("accept","text/html");
	}
	
	public Map<String,String> getHeadersMap(){
		return headers;
	}
}
