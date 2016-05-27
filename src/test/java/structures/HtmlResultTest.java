package structures;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.ResultInfo;
//// TODO: 27/05/2016
/*

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
		hr.generate(null, null);
		hr.generate(new ResultInfo(null,null,null), null);
	}

	@Test
	public void shouldPrintSimpleHtmlOnConsole()
	{
		System.out.println(
				hr.generate(createResultInfo(4, 4), new HashMap<String, String>())
		);
	}

	@Test
	public void shouldCreateHthmlForNoResults()
	{
		System.out.println(
			hr.generate(createResultInfo(0, 0), new HashMap<String, String>())
		);
	}

	@Test
	public void shouldAddLink()
	{
		String s = hr.generate(createResultInfo(4,4),new HashMap<String,String>());
		hr.addLink("Column Title 1","/home");
		System.out.println(hr.getHtml());
	}

	@Test
	public void shouldAddNavigationTable()
	{
		hr.generate(createResultInfo(4,4),new HashMap<String,String>());
		hr.addNavigationLinks("Home","/home");
		System.out.println(hr.getHtml());
	}

	@Test
	public void shoudWriteToFile()
	{
		//TODO: check write to file
		//hr.generate(createResultInfo(4,4),hmap);
		//File file = new File("HtmlOut.html");
		//assertTrue(file.isFile());
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
*/