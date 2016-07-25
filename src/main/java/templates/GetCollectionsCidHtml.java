package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import commands.GetMovies;
import exceptions.InvalidCommandException;
import utils.Pair;
import utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

        page.addPaging(Utils.paging(ci.getData(), String.format("/collections/%s", cid)));

        page.addFormGeneric("Add Movie to Collection",
                Arrays.asList(
                        new Pair<>("method", "POST"),
                        new Pair<>("action", String.format("/collections/%s/movies", cid))
                ),
                Arrays.asList(
                        new HtmlElement("br","Movie ID"),
                        HtmlTree.createDropDownMenu("mid", getMoviesList())
                )
        );

        return page.getHtml();
    }

    private static List<Pair<String, String>> getMoviesList() throws SQLException, InvalidCommandException {
        final int TITLE_POSITION = 1, YEAR_POSITION = 2, ID_POSITION = 0;

        List<Pair<String, String>> movies = new ArrayList<>();

        HashMap<String, String> param = new HashMap<>();
        param.put("sortBy","title");

        GetMovies command = new GetMovies();
        ResultInfo ri = command.execute(param);

        if (ri.getValues().isEmpty()){
            return movies;
        }

        for (ArrayList<String> line : ri.getValues()) {
            Pair<String,String> movie = new Pair<>(
                    String.format("%s (%s)", line.get(TITLE_POSITION), line.get(YEAR_POSITION)),line.get(ID_POSITION));
            movies.add(movie);
        }

        return movies;
    }
}
