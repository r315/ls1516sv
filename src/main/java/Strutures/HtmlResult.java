package Strutures;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created hugo reis on 27/04/2016 
 */
public class HtmlResult extends HtmlTree implements IResult{
	private static final Object FILENAME_KEY = "file-name";	
	private String html = null;
	
    public void display(ResultInfo resultInfo, Map<String,String> headers){
    	if(resultInfo == null){
    		html = null;
    		return;
    	}
    	
    	addTitle(resultInfo.getDisplayTitle());    	
    	HtmlNode table = new HtmlNode("table");
    	table.addAttributes("style=\"width:100%\"");
    	
    	if(!resultInfo.getTitles().isEmpty())
    		table.addChild(addDataToRow(new HtmlNode("tr"),"th",resultInfo.getTitles()));
    	
    	if(resultInfo.getValues().isEmpty()){
    		ArrayList<String> ndata = new ArrayList<String>();
    		ndata.add("No results found.");
    		table.addChild(addDataToRow(new HtmlNode("tr"),"td",ndata));
    	}else{
    		for(ArrayList<String> line: resultInfo.getValues()){
    			table.addChild(addDataToRow(new HtmlNode("tr"),"td",line));
    		}
    	}
    	
    	//TODO: Get Table name
    	HtmlNode body = root.findNode("body");
    	body.addChild(h2("Table Name"));    	
    	body.addChild(table);
    	
    	html = getHtml();
    	 
    	try {
    		if(headers == null)
    			writeToFile(null);
    		else
    			writeToFile(headers.get(FILENAME_KEY));
    		
		} catch (FileNotFoundException e) {
			// TODO HR: Discuss Exceptions!!
			System.out.println("File name given Not Found!");
			
		}
    }    
    
    private HtmlNode addDataToRow(HtmlNode row, String tag, Collection<String> vals){    		
    	for(String s : vals)
    		row.addChild(new HtmlNode(tag,s)); 
    	return row;
    }
    
    public void writeToFile(String filename) throws FileNotFoundException{ 
    	if(filename == null){
    		System.out.println(html);
    	}else{
    		try(  PrintWriter file = new PrintWriter(filename)) {
    			file.println(html);
    		}
    	}
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


