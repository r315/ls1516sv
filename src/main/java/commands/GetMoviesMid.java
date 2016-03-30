package commands;

import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.List;


public class GetMoviesMid implements Command {
    @Override
    public void execute(List<Object> args, ConnectionFactory cf) throws SQLException {
        Connection conn = cf.getConn();

        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Movie WHERE movie_id = ?");
        pstmt.setInt(1,(int)args.get(0));

        ResultSet rs = pstmt.executeQuery();

        printRS(rs);

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
