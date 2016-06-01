package Strutures.ResponseFormat.Html;


import utils.Pair;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class HtmlTree {
	
	protected HtmlElement root;

	public static HtmlElement title(String text){
		return new HtmlElement("title",text);
	}
	public static HtmlElement style(String style){
		return new HtmlElement("style",style);
	}
	public static HtmlElement heading(String h, String text){
		return new HtmlElement(h,text);
	}
	public static HtmlElement form(){
		return new HtmlElement("form");
	}
	public static HtmlElement div(String id){
		return new HtmlElement("div").addAttributes("id",id);
	}
	public static HtmlElement p() {return new HtmlElement("p");}

	public String getHtml(){
		StringBuilder sb = new StringBuilder("<!DOCTYPE html>\n");
		getHtml(sb,root, 0);
		return  sb.toString();
	}

	public HtmlTree(){
		root = new HtmlElement("html")
				.addChild(new HtmlElement("head")
					.addChild(style("table, th, td { border: 1px solid black; border-collapse: collapse;}")))
				.addChild(new HtmlElement("body")
						.addChild(div("header")));
	}

	private void addElementTo(String tag, HtmlElement htmlElement, Consumer<HtmlElement> cons) {
		HtmlElement element = findElementByTag(tag);
		if(element != null)
			cons.accept(element);
	}

	public void addElementTo(String tag, HtmlElement htmlElement) {
		addElementTo(tag,htmlElement,e -> e.addChild(htmlElement));
	}

	public void addElementTo(String tag, HtmlElement htmlElement, int pos) {
		addElementTo(tag,htmlElement,e -> e.addChild(htmlElement,pos));
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

	public HtmlElement findById(String id) { //used to search div element
		return findElement(root, id, (k, r) -> r.getAttributes().contains("id=" + "\"" + k + "\""));
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


	private static HtmlElement htmlElementWithAtr(String tag,List<Pair<String,String>> atrs){
		HtmlElement elm = new HtmlElement(tag);
		atrs.forEach( p -> elm.addAttributes(p.value1, p.value2) );
		return elm;
	}

	public static HtmlElement createTable(){
		HtmlElement table = new HtmlElement("table")
			.addAttributes("width", "100%")
			.addAttributes("border", "1")
			.addAttributes("style", "border-collapse:collapse;");
		return table;
	}

	public static HtmlElement addForm(String legend, List<Pair<String,String>> formAtributes, List<Pair<String,List<Pair<String,String>>>> inputs){

		HtmlElement form = htmlElementWithAtr("form",formAtributes);
		HtmlElement fieldset = new HtmlElement("fieldset")
				.addAttributes("style","width:300px;")
				.addChild(new HtmlElement("legend", legend));

		inputs.forEach( p -> { fieldset
				.addChild(new HtmlElement("br",p.value1))  //form title
				.addChild(htmlElementWithAtr("input", p.value2));
		});

		fieldset.addChild(new HtmlElement("input")
						.addAttributes("type","submit")
						.addAttributes("value","Submit")
		);

		form.addChild(fieldset);
		return form;
	}

	public HtmlElement addList(List<Pair<String,String>> items){
		HtmlElement list = new HtmlElement("ul");
		for(Pair<String,String> item: items){
			HtmlElement line = new HtmlElement("li", item.value1);
			line.addLink(item.value2);
			list.addChild(line);
		}
		return list;
	}

	public static HtmlElement addNavigationLinks(List<Pair<String, String>> cols){
		HtmlElement table = createTable();
		for(Pair<String,String> p : cols) {
			HtmlElement col = new HtmlElement("th", p.value1);
			col.addLink(p.value2);
			table.addChild(col);
		}
		return table;
	}

	public HtmlElement addElementToDiv(String div, HtmlElement elem) {
		return findById(div).addChild(elem);
	}


}
