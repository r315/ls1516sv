package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class JdbcTests {

    @Test
    public void can_connect() throws SQLException {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setDatabaseName("lsdb");
        ds.setUser("ls-user");
        ds.setPassword("changeit");
        Connection conn = ds.getConnection();
        DatabaseMetaData md = conn.getMetaData();
    }
}
