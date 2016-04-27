package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import utils.Utils;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

public class GetTopsNRatingsLowerAverage implements ICommand {
	private final String INFO = "returns a list with the n movies with the lower average ratings, sorted decreasingly.";

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws Exception {
		try(Connection conn = ConnectionFactory.getConn()) {
			int n;

			try {
				n = Utils.getInt(data.get("n"));
			} catch (NumberFormatException e) {
				throw new InvalidCommandVariableException();
			}

			PreparedStatement pstmt = conn.prepareStatement(getQuery());
			pstmt.setInt(1, n);

			ResultSet rs = pstmt.executeQuery();

			printRS(rs);

			pstmt.close();
		}

		//Builderino stuff
		ResultInfo stuff = new ResultInfo();
		return stuff;
	}

	@Override
	public String getInfo() {
		return INFO;
	}

	private String getQuery() {
		return "SELECT TOP (?) title, release_year, COALESCE ((ratavg + revavg) / 2, ratavg, revavg) AS average " +
				"FROM " +
				"(" +
				"SELECT Movie.title, Movie.release_year, ((Rating.one * 1 + Rating.two * 2 + Rating.three * 3 + Rating.four * 4 + Rating.five * 5) / (Rating.one + Rating.two + Rating.three + Rating.four + Rating.five)) AS ratavg, AVG(Review.rating) AS revavg " +
				"FROM Movie " +
				"LEFT JOIN Rating ON Movie.movie_id = Rating.movie_id " +
				"LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
				"GROUP BY Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five " +
				") AS average " +
				"ORDER BY average";
	}

	private void printRS(ResultSet rs) throws SQLException {
		while(rs.next()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("release_year"));

			System.out.println(rs.getString("title") + " (" + calendar.get(Calendar.YEAR) + "): " + rs.getInt("average"));
		}

	}

}
