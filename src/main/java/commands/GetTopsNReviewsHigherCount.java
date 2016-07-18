package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsNReviewsHigherCount extends CommandBase {
	private static final String INFO = "GET /tops/{n}/reviews/higher/count - returns a list with the n movies with higher review count.";
	private final String TITLE = " Movies with higher review count"; //Add n before

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
		final boolean DESC_ORDER = true;
		return TopsCommon.getNReview(data, TITLE, DESC_ORDER);
	}

	@Override
	public String getInfo() {
		return INFO;
	}
}
