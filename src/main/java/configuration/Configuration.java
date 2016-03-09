package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
	private static final String configFile = "./src/main/java/configuration/mysqlconfig.properties";
	
	private ServerConfiguration dataBase;
	
	public ServerConfiguration getDataBase(){
		return dataBase;
	}
	
	private static Configuration instance = new Configuration();
	
	public static Configuration getInstance(){
		return instance;
	}	
	
	private Configuration(){
		InputStream stream = null;
		try{
			Properties prop = new Properties();
			stream = new FileInputStream(configFile);
			
			prop.load(stream);
			dataBase = new ServerConfiguration(prop); 
			stream.close();
		}catch(Exception e){
			//TODO: Create Logger
			System.out.println("Configuration: " + e);
		}
	}
	

}
