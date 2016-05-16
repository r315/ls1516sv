package commands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sqlserver.ConnectionFactory;
import Strutures.ICommand;
import Strutures.ResultInfo;

public class GetTopsReviewsHigherCount implements ICommand {
	private static final String INFO = "GET /tops/reviews/higher/count - returns the detail for the movie with most reviews.";
	private final String TITLE = "Movie with most reviews";

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
		return "SELECT TOP 1 Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount " +
				"FROM Movie " +
				"LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
				"GROUP BY Movie.title, Movie.release_year " +
				"ORDER BY revcount DESC, Movie.title";
	}

	private ResultInfo createRI(ResultSet rs) throws SQLException {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Title");
		columns.add("Release Year");
		columns.add("Review Count");

		ArrayList<ArrayList<String>> data = new ArrayList<>();

		// TODO: mid doesn't exist
		rs.next();
		ArrayList<String> line = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rs.getDate("release_year"));

		line.add(rs.getString("title"));
		line.add(Integer.toString(calendar.get(Calendar.YEAR)));
		line.add(rs.getString("revcount"));

		data.add(line);

		return new ResultInfo(TITLE, columns, data);
	}

}
