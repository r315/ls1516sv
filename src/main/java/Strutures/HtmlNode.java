package Strutures;

import java.util.ArrayList;
import java.util.Collection;


class HtmlNode{
	private String tag = "";
	private String content =""; 
	private String attributes = "";
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

	public void addAttributes(String attributes){
		this.attributes = " " + attributes;
	}

	public void addChild(HtmlNode node){
		childs.add(node);
	}

	public String getHtml(int level){    		
		StringBuffer sb = new StringBuffer();
		StringBuffer tabs = new StringBuffer("\n");

		for(int i = 0; i < level; i++) tabs.append("\t");    		

		sb.append(tabs + "<" + tag + attributes + ">");

		for(HtmlNode child : childs){
			sb.append(child.getHtml(level+1));
		}

		if(content.length()!=0)
			sb.append(content + "</" + tag + ">");
		else
			sb.append(tabs + "</" + tag + ">");

		return  sb.toString();
	}

	public HtmlNode findNode(String tag){
		if(this.tag == tag) return this;
		for(HtmlNode child : childs){
			HtmlNode next = child.findNode(tag);
			if( next != null)
				return next;
		}     		
		return null;
	}
}     