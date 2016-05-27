package Strutures.Command;

import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by Red on 26/04/2016.
 */
public class HeaderMap {

    HashMap<String, Function<ResultInfo,IResultFormat>> headersMap;

    public HeaderMap(){headersMap=new HashMap<>();}

    public HeaderMap(HashMap<String, Function<ResultInfo,IResultFormat>> headersMap){
        this.headersMap=headersMap;
    }

    public void addResponseMethod(String resultMethod, Function<ResultInfo,IResultFormat> result){
        headersMap.put(resultMethod,result);
}

    public Collection<Function<ResultInfo,IResultFormat>> getResults(){
        return this.headersMap.values();
    }

    public Function<ResultInfo,IResultFormat> getResponseMethod(HeaderInfo headerInfo){
       return headersMap.get(headerInfo.getHeadersMap().get("accept"));
    }

}
