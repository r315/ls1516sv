package Strutures;

import java.util.Map;

/**
 * Created by Red on 24/04/2016.
 */
public interface IResult {
    void display(ResultInfo resultInfo, Map<String,String> headers) throws Exception;
    void writeToFile(String filename) throws Exception;
}
