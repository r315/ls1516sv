package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsNReviewsLowerCount extends CommandBase {
    private static final String INFO = "GET /tops/{n}/reviews/lower/count - returns a list with the n movies with lower review count.";
    private final String TITLE = " Movies with lower review count"; //Add n before

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
        final boolean DESC_ORDER = false;
        return TopsCommon.getNReview(data, TITLE, DESC_ORDER);

    }

    @Override
    public String getInfo() {
        return INFO;
    }

}