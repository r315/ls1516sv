package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsRatingsHigherAverage extends CommandBase {
    private static final String INFO = "GET /tops/ratings/higher/average - returns the detail for the movie with the higher average rating.";
    private final String TITLE = "Movie with higher average rating";

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws SQLException {
        final boolean DESC_ORDER = true;
        return TopsCommon.getRatings(TITLE, DESC_ORDER);
    }

    @Override
    public String getInfo() {
        return INFO;
    }

}
