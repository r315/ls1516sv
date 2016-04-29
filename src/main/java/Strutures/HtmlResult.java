package Strutures;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Red on 24/04/2016.
 */
public class HtmlResult implements IResult {
	private String html;
	
    public void display(ResultInfo resultInfo){
    	HtmlNode root = new HtmlNode("html");  
    	HtmlNode node = new HtmlNode("head");     	
    	
    	node.addChild(new HtmlNode("title",resultInfo.getDisplayTitle()));
    	node.addChild(new HtmlNode("h2","LS-1516-2"));
    	node.addChild(new HtmlNode("style","table, th, td { border: 1px solid black; border-collapse: collapse;}"));
    	root.addChild(node);
    	
    	HtmlNode table = new HtmlNode("table style=\"width:100%\"");
    	table.addChild(addDataToRow(new HtmlNode("tr"),"th",resultInfo.getTitles()));
    	for(ArrayList<String> line: resultInfo.getValues()){
    		table.addChild(addDataToRow(new HtmlNode("tr"),"td",line));
    	}
    	
    	node = new HtmlNode("body");
    	node.addChild(table);    	
    	root.addChild(node); 
    	
    	html = "<!DOCTYPE html>" + root.getHtml();
    	System.out.println(html);
    }
    
    
    private HtmlNode addDataToRow(HtmlNode row, String tag, Collection<String> vals){    		
    	for(String s : vals){
    		row.addChild(new HtmlNode(tag,s));
    	}  
    	return row;
    }
    
    public void writeToFile(String filename) throws FileNotFoundException{    	
    	try(  PrintWriter file = new PrintWriter(filename)) {
    	    file.println(html);
    	}
    }    
    
    class HtmlNode{
    	private String tag = "";
    	private String content ="";    	
    	private Collection<HtmlNode> childs;
    	
    	public HtmlNode(String tag){
    		this.tag = tag;
    		childs = new ArrayList<HtmlNode>();
    	}
    	
    	public HtmlNode(String tag, String content){
    		this(tag);
    		this.content = content;
    	}
    	
    	public void addContent(String content){
    		this.content = content;
    	}
    	
    	public void addChild(HtmlNode node){
    		childs.add(node);
    	}
    	
    	public String getHtml(){    		
    		StringBuffer sb = new StringBuffer("<" + tag +">");    		
    		for(HtmlNode child : childs){
    			sb.append(child.getHtml());
    		} 
    		sb.append(content + "</" + tag + ">");
    		return  sb.toString();
    	}
    	
    	public HtmlNode findNode(String tag){
    		if(this.tag == tag) return this;
    		for(HtmlNode child : childs){
    			return child.findNode(tag);
    		}     		
    		return null;
    	}
    }     
}


/*
<!DOCTYPE html>
<html>
	<head>
		<title> Titulo tab browser </title> 
		<h2> Titulo pagina </h2>
		<style> table, th, td { border: 1px solid black; border-collapse: collapse;} </style>
	</head>
	<body>
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


