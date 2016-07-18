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
 * Created by hmr on 15/07/2016.
 */
public class GetMoviesHtml implements IResultFormat {

    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        List<Pair<String,String>> pairs = new ArrayList<>();

        for (ArrayList<String> line : ri.getValues()){
            pairs.add(new Pair<>(line.get(1),"/movies/"+line.get(0)));
        }

        ri.removeColumn("ID");

        HtmlTree page = new HtmlTree();

        page.addData(ri);
        page.addLinksToTable(pairs);

        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home","/"),
                        new Pair<>("Movies","/movies"),
                        new Pair<>("Top Ratings","/tops/ratings")
                )
        );

        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Sort by Date","/movies?sortBy=addedDate"),
                        new Pair<>("Sort by Date Desc","/movies?sortBy=addedDateDesc"),
                        new Pair<>("Sort by Year","/movies?sortBy=year"),
                        new Pair<>("Sort by Year Desc","/movies?sortBy=yearDesc"),
                        new Pair<>("Sort by Title","/movies?sortBy=title"),
                        new Pair<>("Sort by Title Desc","/movies?sortBy=titleDesc"),
                        new Pair<>("Sort by Rating","/movies?sortBy=rating"),
                        new Pair<>("Sort by Rating Desc","/movies?sortBy=ratingDesc")
                )
        );

        page.addPaging(Utils.paging(ci.getData(), "/movies"));

        page.addFormGeneric("Insert a new Movie"
                ,Arrays.asList(new Pair<>("method","POST"),new Pair<>("action","/movies"))
                ,Arrays.asList(
                        new HtmlElement("br", "Movie title:"),
                        new HtmlElement("input").addAttributes("type","text").addAttributes("name","title").addAttributes("required",null),

                        new HtmlElement("br", "Release year:"),
                        new HtmlElement("input").addAttributes("type","text").addAttributes("name","releaseYear").addAttributes("required",null)
                )
        );

        return page.getHtml();
    }
}
