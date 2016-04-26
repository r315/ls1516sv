package Strutures;

import java.util.Collection;

/**
 * Created by Red on 24/04/2016.
 */
public class HtmlResult implements IResult {
	

    //TODO
    public void display(ResultInfo resultInfo){
    	HtmlNode root = new HtmlNode("html");
    	
    	HtmlTree.insert(root,new HtmlNode("head"));
    	
    	String t = root.getHtml();

    }
    
    
      
    public void writeToFile(String filename){
    	
    }
    
    
    
    
    
    class HtmlNode{
    	String tag = "";
    	String content ="";
    	HtmlNode next = null;
    	
    	public HtmlNode(String tag){
    		this.tag = tag;
    	}
    	
    	public void addContent(String content){
    		this.content = content;
    	}
    	
    	public String getHtml(){
    		return "<" + tag +">" + (next != null? next.getHtml():"") + content + "</" + tag + ">";
    	}
    } 
    
    static class HtmlTree{
    	
    	public static void insert(HtmlNode root, HtmlNode node){
    		if(root.next == null){
    			root.next = node;
    			return;
    		}    		
    		insert(root.next,node);
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

