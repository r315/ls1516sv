package Strutures;

import java.util.HashMap;

/**
 * Created by Red on 26/04/2016.
 */
public class HeaderMap {

    HashMap<String, IResult> headersMap;

    public HeaderMap(){

    }

    public void addResponseMethod(String resultMethod, IResult result){
        headersMap.put(resultMethod,result);
}

    public IResult getResponseMethod(HeaderInfo headerInfo){
       return headersMap.get(headerInfo.getHeaders().get("accept"));
    }

    public static HeaderMap createMap(){
        HeaderMap map=new HeaderMap();

        map.addResponseMethod("text/html",new HtmlResult());
        map.addResponseMethod("text/plain",new TextResult());

        return map;
    }
}
