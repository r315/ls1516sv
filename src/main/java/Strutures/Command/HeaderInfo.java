package Strutures.Command;

import exceptions.InvalidCommandException;
import utils.Decoder;

import java.util.HashMap;
import java.util.Map;

public class HeaderInfo {

	public static final String FILENAME_TOKEN="file-name";
	public static final String ACCEPT_TOKEN="accept";
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

	public String getHeaderValue(String key){
		return headers.get(key);
	}

	public boolean hasKey(String key){
		return headers.containsKey(key);
	}
}
