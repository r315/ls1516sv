package Strutures.ResponseFormat.Html;

import java.util.ArrayList;
import java.util.Collection;


class HtmlElement {
	private String tag = "";
	private String content =""; 
	private String attributes = "";
	private Collection<HtmlElement> childs;

	public static HtmlElement title(String text){
		return new HtmlElement("title",text);
	}
	public static HtmlElement style(String style){
		return new HtmlElement("style",style);
	}
	public static HtmlElement h1(String text){
		return new HtmlElement("h1",text);
	}
	public static HtmlElement h2(String text){
		return new HtmlElement("h2",text);
	}

	public HtmlElement(String tag){
		this.tag = tag;
		childs = new ArrayList<HtmlElement>();
	}

	public HtmlElement(String tag, String content){
		this(tag);
		this.content = content;
	}

	public void addContent(String content){
		this.content = content;
	}

	public void addAttributes(String attributes){
		this.attributes = " " + attributes;
	}

	public void addChild(HtmlElement node){
		childs.add(node);
	}

	public String getHtml(int level){    		
		StringBuffer sb = new StringBuffer();
		StringBuffer tabs = new StringBuffer("\n");

		for(int i = 0; i < level; i++) tabs.append("\t");    		

		sb.append(tabs + "<" + tag + attributes + ">");

		for(HtmlElement child : childs){
			sb.append(child.getHtml(level+1));
		}

		if(content.length()!=0)
			sb.append(content + "</" + tag + ">");
		else
			sb.append(tabs + "</" + tag + ">");

		return  sb.toString();
	}

	public HtmlElement findNode(String tag){
		if(this.tag == tag) return this;
		for(HtmlElement child : childs){
			HtmlElement next = child.findNode(tag);
			if( next != null)
				return next;
		}     		
		return null;
	}
}     