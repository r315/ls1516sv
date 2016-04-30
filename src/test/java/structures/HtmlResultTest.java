package structures;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

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
	public void createHtml(){		
		hr.display(createResultInfo());
	}
	
	@Test
	public void saveHtmlToFile() throws Exception{		
		createHtml();
		hr.writeToFile("HtmlOut.html");		
	}
		
	private ResultInfo createResultInfo(){
		int rows = 4;
		int cols = 4;
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
