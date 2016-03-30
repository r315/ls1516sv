package sqlserver;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Map;

public class ConnectionFactory {

    static public Connection getConn () throws SQLException {
        SQLServerDataSource ds=new SQLServerDataSource();
        Map<String,String> env=System.getenv();
        ds.setServerName(env.get("LS_SERVER"));
        ds.setUser(env.get("LS_USER"));
        ds.setPassword(env.get("LS_PASS"));
        ds.setPortNumber(Integer.parseInt(env.get("LS_PORT")));
        ds.setDatabaseName(env.get("LS_DBNAME"));

        return ds.getConnection();
    }

    static public void closeConn (Connection conn) throws SQLException {
        conn.close();
    }

    static public void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnNumber = rsMetaData.getColumnCount();
        String columnName[] = new String[columnNumber];
        for (int i = 0; i < columnNumber; i++) {
            columnName[i] = rsMetaData.getColumnName(i + 1);
        }
        while (rs.next()) {
            for (int i = 0; i < columnNumber; i++) {
                if (i > 0)
                    System.out.print(", ");
                System.out.print(columnName[i] + " " + rs.getObject(i + 1));
            }
            System.out.println();
        }
    }
}


