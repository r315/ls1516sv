package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class GetTopsRatingsLowerAverage implements ICommand {
	private static final String INFO = "GET /tops/ratings/lower/average - returns the detail for the movie with the lower average rating.";
	private final String TITLE = "Movie with lowest average rating";

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws SQLException {
		try(
				Connection conn = ConnectionFactory.getConn();
				Statement stmt = conn.createStatement()
		) {
			ResultSet rs = stmt.executeQuery(GetTopsRatingsHigherAverage.getQuery(false));

			return GetTopsRatingsHigherAverage.createRI(rs, TITLE);
		}
	}

	@Override
	public String getInfo() {
		return INFO;
	}

}
