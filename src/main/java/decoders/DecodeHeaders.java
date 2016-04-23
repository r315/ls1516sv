package decoders;

import java.util.HashMap;

public class DecodeHeaders {

    public static HashMap<String, String> decode(String line) throws Exception {
        return decode(line.split(" "));
    }

    public static HashMap<String, String> decode (String [] args) throws Exception {
        if (args.length > 3 && args[2] != "" && args[2] != null) return decodeParameters(args[2]);
        else if (args.length > 2 && args[2] != null && args[2].indexOf(':') >= 0) return decodeParameters(args[2]);
        else return null;
    }

    private static HashMap<String, String> decodeParameters(String param) {
        String [] paramS = param.split("\\|");
        HashMap<String, String> parameters = new HashMap<>();

        for(int i = 0; i < paramS.length; i++){
            String [] aux = paramS[i].split(":");
            parameters.put(aux[0],aux[1]);
        }

        return parameters;
    }
}
