package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsRatingsLowerAverage extends CommandBase {
	private static final String INFO = "GET /tops/ratings/lower/average - returns the detail for the movie with the lower average rating.";
	private final String TITLE = "Movie with lowest average rating";

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws SQLException {
		final boolean DESC_ORDER = false;
		return TopsCommon.getRatings(TITLE, DESC_ORDER);
	}

	@Override
	public String getInfo() {
		return INFO;
	}

}
