package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.Pair;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by hmr on 17/07/2016.
 */
public class GetHomeHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
            HtmlTree page = new HtmlTree();
            page.addNavigationLinks(
                    Arrays.asList(
                            new Pair<>("Home", "/"),
                            new Pair<>("Movies", "/movies"),
                            new Pair<>("Collections", "/collections"),
                            new Pair<>("Top Ratings", "/tops/ratings")
                    )
            );
            page.addFooter(new HtmlElement("div","Joao Duarte | Luis Almeida | Hugo Reis").
                            addAttributes("style","position:absolute;bottom:0;width:99%;text-align:center"));
            return page.getHtml();
    }
}
