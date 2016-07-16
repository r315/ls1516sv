package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by hmr on 15/07/2016.
 */
public class GetMoviesHtml extends ResultFormat {

    @Override
    public String generate(CommandInfo commandInfo, ResultInfo resultInfo) {
        return resultInfo.getDisplayTitle();
    }
}
