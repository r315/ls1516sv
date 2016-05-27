package Strutures.ResponseFormat.Html;

import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.ResultInfo;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created hugo reis on 27/04/2016 
 */

public class HtmlResult implements IResultFormat {
		private HtmlTree page;


	private ResultInfo resultInfo;


	public HtmlResult(ResultInfo ri){
		resultInfo=ri;
	}
	
	public String generate() {
		if (resultInfo == null) {
			return null;
		}

		if (resultInfo.getValues().isEmpty())
			return emptyPage();

		page = new HtmlTree();
		page.addElementTo("head", HtmlElement.title(resultInfo.getDisplayTitle())); // Tab Title
		page.addElementTo("head",HtmlElement.style("table, th, td { border: 1px solid black; border-collapse: collapse;}"));

		HtmlElement table = new HtmlElement("table");
		table.addAttributes("style=\"width:100%\"");

		if (!resultInfo.getTitles().isEmpty())
			table.addChild(addDataToRow(new HtmlElement("tr"), "th", resultInfo.getTitles()));

		for (ArrayList<String> line : resultInfo.getValues()) {
			table.addChild(addDataToRow(new HtmlElement("tr"), "td", line));
		}

		//TODO: Get Table name
		page.addElementTo("body", HtmlElement.heading("h2", "Table Name"));
		page.addElementTo("body", table);
		return page.getHtml();
	}

	private HtmlElement addDataToRow(HtmlElement row, String tag, Collection<String> vals) {
		for (String s : vals)
			row.addChild(new HtmlElement(tag, s));
		return row;
	}

	public void addLink(String cont, String link) {
		page.addLinkToContent(cont,link);
	}

	//TODO add list of links
	public void addLinksToTable() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	//TODO discuss parameters
	public void addNavigationLinks(String title, String link){
		HtmlElement table = new HtmlElement("table");
		table.addAttributes("style=\"width:100%\"");
		//add columns to table heading, title for now
		table.addChild(new HtmlElement("th", title));
		page.addElementTo("body",table,1);//first element on body
		page.addElementTo("body",HtmlElement.heading("p",""),2); // second element on body
		page.addLinkToContent(title,link);
	}

	public String getHtml(){
		return page.getHtml();
	}

	private String emptyPage(){
		page = new HtmlTree();
		page.addElementTo("head", HtmlElement.title("Not Found!")); // Tab Title
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


