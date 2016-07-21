package utils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import sqlserver.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by hmr on 21/07/2016.
 */
public class DataBase {
    private static SQLServerDataSource ds = null;

    public static void clear() throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Has");
            stmt.executeUpdate("DELETE FROM Rating");
            stmt.executeUpdate("DELETE FROM Review");
            stmt.executeUpdate("DELETE FROM Collection");
            stmt.executeUpdate("DELETE FROM Movie");
            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.close();
        }
    }
}
