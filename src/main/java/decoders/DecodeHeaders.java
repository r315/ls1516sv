package decoders;

import java.util.Arrays;
import java.util.HashMap;

public class DecodeHeaders {
	private static final String PARAM_SEPARATOR = "\\|";
	private static final String VALUE_SEPARATOR = ":";

    public static HashMap<String, String> decode(String line) {
        return decode(line.split(" "));
    }
    // this decoder is immune to double spaces between path and headers
    // also order of parameters and headers is not important
    public static HashMap<String,String> decode(String [] args){
		HashMap<String,String> map = new HashMap<String,String>();
		try{
		Arrays.stream(args)
			.filter(s->s.contains(VALUE_SEPARATOR))
			.map(sm->sm.split(PARAM_SEPARATOR))	//return Stream<String[]>
			.flatMap(Arrays::stream)	//return Stream<String>
			.forEach(h->{
					String [] sh = h.split(VALUE_SEPARATOR);
					map.put(sh[0],sh[1]);
				}
			);
		}catch(Exception e){
			//returns empty map on null args			
		}
		return map;
	}

//    public static HashMap<String, String> decode (String [] args) {
//        if (args.length > 3 && args[2] != null && args[2].length() != 0) return decodeParameters(args[2]);
//        else if (args.length > 2 && args[2] != null && args[2].indexOf(':') >= 0) return decodeParameters(args[2]);
//        else return new HashMap<>();
//    }
//
//    private static HashMap<String, String> decodeParameters(String param) {
//        String [] paramS = param.split("\\|");
//        HashMap<String, String> parameters = new HashMap<>();
//
//        for(int i = 0; i < paramS.length; i++){
//            String [] aux = paramS[i].split(":");
//            parameters.put(aux[0],aux[1]);
//        }
//
//        return parameters;
//    }
//    
//    
    
}
