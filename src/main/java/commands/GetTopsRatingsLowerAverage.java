package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GetTopsRatingsLowerAverage implements ICommand {
	public static final String INFO = "GET /tops/ratings/lower/average - returns the detail for the movie with the lower average rating.";
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
		return "SELECT TOP 1 title, release_year, COALESCE((one + [1]), one, [1]) as one, COALESCE((two + [2]), two, [2]) as two, COALESCE((three + [3]), three, [3]) as three, COALESCE((four + [4]), four, [4]) as four, COALESCE((five + [5]), five, [5]) as five " +
				"FROM " +
				"(" +
				"SELECT Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5] " +
				"FROM Movie " +
				"LEFT JOIN Rating ON Movie.movie_id = Rating.movie_id " +
				"LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
				"LEFT JOIN ( " +
				"SELECT movie_id, [1], [2], [3], [4], [5] " +
				"FROM " +
				"(SELECT movie_id, rating FROM Review GROUP BY rating, movie_ID) AS SourceTable " +
				"PIVOT " +
				"( " +
				"COUNT(SourceTable.rating) " +
				"FOR rating IN ([1], [2], [3], [4], [5]) " +
				") AS SourceTable) AS reviewRatings ON reviewRatings.movie_id = Movie.movie_id " +
				"WHERE Movie.movie_id = ? " +
				"GROUP BY Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five, [1], [2], [3], [4], [5] " +
				") AS average " +
				"ORDER BY average";
	}

	private ResultInfo createRI(ResultSet rs) throws SQLException {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Title");
		columns.add("Release Year");
		columns.add("Rating");

		ArrayList<ArrayList<String>> data = new ArrayList<>();

		rs.next();
		ArrayList<String> line = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rs.getDate("release_year"));

		Float average = (float) (rs.getInt("one") + rs.getInt("two") * 2 + rs.getInt("three") * 3 + rs.getInt("four") * 4 + rs.getInt("five") * 5)
				/ (rs.getInt("one") + rs.getInt("two") + rs.getInt("three") + rs.getInt("four") + rs.getInt("five"));

		line.add(rs.getString("title"));
		line.add(Integer.toString(calendar.get(Calendar.YEAR)));
		line.add(String.format("%.2f", average));

		data.add(line);

		return new ResultInfo(TITLE, columns, data);
	}

}
