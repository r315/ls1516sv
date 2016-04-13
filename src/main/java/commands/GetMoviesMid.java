package commands;

import exceptions.CommandWrongVariableException;
import sqlserver.ConnectionFactory;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class GetMoviesMid implements ICommand {

    @Override
    public void execute(Collection<String> args, HashMap<String, String> prmts) throws Exception {
        try(Connection conn = ConnectionFactory.getConn()) {
            int mID;
            Iterator<String> it = args.iterator();
            it.next();
            try {
                mID = Integer.parseInt(it.next());
            } catch (NumberFormatException e) {
                throw new CommandWrongVariableException();
            }

            PreparedStatement pstmt = conn.prepareStatement(getQuery());
            pstmt.setInt(1, mID);

            ResultSet rs = pstmt.executeQuery();

            printRS(rs);

            pstmt.close();
        }
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
