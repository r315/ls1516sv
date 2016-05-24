package Strutures;


public class HtmlTree {
	
	protected static HtmlNode root;

	public static HtmlNode title(String text){		 
		return new HtmlNode("title",text);
	}
	public static HtmlNode h1(String text){
		return new HtmlNode("h1",text);
	}
	public static HtmlNode h2(String text){
		return new HtmlNode("h2",text);
	}

	public static void addTitle(String tabtitle){
		HtmlNode titleNode = root.findNode("head");
		titleNode.addChild(title(tabtitle));
	}

	public static String getHtml(){

		return "<!DOCTYPE html>\n" + root.getHtml(0);  
	}

	public HtmlTree(){
		root = new HtmlNode("html");
		HtmlNode head = new HtmlNode("head");		   
		head.addChild(new HtmlNode("style","table, th, td { border: 1px solid black; border-collapse: collapse;}"));
		root.addChild(head);
		root.addChild(new HtmlNode("body"));
	}	   
}
