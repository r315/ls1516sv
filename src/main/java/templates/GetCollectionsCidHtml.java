package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import utils.Pair;
import utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        Collection<ArrayList<String>> values= ri.getValues();
        if(!ri.generateresult)throw new InvalidCommandParametersException("Collection not found!");

        for (ArrayList<String> line : values)
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

        page.addPaging(Utils.paging(ci.getData(), String.format("/collections/%s", cid)));

        page.addFormGeneric("Add Movie to Collection",
                Arrays.asList(
                        new Pair<>("method", "POST"),
                        new Pair<>("action", String.format("/collections/%s/movies", cid))
                ),
                Arrays.asList(
                        new HtmlElement("br","Select Movie"),
                        HtmlTree.createDropDownMenu("mid", Utils.getMoviesList())
                )
        );

        return page.getHtml();
    }
}
