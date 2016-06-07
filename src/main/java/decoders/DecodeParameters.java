package decoders;

import java.util.HashMap;

import exceptions.InvalidCommandParametersException;

/**
 * Created by Luigi Sekuiya on 23/04/2016.
 */
public class DecodeParameters {

    public static HashMap<String, String> decode(String line) throws InvalidCommandParametersException{
        return decode(line.split(" "));
    }

    public static HashMap<String, String> decode (String [] args)throws InvalidCommandParametersException {
        if (args.length > 3 && args[3] != null && args[3].length() != 0) return decodeParameters(args[3]);
        else if (args.length > 2 && args[2] != null && args[2].indexOf('=') >= 0) return decodeParameters(args[2]);
        else return new HashMap<>();
    }

    public static HashMap<String, String> decodeParameters(String param)throws InvalidCommandParametersException {
        String [] paramS = param.split("&");
        HashMap<String, String> parameters = new HashMap<>();

        for(int i = 0; i < paramS.length; i++){
            String [] aux = paramS[i].split("=");
            if(aux.length!=2)throw new InvalidCommandParametersException();
            aux[1]=aux[1].replace('+',' ');
            parameters.put(aux[0],aux[1]);
        }

        return parameters;
    }
}
