package Strutures;

import java.util.Collection;

/**
 * Created by Red on 24/04/2016.
 */
public interface IResult {
    void display(ResultInfo resultInfo) throws Exception;
    void writeToFile(String filename) throws Exception;
}
