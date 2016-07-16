package Strutures.ResponseFormat;

import Strutures.Command.CommandInfo;

/**
 * Created by Red on 18/05/2016.
 */
public interface IResultFormat {
    String generate(ResultInfo ri, CommandInfo ci);
}
