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

    public static void createCollection(boolean addMovie) throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Collection (name,description) VALUES ('TestColection','Collections for testing')");
            stmt.executeUpdate("INSERT INTO Movie (title,release_year) VALUES ('Star Wars IV','19770525')");
            if(addMovie)
                stmt.executeUpdate("INSERT INTO Has VALUES (1,1)");
            stmt.close();
        }
    }

    public static void deleteCollections() throws SQLException {
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Has WHERE collection_id=1");
            stmt.executeUpdate("DELETE FROM Movie WHERE movie_id=1");
            stmt.executeUpdate("DELETE FROM Collection WHERE collection_id=1");
            stmt.executeUpdate("DBCC CHECKIDENT (Collection, RESEED, 0)");
            stmt.executeUpdate("DBCC CHECKIDENT (Movie, RESEED, 0)");
            stmt.close();
        }
    }

    public static void removeTestMovie(){
        try (Connection conn = ConnectionFactory.getConn()) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM Movie WHERE title=TestMovie and release_year=2016");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Fail to remove test movie: " + e.getMessage());
        }
    }

}
