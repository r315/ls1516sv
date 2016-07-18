package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Red on 17/07/2016.
 */
public class GetTopsNHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {

        //Add collections links to each column
        List<Pair<String,String>> pairs=new ArrayList<>();

        for (ArrayList<String> line : ri.getValues())
            pairs.add(new Pair<>(line.get(1),"/movies/"+line.get(0)));

        //Remove id column
        ri.removeColumn("ID");

        HtmlTree page = new HtmlTree();
        page.addData(ri);

        //Set Navigation links
        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        new Pair<>("Top Ratings", "/tops/ratings")
                )
        );

        page.addLinksToTable(pairs);

        return page.getHtml();
    }
}
