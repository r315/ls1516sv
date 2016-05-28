package structures;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;


public class HtmlResultTest {
	public static HtmlResult hr;

	@Test
	public void shouldNotThrowExceptions()
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
	public void shouldAddLinksToResultsTable()
	{
		hr = new HtmlResult(createResultInfo(4, 4));
		List<Pair<String,String>> links = new ArrayList<Pair<String,String>>();
		for(int i = 1; i<5;i++)
			links.add(new Pair( "Column Title "+ Integer.toString(i), "/home"));
		String s = hr.generate();
		hr.addLinksToTable(links);
		System.out.println("Html with link on Column Title 1\n" + hr.getHtml());
	}

	@Test
	public void shouldAddNavigationTable()
	{
		hr = new HtmlResult(createResultInfo(4, 4));
		hr.generate();
        hr.addNavigationLinks(Arrays.asList( new Pair("Home","/home"),new Pair("forward","#\" onclick=\"history.go(1);return false;"),new Pair("back","#\" onclick=\"history.go(-1);return false;")));
		System.out.println("Page with navigation links \n" + hr.getHtml());
	}

	@Test
	public void shouldAddList()
	{
		hr = new HtmlResult();
		hr.generate();
		hr.addNavigationLinks(Arrays.asList( new Pair("Home","/home"),new Pair("forward","#\" onclick=\"history.go(1);return false;"),new Pair("back","#\" onclick=\"history.go(-1);return false;")));
		hr.addList(Arrays.asList(new Pair("Item 1", "/home"), new Pair("Item 2", "/home"), new Pair("Item 3", "/home")),"List A");
		hr.addList(Arrays.asList(new Pair("Item 1", "/home"), new Pair("Item 2", "/home"), new Pair("Item 3", "/home")),"List B");
		System.out.println("Page with List \n" + hr.getHtml());
	}

	@Test
	public void shouldGetBlankPage(){
		hr = new HtmlResult();
		System.out.println("Blank Page" + hr.getHtml());
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

