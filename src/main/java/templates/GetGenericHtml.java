package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

/**
 * Created by Luigi Sekuiya on 17/07/2016.
 */
public class GetGenericHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
        if(!ri.generateresult) return "";

        HtmlTree page = new HtmlTree();

        page.addData(ri);
        return page.getHtml();
    }
}
