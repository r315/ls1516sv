package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsNRatingsHigherAverage extends CommandBase {
	private static final String INFO = "GET /tops/{n}/ratings/higher/average - returns a list with the n movies with higher average ratings, sorted decreasingly.";
	private final String TITLE = " Movies with higher average ratings"; //Add n before

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
		final boolean DESC_ORDER = true;
		return TopsCommon.getNRatings(data, TITLE, DESC_ORDER);
	}

	@Override
	public String getInfo() {
		return INFO;
	}

}
