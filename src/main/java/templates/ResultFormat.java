package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by Red on 15/07/2016.
 */
public abstract class ResultFormat {

    public abstract String generate(CommandInfo commandInfo, ResultInfo resultInfo);
}
