package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
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

/**
 * Created by hmr on 17/07/2016.
 */
public class GetMoviesMidRatingsHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        Iterator<ArrayList<String>> it=ri.getValues().iterator();
        if(!it.hasNext()) throw new InvalidCommandParametersException("Movie not found");
        ArrayList<String> values= it.next();
        String mid = values.get(0);
        String movie_id=values.get(0);
        String movie_name=values.get(1);

        Pair<String,String> pair = new Pair<>(movie_name,"/movies/"+movie_id);

        ri.removeColumn("ID");
        ri.removeColumn("Titulo");

        HtmlTree page = new HtmlTree();
        page.addData(ri);

        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        pair
                )
        );

        page.addFormGeneric(
                String.format("Submit a rating to movie %s", movie_name),
                Arrays.asList(
                        new Pair<>("method", "POST"),
                        new Pair<>("action", String.format("/movies/%s/ratings", movie_id))),
                Arrays.asList(
                        new HtmlElement("p", "Rating:"),
                        new HtmlElement("select").addAttributes("name", "rating").
                                addChild(new HtmlElement("option", "1").addAttributes("value", "1")).
                                addChild(new HtmlElement("option", "2").addAttributes("value", "2")).
                                addChild(new HtmlElement("option", "3").addAttributes("value", "3")).
                                addChild(new HtmlElement("option", "4").addAttributes("value", "4")).
                                addChild(new HtmlElement("option", "5").addAttributes("value", "5"))
                )
        );
        return page.getHtml();
    }
}
