package commands;

import Strutures.ICommand;
import Strutures.ResultInfo;
import exceptions.InvalidCommandVariableException;
import utils.Utils;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.HashMap;


public class GetMoviesMid implements ICommand {

    @Override
    public ResultInfo execute(HashMap<String, String> data) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID;

            try {
                mID = Utils.getInt(data.get("mID"));
            } catch (NumberFormatException e) {
                throw new InvalidCommandVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);

            ResultSet rs = pstmt.executeQuery();

            printRS(rs);

            pstmt.close();
        }

        //Builderino stuff
        ResultInfo stuff = new ResultInfo();
        return stuff;
    }

    @Override
    public String getInfo() {
        return "GET /movies/{mid} - returns the detailed information for the movie identified by mid.";
    }

    private String getQuery() {
        return "SELECT * FROM Movie WHERE movie_id = ?";
    }

    private void printRS(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnNumber = rsMetaData.getColumnCount();
        String columnName[] = new String[columnNumber];
        for (int i = 0; i < columnNumber; i++) {
            columnName[i] = rsMetaData.getColumnName(i + 1);
        }
        while (rs.next()) {
            for (int i = 0; i < columnNumber; i++) {
                if (i > 0)
                    System.out.print("&");
                System.out.print(columnName[i] + "=" + rs.getObject(i + 1));
            }
            System.out.println();
        }

    }

}
