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

public class GetTopsNReviewsHigherCount implements ICommand {
	public static final String INFO = "GET /tops/{n}/reviews/higher/count - returns a list with the n movies with higher review count.";
	private final String TITLE = " Movies with higher review count"; //Add n before

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
		String query = "SELECT Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount " +
				"FROM Movie " +
				"LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
				"GROUP BY Movie.title, Movie.release_year " +
				"ORDER BY revcount DESC " +
				"OFFSET ? ROWS";
		if (topB) query += " FETCH NEXT " + top + " ROWS ONLY";
		return query;
	}

	private ResultInfo createRI(ResultSet rs, int n) throws SQLException {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Title");
		columns.add("Release Year");
		columns.add("Review Count");

		ArrayList<ArrayList<String>> data = new ArrayList<>();

		while(rs.next()) {
			ArrayList<String> line = new ArrayList<>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("release_year"));

			line.add(rs.getString("title"));
			line.add(Integer.toString(calendar.get(Calendar.YEAR)));
			line.add(rs.getString("revcount"));

			data.add(line);
		}

		return new ResultInfo(n + TITLE, columns, data);
	}

}
