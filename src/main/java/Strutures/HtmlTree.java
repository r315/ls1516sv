package Strutures;


public class HtmlTree {
	
	protected HtmlElement root;

	public String getHtml(){
		return "<!DOCTYPE html>\n" + root.getHtml(0);  
	}

	public HtmlTree(){
		root = new HtmlElement("html");
		root.addChild(new HtmlElement("head"));
		root.addChild(new HtmlElement("body"));
	}

	public void addElementTo(String elm, HtmlElement htmlElement) {
		HtmlElement element = root.findNode(elm);
		element.addChild(htmlElement);
	}
}
