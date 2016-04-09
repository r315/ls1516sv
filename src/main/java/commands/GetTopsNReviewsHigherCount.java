package commands;

import exceptions.CommandWrongVariableException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class GetTopsNReviewsHigherCount implements ICommand {

	@Override
	public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception {
		int n;
		Iterator<String> it = args.iterator();
		it.next();
		try {
			n = Integer.parseInt(it.next());
		} catch (NumberFormatException e) {
			throw new CommandWrongVariableException();
		}

		Connection conn = ConnectionFactory.getConn();

		PreparedStatement pstmt = conn.prepareStatement(getQuery());
		pstmt.setInt(1,n);

		ResultSet rs = pstmt.executeQuery();

		printRS(rs);

		pstmt.close();
		ConnectionFactory.closeConn();
	}

	private String getQuery() {
		return "SELECT TOP (?) title, release_year, COALESCE (ratcount + revcount, ratcount, revcount) AS total " +
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

			System.out.println(rs.getString("title") + " (" + calendar.get(Calendar.YEAR) + "): " + rs.getInt("total"));
		}

	}

}
