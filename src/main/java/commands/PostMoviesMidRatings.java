package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import exceptions.InvalidCommandParametersException;
import utils.Utils;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PostMoviesMidRatings implements ICommand {
	private static final String TITLE = "Movie inserted with ID: ";
	private static final String INFO = "POST /movies/{mid}/ratings - submits a new rating for the movie identified by mid, given the parameters \"rating\"";

	/*
		POST /movies/{mid}/ratings - submits a new rating for the movie identified by mid, given the following parameters
		rating - integer between 1 and 5.
	*/

	// TODO: Rollback

	@Override
	public ResultInfo execute(HashMap<String, String> data) throws SQLException, InvalidCommandParametersException {
		ResultInfo ri = null;
		try(Connection conn = ConnectionFactory.getConn())
		{
			String rID;
			int mID;
			try{
				rID= getRating(data.get("rating"));
				mID = Utils.getInt(data.get("mid"));
			}catch(NumberFormatException| NullPointerException e){
				throw new InvalidCommandParametersException();
			}

			PreparedStatement pstmt = conn.prepareStatement(getQuery(rID),PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,mID);
			int res = pstmt.executeUpdate();

			if(res != 0)
				ri = createResultInfo(pstmt.getGeneratedKeys());

			pstmt.close();

		}		
		return ri;
	}

	@Override
	public String getInfo() {
		return INFO;
	}	

	private String getQuery(String rID){
		return 	"update Rating " +
				"set " + rID + "+=1 " +
				"where movie_id=?";
	}

	private static String getRating(String r){
		//r="1" -> "one"
		HashMap<String, String> m=new HashMap<String,String>();
		m.put("1","one");
		m.put("2","two");
		m.put("3","three");
		m.put("4","four");
		m.put("5","five");
		return m.get(r);
	}
	
	//this could be on ResultInfo
	private ResultInfo createResultInfo(ResultSet rs) throws SQLException{
		ArrayList<String> columns = new ArrayList<>();
		columns.add("Rating ID");
		ArrayList<ArrayList<String>> rdata = new ArrayList<>();
		while(rs.next()) {
			ArrayList<String> line = new ArrayList<String>();
			line.add(Integer.toString(rs.getInt(1)));
			rdata.add(line);
		}
		return new ResultInfo(TITLE,columns,rdata);
	}

}
