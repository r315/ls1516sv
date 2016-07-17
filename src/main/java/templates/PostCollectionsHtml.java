package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by Luigi Sekuiya on 17/07/2016.
 */
public class PostCollectionsHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
        String cid = ri.getGeneratedId();
        return String.format("/collections/%d",Integer.parseInt(cid));
    }
}
