package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import commands.GetCollectionMoviesMid;
import commands.GetMoviesMidReviews;
import exceptions.InvalidCommandException;
import utils.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hmr on 17/07/2016.
 */
public class GetMoviesMidHtml implements IResultFormat {
    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        String mid = ri.getValues().iterator().next().get(0);
        ResultInfo resultInfo = null;
        GetMoviesMidReviews reviews = new GetMoviesMidReviews();
        GetCollectionMoviesMid collections = new GetCollectionMoviesMid();
        HashMap<String, String> param = new HashMap<>();

        param.put("mid",mid);
        resultInfo = reviews.execute(param);
        List<Pair<String,String>> pairs = new ArrayList<>();
        for (ArrayList<String> line : resultInfo.getValues()){
            pairs.add(new Pair<>(line.get(3),"/movies/"+mid+"/reviews/"+line.get(2)));        }

        HtmlTree page = new HtmlTree();
        page.addData(ri);
        if (!pairs.isEmpty()) page.addList(pairs,"Reviews by");
        resultInfo = collections.execute(param);

        pairs = new ArrayList<>();

        for (ArrayList<String> line : resultInfo.getValues()){
            pairs.add(new Pair<>(line.get(1),"/collections/"+line.get(0)));
        }

        if (!pairs.isEmpty()) page.addList(pairs, "Collections");

        page.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        new Pair<>("Movies", "/movies"),
                        new Pair<>("Ratings", "/movies/" + mid + "/ratings"),
                        new Pair<>("Reviews", "/movies/" + mid + "/reviews")
                )
        );

        return page.getHtml();
    }
}
