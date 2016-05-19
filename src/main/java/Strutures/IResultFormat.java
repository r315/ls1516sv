package Strutures;

import java.util.Map;

/**
 * Created by Red on 18/05/2016.
 */
public interface IResultFormat {
    String generate(ResultInfo resultInfo, Map<String,String> headers) throws Exception;
    //void display(Map<String,String> headers);
}
