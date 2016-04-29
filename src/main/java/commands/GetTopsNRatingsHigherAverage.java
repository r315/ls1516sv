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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GetTopsNRatingsHigherAverage implements ICommand {
	private static final String INFO = "GET /tops/{n}/ratings/higher/average - returns a list with the n movies with higher average ratings, sorted decreasingly.";
	private final String TITLE = " Movies with higher average ratings"; //Add n before

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws Exception {
		Boolean topB = false;
		int skip = 0, top = 1;

		if (data != null) {
			topB = (data.get("top") != null);
			HashMap<String, Integer> skiptop = Utils.getSkipTop(data.get("skip"), data.get("top"));

			skip = skiptop.get("skip");
			top = skiptop.get("top");
		}

		try(Connection conn = ConnectionFactory.getConn()) {
			int n;

			try {
				n = Utils.getInt(data.get("n"));
			} catch (NumberFormatException e) {
				throw new InvalidCommandVariableException();
			}

			PreparedStatement pstmt = conn.prepareStatement(getQuery(topB, top));
			pstmt.setInt(1, n);
			pstmt.setInt(2, skip);

			ResultSet rs = pstmt.executeQuery();

			ResultInfo result = createRI(rs, n);

			pstmt.close();

			return result;
		}

	}

	@Override
	public String getInfo() {
		return INFO;
	}

	private String getQuery(Boolean topB, int top) {
		String query = "SELECT TOP (?) title, release_year, COALESCE((one + [1]), one, [1]) as one, COALESCE((two + [2]), two, [2]) as two, COALESCE((three + [3]), three, [3]) as three, COALESCE((four + [4]), four, [4]) as four, COALESCE((five + [5]), five, [5]) as five " +
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
						"ORDER BY average DESC " +
						"OFFSET ? ROWS";
		if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
		return query;
	}

	private ResultInfo createRI(ResultSet rs, int n) throws SQLException {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Title");
		columns.add("Release Year");
		columns.add("Average Rating");

		ArrayList<ArrayList<String>> data = new ArrayList<>();

		while(rs.next()) {
			ArrayList<String> line = new ArrayList<>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("release_year"));

			Float average = (float) (rs.getInt("one") + rs.getInt("two") * 2 + rs.getInt("three") * 3 + rs.getInt("four") * 4 + rs.getInt("five") * 5)
					/ (rs.getInt("one") + rs.getInt("two") + rs.getInt("three") + rs.getInt("four") + rs.getInt("five"));

			line.add(rs.getString("title"));
			line.add(Integer.toString(calendar.get(Calendar.YEAR)));
			line.add(String.format("%.2f", average));

			data.add(line);
		}

		return new ResultInfo(n + TITLE, columns, data);
	}

}
