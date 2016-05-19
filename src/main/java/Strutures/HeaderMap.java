package Strutures;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Red on 26/04/2016.
 */
public class HeaderMap {

    HashMap<String, IResultFormat> headersMap;

    public HeaderMap(){headersMap=new HashMap<>();}

    public HeaderMap(HashMap<String, IResultFormat> headersMap){
        this.headersMap=headersMap;
    }

    public void addResponseMethod(String resultMethod, IResultFormat result){
        headersMap.put(resultMethod,result);
}

    public Collection<IResultFormat> getResults(){
        return this.headersMap.values();
    }

    public IResultFormat getResponseMethod(HeaderInfo headerInfo){
       return headersMap.get(headerInfo.getHeadersMap().get("accept"));
    }

}
