package structures;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import Strutures.HtmlResult;
import Strutures.ResultInfo;

public class HtmlResultTest {
	public static HtmlResult hr;
	
	@BeforeClass
	public static void createHtmlResultInstance(){
		hr = new HtmlResult();
	}
	
	@Test
	public void createHtml(){		
		hr.display(createResultInfo());
	}
	
	@Test
	public void saveHtmlToFile() throws FileNotFoundException{		
		createHtml();
		hr.writeToFile("HtmlOut.html");		
	}
	
	
	private ResultInfo createResultInfo(){
		
		 ArrayList<String> columns = new ArrayList<>();
		 ArrayList<ArrayList<String>> data = new ArrayList<>();
		 
		 for(int i = 0; i<4;i++){
			 columns.add("Column Title "+(i+1));
			 ArrayList<String> line = new ArrayList<String>();
			 line.add("Line "+i);
			 data.add(line);
		 }
		 return new ResultInfo("Teste",columns,data);
		
	}

}
