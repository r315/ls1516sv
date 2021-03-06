package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import utils.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hmr on 17/07/2016.
 */
public class GetMoviesMidReviewsRidHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        Iterator<ArrayList<String>> it= ri.getValues().iterator();
        if(!it.hasNext())throw new InvalidCommandParametersException("Page not found!");
        ArrayList<String> values = it.next();

        Pair<String,String> pair1 = new Pair<>(values.get(1),"/movies/"+values.get(0));
        Pair<String,String> pair2 = new Pair<>(values.get(1)+"'s Reviews","/movies/"+values.get(0)+"/reviews");

        List<Pair<String,String>> pairs = new ArrayList<>();

        ri.removeColumn("Movie's ID");
        ri.removeColumn("Movie's Title");
        ri.removeColumn("Review's ID");

        HtmlTree page = new HtmlTree();
        page.addData(ri);
        page.addLinksToTable(pairs);
        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        pair1,
                        pair2
                )
        );
        return page.getHtml();
    }
}
