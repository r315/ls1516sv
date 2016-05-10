package structures;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import Strutures.HtmlResult;
import Strutures.ResultInfo;

//TODO: improve tests
public class HtmlResultTest {
	public static HtmlResult hr;
	public static Map<String,String> hmap;
	@BeforeClass
	public static void createHtmlResultInstance(){
		hr = new HtmlResult();
		hmap = new HashMap<String,String>();
		hmap.put("file-name","HtmlOut.html");
	}
	
	@Test
	public void shoudNotThrowExceptions()
	{
		hr.display(null, null);		
	}
	
	@Test
	public void shoudPrintOnConsole()
	{		
		hr.display(createResultInfo(4,4),null);		
		
	}	
	
	@Test
	public void shoudWriteToFile()
	{		
		hr.display(createResultInfo(4,4),hmap);		
		//TODO: check file
	}
	@Test
	public void shoudPrintToConsoleAndWriteToFile()
	{	
		hmap.put("accept","text/html");
		hr.display(createResultInfo(4,4),hmap);		
		
	}	
	
	@Test
	public void shouldSaveHtmlToFileNodata() 
	{
		hr.display(createResultInfo(0,0),hmap);		
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
