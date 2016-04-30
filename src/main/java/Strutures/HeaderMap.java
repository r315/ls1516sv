package Strutures;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Red on 26/04/2016.
 */
public class HeaderMap {

    HashMap<String, IResult> headersMap;

    public HeaderMap(){headersMap=new HashMap<>();}

    public HeaderMap(HashMap<String, IResult> headersMap){
        this.headersMap=headersMap;
    }

    public void addResponseMethod(String resultMethod, IResult result){
        headersMap.put(resultMethod,result);
}

    public Collection<IResult> getResults(){
        return this.headersMap.values();
    }

    public IResult getResponseMethod(HeaderInfo headerInfo){
       return headersMap.get(headerInfo.getHeadersMap().get("accept"));
    }
}
