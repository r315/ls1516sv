package commands;

import Strutures.Command.ICommand;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandVariableException;
import sqlserver.ConnectionFactory;
import utils.Pair;
import utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GetTopsNRatingsLowerAverage implements ICommand {
	private static final String INFO = "GET /tops/{n}/ratings/lower/average - returns a list with the n movies with the lower average ratings, sorted decreasingly.";
	private final String TITLE = " Movies with lower average ratings"; //Add n before

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws InvalidCommandException, SQLException {
		String topS = data.get("top");

		Boolean topB = (topS != null);
		int skip, top;

		Pair<Integer, Integer> skiptop = Utils.getSkipTop(data.get("skip"), topS);

		skip = skiptop.value1;
		top = skiptop.value2;

		try(
				Connection conn = ConnectionFactory.getConn();
				PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top))
		){
			int n = Utils.getInt(data.get("n"));
			if (n < 0) throw new InvalidCommandVariableException();

			pstmt.setInt(1, n);
			pstmt.setInt(2, skip);

			ResultSet rs = pstmt.executeQuery();

			return GetTopsNRatingsHigherAverage.createRI(rs, n, TITLE);

		} catch (NumberFormatException e) {
			throw new InvalidCommandVariableException();
		}

	}

	@Override
	public String getInfo() {
		return INFO;
	}

	private String getQuery(Boolean topB, int top) {
		String query = "SELECT * FROM ( " +
						"SELECT Movie.*, ((one * 1. + two * 2 + three * 3 + four * 4 + five * 5) / nullif((one + two + three + four + five),0)) as rating FROM Movie\n" +
						"LEFT JOIN (\n" +
						"SELECT movie_id, COALESCE((one + [1]), one, [1]) as one, COALESCE((two + [2]), two, [2]) as two, COALESCE((three + [3]), three, [3]) as three, COALESCE((four + [4]), four, [4]) as four, COALESCE((five + [5]), five, [5]) as five\n" +
						"FROM\n" +
						"(\n" +
						"SELECT Rating.movie_id, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5]\n" +
						"FROM Rating\n" +
						"FULL JOIN (\n" +
						"SELECT movie_id, [1], [2], [3], [4], [5]\n" +
						"FROM\n" +
						"(SELECT movie_id, rating FROM Review GROUP BY rating, movie_ID) AS SourceTable\n" +
						"PIVOT\n" +
						"(\n" +
						"COUNT(SourceTable.rating)\n" +
						"FOR rating IN ([1], [2], [3], [4], [5])\n" +
						") AS SourceTable) AS reviewRatings ON reviewRatings.movie_id = Rating.movie_id\n" +
						"GROUP BY Rating.movie_id, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5]\n" +
						") AS average) AS ratings ON ratings.movie_id = Movie.movie_id\n" +
						"ORDER BY rating OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY " +
						") AS tudo ORDER BY rating DESC OFFSET ? ROWS";
		if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
		return query;
	}

}
