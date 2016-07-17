package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

import java.util.stream.Collectors;

/**
 * Created by Luigi Sekuiya on 17/07/2016.
 */
public class PostMoviesMidRatingsHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
        String mid = ci.getResources().stream().collect(Collectors.toList()).get(1);
        return String.format("/movies/%d/ratings",Integer.parseInt(mid));
    }
}
