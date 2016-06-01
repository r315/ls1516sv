package Strutures.ResponseFormat.Html;

import Strutures.ResponseFormat.Html.HtmlTree;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.ResultInfo;
import pt.isel.ls.html.HtmlElem;
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

        if(!resultInfo.generateresult)
            return "";

		if (resultInfo.getValues().isEmpty())
			return emptyPage(resultInfo.getDisplayTitle());

		page = new HtmlTree();
		page.addElementTo("head", HtmlTree.title(resultInfo.getDisplayTitle())); // Tab Title

		HtmlElement table = HtmlTree.createTable();

		if (!resultInfo.getTitles().isEmpty())
			table.addChild(addDataToRow(new HtmlElement("tr"), "th", resultInfo.getTitles()));

		for (ArrayList<String> line : resultInfo.getValues()) {
			table.addChild(addDataToRow(new HtmlElement("tr"), "td", line));
		}

		page.addElementTo("body", HtmlTree.heading("h2", resultInfo.getDisplayTitle()));
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

	public void addLinksToTable(List<Pair<String,String>> lines) {
		for(Pair<String,String> p : lines) {
			page.addLinkToContent(p.value1,p.value2);
		}
	}

	public String getHtml(){
        return page.getHtml();
	}

	private String emptyPage(String title){
		page = new HtmlTree();
		page.addElementTo("head", HtmlTree.title("Not Found!")); // Tab Title
		page.addElementTo("body", HtmlTree.heading("h2",title));
		page.addElementTo("body", HtmlTree.heading("h4","No results found!"));
		return page.getHtml();
	}

    public void addList(List<Pair<String,String>> items,String title){
        page.addElementTo("body",new HtmlElement("h3",title));
        page.addElementTo("body",page.addList(items));
    }

    public void addNavigationLinks(List<Pair<String,String>> cols){
        page.addElementToDiv("header", HtmlTree.addNavigationLinks(cols));
        page.addElementToDiv("header",HtmlTree.p());
    }

    public void addForm(String legend, List<Pair> formAtributes, List<Pair> inputs){
        page.addElementTo("body", HtmlTree.p());
        page.addElementTo("body", HtmlTree.p());
        page.addElementTo("body", HtmlTree.addForm(legend, formAtributes, inputs));
    }

    public void addElementTo(String tag, HtmlElement elem){
        page.addElementTo(tag,elem);
    }
    
    public void addElementTo(String tag, HtmlElement elem, int pos){
        page.addElementTo(tag,elem,pos);
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


