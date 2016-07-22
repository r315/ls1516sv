package Strutures.Command;

import exceptions.InvalidCommandException;
import utils.Decoder;

import java.util.HashMap;
import java.util.Map;

public class HeaderInfo {

	public static final String FILENAME_TOKEN="file-name";
	public static final String ACCEPT_TOKEN="accept";
	public static final String TEXT_HTML_TOKEN="text/html";
	public static final String TEXT_PLAIN_TOKEN="text/plain";
	private Map<String,String> headers;

	public HeaderInfo(){
		headers= new HashMap<>();
		headers.put(ACCEPT_TOKEN,TEXT_HTML_TOKEN);
	}

	public HeaderInfo(String[] h) throws InvalidCommandException {
		headers= Decoder.decodeHeaders(h);
		if(!headers.containsKey(ACCEPT_TOKEN))
			headers.put(ACCEPT_TOKEN,TEXT_HTML_TOKEN);
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
