package commands;

import exceptions.CommandWrongVariableException;
import exceptions.InvalidCommandParameters;
import exceptions.InvalidCommandPathException;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class PostMoviesMidRatings implements ICommand {

	/*
		POST /movies/{mid}/ratings - submits a new rating for the movie identified by mid, given the following parameters
		rating - integer between 1 and 5.
	*/

	@Override
	public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception {
		try(Connection conn = ConnectionFactory.getConn())
		{
			String rID= getRating(prmts.get("rating"));
			if(rID==null)
				throw new InvalidCommandParameters();

			Iterator<String> it= args.iterator();
			it.next();
			int mID;
			try {
				mID = Integer.parseInt(it.next());
			} catch (NumberFormatException e) {
				throw new CommandWrongVariableException();
			}

			PreparedStatement pstmt = conn.prepareStatement(getQuery(rID),PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,mID);
			int res = pstmt.executeUpdate();

			if(res != 0){
				ResultSet rs = pstmt.getGeneratedKeys();
				printRS(rs);
			}
			pstmt.close();

		}

	}

	private void printRS(ResultSet rs) throws SQLException {
		while(rs.next()) {
			System.out.println("Review inserted with ID: "+ rs.getInt(1));
		}
	}

	private String getQuery(String rID){
		return 	"update Rating " +
				"set " + rID + "+=1 " +
				"where movie_id=?";
	}

	private String getRating(String r){
		//r="1" -> "one"
		HashMap<String, String> m=new HashMap<String,String>();
		m.put("1","one");
		m.put("2","two");
		m.put("3","three");
		m.put("4","four");
		m.put("5","five");
		return m.get(r);
	}

}
