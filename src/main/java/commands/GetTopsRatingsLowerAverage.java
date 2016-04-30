package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class GetTopsRatingsLowerAverage implements ICommand {
	private static final String INFO = "GET /tops/ratings/lower/average - returns the detail for the movie with the lower average rating.";
	private final String TITLE = "Movie with lowest average rating";

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws SQLException {
		try(Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(getQuery());

			ResultInfo result = createRI(rs);

			stmt.close();

			return result;
		}
	}

	@Override
	public String getInfo() {
		return INFO;
	}

	private String getQuery() {
		return "SELECT TOP 1 Movie.*, ((one * 1. + two * 2 + three * 3 + four * 4 + five * 5) / nullif((one + two + three + four + five),0)) as rating FROM Movie\n" +
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
				"ORDER BY rating";
	}

	private ResultInfo createRI(ResultSet rs) throws SQLException {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Title");
		columns.add("Release Year");
		columns.add("Rating");

		ArrayList<ArrayList<String>> data = new ArrayList<>();

		if (!rs.next()) return new ResultInfo(TITLE, columns, data);

		ArrayList<String> line = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rs.getDate("release_year"));

		line.add(rs.getString("title"));
		line.add(Integer.toString(calendar.get(Calendar.YEAR)));
		line.add(String.format(Locale.FRENCH,"%.2f", rs.getFloat("rating")));

		data.add(line);

		return new ResultInfo(TITLE, columns, data);
	}

}
