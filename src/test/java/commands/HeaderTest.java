package commands;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import utils.CommandDecoder;
import console.ComponentSupplier;
import exceptions.InvalidCommandPathException;

public class HeaderTest {
	
	
	@Test
	public void getHeaderWithOneOption() throws InvalidCommandPathException{
		
		ComponentSupplier cs = new ComponentSupplier(new String[] {"GET","/movies","accept:text/plain|accept-language:en-gb"});
		HeaderInfo h = cs.getHeader();
		
		assertNotNull(h);
		
		
	}
	
	@Test
	public void asf() throws InvalidCommandPathException{
		String s = CommandDecoder.getSegment("/movies/123",0);
		
	}

}
