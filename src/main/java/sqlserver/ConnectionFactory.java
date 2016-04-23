package sqlserver;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Map;

public class ConnectionFactory {

    static SQLServerDataSource ds = null;

    static private void createDataSource() throws SQLException {
        ds = new SQLServerDataSource();
        Map<String,String> env=System.getenv();
        ds.setServerName(env.get("LS_SERVER"));
        ds.setUser(env.get("LS_USER"));
        ds.setPassword(env.get("LS_PASS"));
        try{
        	ds.setPortNumber(Integer.parseInt(env.get("LS_PORT")));
		}catch (NumberFormatException ne){
			//use default port
			ds.setPortNumber(1433);
		}        
        ds.setDatabaseName(env.get("LS_DBNAME"));
    }

    static public Connection getConn () throws SQLException {
        if (ds == null) createDataSource();

        return ds.getConnection();
    }

    static public void closeConn (Connection conn) throws SQLException {
        conn.close();
    }

}


