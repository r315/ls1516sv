package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;


public class GetMoviesMid implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws SQLException {
        int mID = Integer.parseInt(args.iterator().next());

        Connection conn = ConnectionFactory.getConn();

        PreparedStatement pstmt = conn.prepareStatement(getQuery());
        pstmt.setInt(1,mID);

        ResultSet rs = pstmt.executeQuery();

        printRS(rs);

        pstmt.close();
        ConnectionFactory.closeConn();
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
