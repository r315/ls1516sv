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
    public String generate(ResultInfo ri, CommandInfo ci) {
        String mid = ri.getValues().iterator().next().get(0);

        //Get Reviews
        GetMoviesMidReviews reviews = new GetMoviesMidReviews();

        HashMap<String, String> param = new HashMap<>();
        param.put("mid",mid);

        ResultInfo resultInfo = null;
        try {
            resultInfo = reviews.execute(param);
        } catch (InvalidCommandException e) {
           //TODO: handle this exceptions
        } catch (SQLException e) {

        }

        List<Pair<String,String>> pairs = new ArrayList<>();

        for (ArrayList<String> line : resultInfo.getValues()){
            pairs.add(new Pair<>(line.get(3),"/movies/"+mid+"/reviews/"+line.get(2)));
        }

        //Generate and Add Reviews
        HtmlTree page = new HtmlTree();
        page.addData(ri);

        if (!pairs.isEmpty()) page.addList(pairs,"Reviews by");

        //Get Collections
        GetCollectionMoviesMid collections = new GetCollectionMoviesMid();

        try {
            resultInfo = collections.execute(param);
        } catch (InvalidCommandException e) {
            //TODO: handle this exceptions
        } catch (SQLException e) {
            //TODO: handle this exceptions
        }

        pairs = new ArrayList<>();

        for (ArrayList<String> line : ri.getValues()){
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
