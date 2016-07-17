package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by Luigi Sekuiya on 17/07/2016.
 */
public class PostCollectionsCidMoviesHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
        String cid = ci.getData().get("cid");
        return String.format("/collections/%s",cid);
    }
}
