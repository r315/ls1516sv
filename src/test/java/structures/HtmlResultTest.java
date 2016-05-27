package structures;


import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;


public class HtmlResultTest {
	public static HtmlResult hr;
	public static Map<String,String> hmap;
	@BeforeClass
	public static void createHtmlResultInstance(){
		hmap = new HashMap<String,String>();
		hmap.put("file-name","HtmlOut.html");
	}

	@Test
	public void shoudNotThrowExceptions()
	{
		hr = new HtmlResult(new ResultInfo(null,null,null));
		hr.generate();
	}

	@Test
	public void shouldPrintSimpleHtmlOnConsole()
	{
		hr = new HtmlResult(createResultInfo(4, 4));
		System.out.println("Page with 4 result lines \n" + hr.generate()
		);
	}

	@Test
	public void shouldCreateHthmlForNoResults()
	{
		hr = new HtmlResult(createResultInfo(0, 0));
		System.out.println("Page for no results \n" +
                        hr.generate());
	}

	@Test
	public void shouldAddLink()
	{
		hr = new HtmlResult(createResultInfo(4, 4));
		String s = hr.generate();
		hr.addLink("Column Title 1","/home");
		System.out.println("Html with link on Column Title 1\n" + hr.getHtml());
	}

	@Test
	public void shouldAddNavigationTable()
	{
		hr = new HtmlResult(createResultInfo(4, 4));
		hr.generate();
        hr.addNavigationLinks(Arrays.asList( new Pair("Home","/home")));
		System.out.println("Page with navigation links \n" + hr.getHtml());
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

