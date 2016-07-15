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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GetTopsNReviewsHigherCount implements ICommand {
	private static final String INFO = "GET /tops/{n}/reviews/higher/count - returns a list with the n movies with higher review count.";
	private final String TITLE = " Movies with higher review count"; //Add n before

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
		) {
			int n = Utils.getInt(data.get("n"));
			if (n < 0) throw new InvalidCommandVariableException();

			n -= skip;
			if (n < 1) n=1;

			pstmt.setInt(1, skip);
			pstmt.setInt(2, n);

			ResultSet rs = pstmt.executeQuery();

			return createRI(rs, n, TITLE);

		} catch (NumberFormatException e) {
			throw new InvalidCommandVariableException();
		}

	}

	@Override
	public String getInfo() {
		return INFO;
	}

	private String getQuery(Boolean topB, int top) {
		String query = "SELECT Movie.movie_id, Movie.title, Movie.release_year, COUNT(Review.rating) AS revcount " +
				"FROM Movie " +
				"LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
				"GROUP BY Movie.title, Movie.release_year, Movie.movie_id " +
				"ORDER BY revcount DESC " +
				"OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		if (topB) {
			query += ") AS tudo ORDER BY revcount DESC OFFSET 0 ROWS FETCH NEXT " + top + " ROWS ONLY";
			query = "SELECT * FROM ( " + query;
		}
		return query;
	}

	static ResultInfo createRI(ResultSet rs, int n, String title) throws SQLException {
		ArrayList<String> columns = new ArrayList<>();
		columns.add("ID");
		columns.add("Title");
		columns.add("Release Year");
		columns.add("Review Count");

		ArrayList<ArrayList<String>> data = new ArrayList<>();

		while(rs.next()) {
			ArrayList<String> line = new ArrayList<>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("release_year"));

			line.add(rs.getString("movie_id"));
			line.add(rs.getString("title"));
			line.add(Integer.toString(calendar.get(Calendar.YEAR)));
			line.add(rs.getString("revcount"));

			data.add(line);
		}

		return new ResultInfo(n + title, columns, data);
	}

}
