package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;

public class PostMoviesHtml implements IResultFormat {

    @Override
    public String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException {
        String movieID = ri.getGeneratedId();
        return String.format("/movies/%d",Integer.parseInt(movieID));
    }
}
