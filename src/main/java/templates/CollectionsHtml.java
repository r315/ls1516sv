package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmr on 17/07/2016.
 */
public class CollectionsHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
//        if (query == null) query = "top=5";
//        else if (!query.contains("top=")) query += "&top=5";

        //Add collections links to each column
        List<Pair<String,String>> pairs=new ArrayList<>();
        for (ArrayList<String> line : ri.getValues())
            pairs.add(new Pair<>(line.get(1),"/collections/"+line.get(0)));

        //Remove id column
        List<String> list= ri.removeColumn("ID");

        HtmlTree page = new HtmlTree();
        page.addData(ri);

        //Set Navigation links
        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/")
                )
        );

        page.addLinksToTable(pairs);

        //TODO fix paging
        //page.addPaging(Utils.paging(query, "/collections"));

        page.addFormGeneric("Add Collection",
                Arrays.asList(
                        new Pair<String, String>("method", "POST"),
                        new Pair<String, String>("action", String.format("/collections?%s", "top=5"))),
                Arrays.asList(
                        new HtmlElement("br", "Collection title"),
                        new HtmlElement("input")
                                .addAttributes("name", "name")
                                .addAttributes("type", "text")
                                .addAttributes("required", null),
                        new HtmlElement("br", "Description"),
                        new HtmlElement("input")
                                .addAttributes("name", "description")
                                .addAttributes("type", "text")
                                .addAttributes("required", null))
        );
        return page.getHtml();
    }
}
