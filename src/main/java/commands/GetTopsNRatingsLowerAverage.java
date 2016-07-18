package commands;

import Strutures.Command.CommandBase;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import utils.TopsCommon;

import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsNRatingsLowerAverage extends CommandBase {
	private static final String INFO = "GET /tops/{n}/ratings/lower/average - returns a list with the n movies with the lower average ratings, sorted decreasingly.";
	private final String TITLE = " Movies with lower average ratings"; //Add n before

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
		final boolean DESC_ORDER = false;
		return TopsCommon.getNRatings(data, TITLE, DESC_ORDER);

	}

	@Override
	public String getInfo() {
		return INFO;
	}

}
