package structures;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.HtmlTreeNotCreatedException;
import Strutures.HtmlResult;
import Strutures.ResultInfo;

//TODO: improve tests
public class HtmlResultTest {
	public static HtmlResult hr;
	
	@BeforeClass
	public static void createHtmlResultInstance(){
		hr = new HtmlResult();
	}
	
	@Test
	public void shouldGetExceptionIfHasNoTree(){	
		try {
			hr.writeToFile("HtmlOut.html");
			fail();
		} catch (Exception e) {
			// Exception expected
			if(!(e instanceof HtmlTreeNotCreatedException))
				fail();
		}	
	}	
	
	@Test
	public void saveHtmlToFile() throws Exception{	
		hr.display(createResultInfo(4,4));		
		hr.writeToFile("HtmlOut.html");		
	}
	
	@Test
	public void saveHtmlToFileNodata() throws Exception{
		hr.display(createResultInfo(0,0));		
		hr.writeToFile("HtmlOut.html");		
	}
		
	private ResultInfo createResultInfo(int r, int c){
		int rows = r;
		int cols = c;
		 ArrayList<String> columns = new ArrayList<>();
		 ArrayList<ArrayList<String>> data = new ArrayList<>();
		 
		 for(int i = 0; i<rows;i++){
			 columns.add("Column Title "+(i+1));
			 ArrayList<String> line = new ArrayList<String>();
			 for(int j = 0; j < cols ;j++){
				 line.add("Data "+j);
			 }
			 data.add(line);
		 }
		 return new ResultInfo("Teste",columns,data);
		
	}

}
