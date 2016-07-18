package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsReviewsHigherCount extends CommandBase {
	private static final String INFO = "GET /tops/reviews/higher/count - returns the detail for the movie with most reviews.";
	private final String TITLE = "Movie with most reviews";

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws SQLException {
		return TopsCommon.getReview(TITLE);
	}

	@Override
	public String getInfo() {
		return INFO;
	}

}
