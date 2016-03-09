package sqlservertest;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import configuration.Configuration;
import configuration.ServerConfiguration;

public class ConfigurationTest {
	
	Configuration config=null;
	
	@Before
    public void before(){		
		try{
			config = Configuration.getInstance();
		}catch(Exception e){
			config = null;			
		}		
    }
	
	@Test
	public void TestConfigurationFileLoad(){		
		assertNotNull(config);	
	}
	
	
	@Test
	public void TestDataBaseConfiguration(){
		assertEquals(ServerConfiguration.SERVER_TYPE_MYSQL,config.getDataBase().type);	
	}

}
