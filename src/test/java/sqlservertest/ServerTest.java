package sqlservertest;

import com.microsoft.sqlserver.jdbc.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class ServerTest {

    Connection conn=null;
    ResultSet rs=null;
    Map<Integer,String> datamap=null;

    @Before
    public void getConnectionAndCreateTable()throws SQLException {
    	try{
    		SQLServerDataSource ds=new SQLServerDataSource();
    		Map<String,String> env=System.getenv();
    		ds.setServerName(env.get("LS_SERVER"));
    		ds.setUser(env.get("LS_USER"));
    		ds.setPassword(env.get("LS_PASS"));
    		ds.setPortNumber(Integer.parseInt(env.get("LS_PORT")));
    		ds.setDatabaseName(env.get("LS_DBNAME"));
    		conn=ds.getConnection();
    		Statement stmt= conn.createStatement();
    		stmt.executeUpdate(
    				"create table Test(" +
    						"number int primary key," +
    						"name varchar (100));"
    				);
    		stmt.close();
    	}catch (SQLException ex){
    		throw new SQLException("Error getting SQLServer connection");
    	}

        datamap= new HashMap<Integer, String>(3);
        datamap.put(38241,"João Duarte");   datamap.put(36187,"Luís Almeida");  datamap.put(38652,"Hugo Reis");

        PreparedStatement pstmt= conn.prepareStatement(
                "insert into Test values(?,?);"
        );
        for (int key:datamap.keySet()){
            pstmt.setInt(1,key);
            pstmt.setString(2,datamap.get(key));
            pstmt.executeUpdate();
        }
    }

    @After
    public void CloseTests() throws SQLException{
        Statement stmt= conn.createStatement();
        stmt.executeUpdate(
                "DROP TABLE Test;"
        );
        stmt.close();
        conn.close();
    }

    @Test
    public void nColsTest() throws SQLException{
        Statement stmt= conn.createStatement();
        rs= stmt.executeQuery(
                "select * from Test"
        );
        ResultSetMetaData rsMetaData = rs.getMetaData();
        Assert.assertEquals(2,rsMetaData.getColumnCount());
        stmt.close();
    }

    @Test
    public void NumbersTest() throws SQLException{
        PreparedStatement pstmt= conn.prepareStatement(
                "select name from Test where number=?"
        );
        for (int key:datamap.keySet()){
            pstmt.setInt(1,key);
            rs=pstmt.executeQuery();
            rs.next();
            Assert.assertEquals(datamap.get(key), rs.getObject(1));
        }
        pstmt.close();
    }

}
