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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hmr on 17/07/2016.
 */
public class GetMoviesMidReviewsHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        ArrayList<String> values;

        if (ri.getValues().iterator().hasNext()) values = ri.getValues().iterator().next();
        else {values = getInfo(ci, ri);}
        String mid = values.get(0);
        String movie_name = values.get(1);

        Pair<String,String> pair = new Pair<>(values.get(1),"/movies/"+mid);

        List<Pair<String,String>> pairs = new ArrayList<>();

        for (ArrayList<String> line : ri.getValues()){
            pairs.add(new Pair<>(line.get(5),"/movies/"+line.get(0)+"/reviews/"+line.get(2)));
        }

        ri.removeColumn("Movie's ID");
        ri.removeColumn("Movie's Title");
        ri.removeColumn("Review's ID");

        HtmlTree page = new HtmlTree();
        page.addData(ri);
        page.addLinksToTable(pairs);
        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        pair
                )
        );

        page.addPaging(Utils.paging(ci.getData(), String.format("/movies/%s/reviews", mid)));

        page.addFormGeneric(
                String.format("Submit a review for movie %s", movie_name)
                , Arrays.asList(new Pair<>("method", "POST"), new Pair<>("action", String.format("/movies/%s/reviews", mid)))
                , Arrays.asList(
                        new HtmlElement("br", "Name:"),
                        new HtmlElement("input").addAttributes("type", "text").addAttributes("name", "reviewerName").addAttributes("required", null),

                        new HtmlElement("br", "Rating:"),
                        new HtmlElement("select").addAttributes("name", "rating").
                                addChild(new HtmlElement("option", "1").addAttributes("value", "1")).
                                addChild(new HtmlElement("option", "2").addAttributes("value", "2")).
                                addChild(new HtmlElement("option", "3").addAttributes("value", "3")).
                                addChild(new HtmlElement("option", "4").addAttributes("value", "4")).
                                addChild(new HtmlElement("option", "5").addAttributes("value", "5")),

                        new HtmlElement("br", "Summary:"),
                        new HtmlElement("textarea").addAttributes("name", "reviewSummary").addAttributes("rows", "3").addAttributes("cols", "50").addAttributes("required", null),

                        new HtmlElement("br", "Review:"),
                        new HtmlElement("textarea").addAttributes("name", "review").addAttributes("rows", "10").addAttributes("cols", "50").addAttributes("required", null)
                )
        );
        return page.getHtml();
    }

    private ArrayList<String> getInfo(CommandInfo command, ResultInfo resultInfo) {
        HashMap<String, String> param = command.getData();
        ArrayList<String> info = new ArrayList<>();

        info.add(param.get("mid"));

        Pattern p = Pattern.compile("^(.*?)'s Reviews");
        Matcher m = p.matcher(resultInfo.getDisplayTitle());

        if (m.find()) info.add(m.group(1));

        return info;
    }
}
