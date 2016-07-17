package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;

import java.util.Arrays;

/**
 * Created by Red on 17/07/2016.
 */
public class GetTopsRatingsHtml implements IResultFormat{

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
        HtmlTree page = new HtmlTree();
        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home","/"),
                        new Pair<>("Movies","/movies")
                )
        );
        page.addList(
                Arrays.asList(
                        new Pair<>("Ratings Higher Average","/tops/5/ratings/higher/average"),
                        new Pair<>("Ratings Lower Average","/tops/5/ratings/lower/average"),
                        new Pair<>("Review Higher Count","/tops/5/reviews/higher/count"),
                        new Pair<>("Review Lower Count","/tops/5/reviews/lower/count")
                ),"Tops"
        );
    return page.getHtml();
    }
}
