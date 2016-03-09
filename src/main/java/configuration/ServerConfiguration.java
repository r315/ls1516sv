package configuration;

import java.util.Properties;

public class ServerConfiguration {
	public static final String SERVER_TYPE_MYSQL = "mysql";
	public static final String SERVER_TYPE_SQLSERVER = "sqlserver";
	public String name;
	public String type;
	public String user;
	public String pass;
	public String server;
	public String port;
	public String database;
	
	public ServerConfiguration(Properties prop){
		name=prop.getProperty("name");
		type=prop.getProperty("type");
		user=prop.getProperty("user");
		server=prop.getProperty("server");
		user=prop.getProperty("user");
		pass=prop.getProperty("pass");	
		database = prop.getProperty("database");
		port=prop.getProperty("port");
		
	}
}
