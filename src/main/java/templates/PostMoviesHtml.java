package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;

public class PostMoviesHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) {
        String movieID = ri.getGeneratedId();
        return String.format("/movies/%d",Integer.parseInt(movieID));
    }
}
