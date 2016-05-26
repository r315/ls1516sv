package Strutures;


import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class HtmlTree {
	
	protected HtmlElement root;

	public String getHtml(){
		StringBuilder sb = new StringBuilder("<!DOCTYPE html>\n");
		getHtml(sb,root, 0);
		return  sb.toString();
	}

	public HtmlTree(){
		root = new HtmlElement("html");
		root.addChild(new HtmlElement("head"));
		root.addChild(new HtmlElement("body"));
	}

	private void addElementTo(String elm, HtmlElement htmlElement, Consumer<HtmlElement> cons) {
		HtmlElement element = findElementByTag(elm);
		if(element != null)
			cons.accept(element);
	}
	public void addElementTo(String elm, HtmlElement htmlElement) {
		addElementTo(elm,htmlElement,e -> e.addChild(htmlElement));
	}

	public void addElementTo(String elm, HtmlElement htmlElement, int pos) {
		addElementTo(elm,htmlElement,e -> e.addChild(htmlElement,pos));
	}

	public void addLinkToContent(String content, String link){
		HtmlElement elm = findByContent(content);
		if(elm != null)
			elm.addLink(link);
	}

	public HtmlElement findElementByTag(String ttag){
		return findElement(root, ttag, (k, r) -> r.getTag().equals(k));
	}

	public HtmlElement findByContent(String cont) {
		return findElement(root, cont, (k, r) -> r.getContent().equals(k));
	}

	private HtmlElement findElement(HtmlElement root, String key, BiPredicate<String,HtmlElement> pred){
		if(pred.test(key,root))
			return root;
		for(HtmlElement child : root.getChilds()){
			HtmlElement next = findElement(child, key, pred);
			if( next != null)
				return next;
		}
		return null;
	}

	private void getHtml(StringBuilder sb, HtmlElement root, int level){
		StringBuilder tabs = new StringBuilder("\n");
		String content = root.getContent();
		String tag = root.getTag();
		String attributes = root.getAttributes();

		for(int i = 0; i < level; i++) {
			tabs.append("\t");
		}

		sb.append(tabs + "<" + tag + attributes + ">");

		for(HtmlElement child : root.getChilds()){
			getHtml(sb, child, level + 1);
		}

		sb.append(content.length()!=0?content:tabs);
		sb.append("</" + tag + ">");
	}
}
