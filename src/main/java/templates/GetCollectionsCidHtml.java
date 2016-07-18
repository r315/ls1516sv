package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.Pair;
import utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmr on 17/07/2016.
 */
public class GetCollectionsCidHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        String cid = ci.getData().get("cid");

        //Add collections links to each column
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (ArrayList<String> line : ri.getValues())
            pairs.add(new Pair<>(line.get(1), "/movies/" + line.get(0)));

        //Remove Release Year and Movie ID columns
        ri.removeColumn("Release Year");
        ri.removeColumn("Movie ID");

        HtmlTree page = new HtmlTree();
        page.addData(ri);

        //Set Navigation links
        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        new Pair<>("Collections", "/collections")
                )
        );

        page.addLinksToTable(pairs);

        page.addPaging(Utils.paging(Utils.reconQuery(ci.getData()), String.format("/collections/%s", cid)));

        page.addFormGeneric("Add Movie to Collection",
                Arrays.asList(
                        new Pair<String, String>("method", "POST"),
                        new Pair<String, String>("action", String.format("/collections/%s/movies", cid))),
                Arrays.asList(
                        new HtmlElement("br","Movie ID"),
                        new HtmlElement("input")
                            .addAttributes("name","mid")
                            .addAttributes("type","text")
                            .addAttributes("required",null))
        );

        return page.getHtml();
    }
}
