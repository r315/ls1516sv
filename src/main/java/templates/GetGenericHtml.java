package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;

/**
 * Created by Luigi Sekuiya on 17/07/2016.
 */
public class GetGenericHtml implements IResultFormat {

    private static GetGenericHtml instance=null;

    private GetGenericHtml(){}

    public static GetGenericHtml getInstance(){
        if(instance == null)
            instance = new GetGenericHtml();
        return instance;
    }

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        if(!ri.generateresult) return "";
        HtmlTree page = new HtmlTree();
        page.addData(ri);
        return page.getHtml();
    }
}
