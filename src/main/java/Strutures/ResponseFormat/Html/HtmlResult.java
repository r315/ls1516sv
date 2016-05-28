package Strutures.ResponseFormat.Html;

import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created hugo reis on 27/04/2016 
 */

public class HtmlResult implements IResultFormat {
	private HtmlTree page;
	public ResultInfo resultInfo;

	public HtmlResult(){ page = new HtmlTree();	}
	public HtmlResult(ResultInfo ri){
		resultInfo=ri;
	}

	
	public String generate() {
		if (resultInfo == null) {
			return getHtml(); //blankPage
		}

		if (resultInfo.getValues().isEmpty())
			return emptyPage();

		page = new HtmlTree();
		page.addElementTo("head", HtmlElement.title(resultInfo.getDisplayTitle())); // Tab Title

		HtmlElement table = createTable();

		if (!resultInfo.getTitles().isEmpty())
			table.addChild(addDataToRow(new HtmlElement("tr"), "th", resultInfo.getTitles()));

		for (ArrayList<String> line : resultInfo.getValues()) {
			table.addChild(addDataToRow(new HtmlElement("tr"), "td", line));
		}

		page.addElementTo("body", HtmlElement.heading("h2", resultInfo.getDisplayTitle()));
		page.addElementTo("body", table);
		return page.getHtml();
	}

	private HtmlElement addDataToRow(HtmlElement row, String tag, Collection<String> vals) {
		for (String s : vals)
			row.addChild(new HtmlElement(tag, s));
		return row;
	}

	private HtmlElement createTable(){
		HtmlElement table = new HtmlElement("table");
		table.addAttributes("style=\"width:100%\"");
		return table;
	}

	public void addLink(String cont, String link) {
		page.addLinkToContent(cont,link);
	}


	public void addLinksToTable(List<Pair<String,String>> lines) {
		for(Pair<String,String> p : lines) {
			page.addLinkToContent(p.value1,p.value2);
		}
	}

	public void addList(List<Pair<String,String>> items){
		HtmlElement list = new HtmlElement("ul");

		for(Pair<String,String> item: items){
			HtmlElement line = new HtmlElement("li", item.value1);
			line.addLink(item.value2);
			list.addChild(line);
		}
		page.addElementTo("body",list);
	}

	public void addNavigationLinks(List<Pair<String,String>> cols){

		HtmlElement table = createTable();

		for(Pair<String,String> p : cols) {
			HtmlElement col = new HtmlElement("th", p.value1);
			col.addLink(p.value2);
			table.addChild(col);
		}

		page.addElementTo("body",table,0);//first element on body
		page.addElementTo("body",HtmlElement.heading("p",""),1); // second element on body
	}

	public String getHtml(){
		return page.getHtml();
	}

	private String emptyPage(){
		page = new HtmlTree();
		page.addElementTo("head", HtmlElement.title("Not Found!")); // Tab Title
		//TODO ad title
		page.addElementTo("body", HtmlElement.heading("h2","Add Title Here"));
		page.addElementTo("body", HtmlElement.heading("h4","No results found!"));
		return page.getHtml();
	}
}

/*
<!DOCTYPE html>
<html>
	<head>
		<title> Titulo tab browser </title> 		
		<style> table, th, td { border: 1px solid black; border-collapse: collapse;} </style>
	</head>
	<body>
		<h2> Titulo Tabela </h2>
		<table style="width:100%">
			<tr>
				<th>Titulo</th>
				<th>Ano de Lancamento</th>		
				<th>Review</th>
			</tr>
		   	<tr>
				<td>Jill</td>
				<td>Smith</td>		
				<td>50</td>
		  	</tr>
		  	<tr>
				<td>Eve</td>
				<td>Jackson</td>		
				<td>94</td>
		  	</tr>
		  	<tr>
				<td>John</td>
				<td>Doe</td>		
				<td>80</td>
		  	</tr>
		</table>
	</body>
</html>
*/


