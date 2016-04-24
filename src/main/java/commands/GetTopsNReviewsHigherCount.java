package commands;

import Strutures.Result;
import exceptions.InvalidCommandVariableException;
import pt.isel.ls.Utils;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

public class GetTopsNReviewsHigherCount implements ICommand {

	@Override
	public Result execute(HashMap<String, String> data) throws Exception {
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
		Result stuff = new Result();
		return stuff;
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
