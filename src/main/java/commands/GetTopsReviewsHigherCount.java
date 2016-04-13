package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

public class GetTopsReviewsHigherCount implements ICommand {

	@Override
	public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
		try(Connection conn = ConnectionFactory.getConn()) {
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(getQuery());

			printRS(rs);

			stmt.close();
		}
	}

	private String getQuery() {
		return "SELECT TOP 1 title, release_year, COALESCE (ratcount + revcount, ratcount, revcount) AS total " +
				"FROM " +
				"(" +
				"SELECT Movie.title, Movie.release_year, ((Rating.one + Rating.two + Rating.three + Rating.four + Rating.five)) AS ratcount, COUNT(Review.rating) AS revcount " +
				"FROM Movie " +
				"LEFT JOIN Rating ON Movie.movie_id = Rating.movie_id " +
				"LEFT JOIN Review ON Review.movie_id = Movie.movie_id " +
				"GROUP BY Movie.title, Movie.release_year, Rating.one, Rating.two, Rating.three, Rating.four, Rating.five " +
				") AS total " +
				"ORDER BY total DESC";
	}

	private void printRS(ResultSet rs) throws SQLException {
		while(rs.next()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(rs.getDate("release_year"));

			System.out.println(rs.getString("title") + " (" + calendar.get(Calendar.YEAR) + ")");
		}

	}

}
