package sqlserver;

import java.sql.Connection;
import java.sql.DriverManager;

import configuration.Configuration;
import configuration.ServerConfiguration;

public class ConnectionFactory {
	private static Connection conn = null;
	private static ServerConfiguration serverConfiguration;
	
	//"jdbc:mysql://<server>/<database>?user=<username>&password=<password>"
	private void mysqlConnection() throws Exception{
		String url;			
		url = "jdbc:mysql://" + serverConfiguration.server;			
		if (serverConfiguration.port != null && serverConfiguration.port.length() > 0) 
			url += ":" + serverConfiguration.port;
		url += "/";
		if (serverConfiguration.database != null && serverConfiguration.database.length() > 0)
			url += serverConfiguration.database + "?";
		url += "user=" + serverConfiguration.user + "&password=" + serverConfiguration.pass;	
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(url);
			//conn.setAutoCommit(false);
		}catch(Exception ex){
			conn = null;
			throw new Exception("ConnectionFactory: fail to create mysql connection");
		}	
	}

	private void sqlserverConnection() throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("ConnectionFactory: sqlserver not implemented");
	}
	
	private static ConnectionFactory instance = new ConnectionFactory();
	public ConnectionFactory getInstance(){ 
		return instance;
	}
	
	public Connection getConnection(){
			return conn;
	}
	
	private ConnectionFactory(){		
		try{
		if(Configuration.getInstance().getDataBase().type == ServerConfiguration.SERVER_TYPE_MYSQL)
			mysqlConnection();
		else
			sqlserverConnection();
		}catch (Exception ex){
			//TODO: log this
			System.out.println(ex.toString());
		}
	}		
}
