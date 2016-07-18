package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class PostMoviesMidReviewsHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        String mid = ci.getResources().stream().collect(Collectors.toList()).get(1);
        String rid = ri.getGeneratedId();
        return String.format("/movies/%d/reviews/%d",Integer.parseInt(mid),Integer.parseInt(rid));
    }
}
