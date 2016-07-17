package Strutures.ResponseFormat.Html;

import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class HtmlTree {

    private HtmlElement root;
    public static HtmlElement title(String text){
        return new HtmlElement("title",text);
    }
    public static HtmlElement style(String style){
        return new HtmlElement("style",style);
    }
    public static HtmlElement heading(String h, String text){
        return new HtmlElement(h,text);
    }
    public static HtmlElement div(String id){
        return new HtmlElement("div").addAttributes("id",id);
    }
    public static HtmlElement p() {return new HtmlElement("p");}
    public static HtmlElement tr() {return new HtmlElement("tr");}

    public HtmlTree(){
        root = new HtmlElement("html")
                .addChild(new HtmlElement("head")
                        .addChild(style("table, th, td { border: 1px solid black; border-collapse: collapse;}")))
                .addChild(new HtmlElement("body")
                        .addChild(div("header"))
                        .addChild(div("data"))
                        .addChild(div("footer")));
    }

    private static HtmlElement findElement(HtmlElement root, String key, BiPredicate<String,HtmlElement> pred){
        if(pred.test(key,root))
            return root;
        for(HtmlElement child : root.getChilds()){
            HtmlElement next = findElement(child, key, pred);
            if( next != null)
                return next;
        }
        return null;
    }

    private HtmlElement findElementByTag(String ttag){
        return findElement(root, ttag, (k, r) -> r.getTag().equals(k));
    }

    private HtmlElement findByContent(String cont) {
        return findElement(root, cont, (k, r) -> r.getContent().equals(k));
    }

    private HtmlElement findById(String id) { //used to search div element
        return findElement(root, id, (k, r) -> r.getAttributes().contains("id=" + "\"" + k + "\""));
    }

    private HtmlElement addElementTo(String tag, Consumer<HtmlElement> cons) {
        HtmlElement element = findElementByTag(tag);
        if(element != null)
            cons.accept(element);
        return  element;
    }

    private HtmlElement addElementTo(String tag, HtmlElement htmlElement) {
        return addElementTo(tag, e -> e.addChild(htmlElement));
    }

    private HtmlElement addElementTo(String tag, HtmlElement htmlElement, int pos) {
        return addElementTo(tag, e -> e.addChild(htmlElement,pos));
    }

    private HtmlElement addElementToDiv(String div, HtmlElement elem) {
        return findById(div).addChild(elem);
    }

    private HtmlElement addLinkToContent(String content, String link){
        HtmlElement elm = findByContent(content);
        if(elm != null)
            elm.addLink(link);
        return elm;
    }

    private static HtmlElement htmlElementWithAtr(String tag, List<Pair<String,String>> atrs){
        HtmlElement elm = new HtmlElement(tag);
        atrs.forEach( p -> elm.addAttributes(p.value1, p.value2) );
        return elm;
    }

    private static HtmlElement createTable(){
        HtmlElement table = new HtmlElement("table")
                .addAttributes("width", "100%")
                .addAttributes("border", "1")
                .addAttributes("style", "border-collapse:collapse;");
        return table;
    }

    private static HtmlElement createFormGeneric(String legend,
                                                 List<Pair<String,String>> formAtributes,
                                                 List<HtmlElement> inputs){
        HtmlElement form = htmlElementWithAtr("form",formAtributes);
        HtmlElement fieldset = new HtmlElement("fieldset")
                .addAttributes("style","width:300px;")
                .addChild(new HtmlElement("legend", legend));

        inputs.forEach( p -> fieldset.addChild(p));

        fieldset.addChild(new HtmlElement("input")
                        .addAttributes("type","submit")
                        .addAttributes("value","Submit")
        );
        form.addChild(fieldset);
        return form;
    }

    private static HtmlElement createList(List<Pair<String,String>> items){
        HtmlElement list = new HtmlElement("ul");
        for(Pair<String,String> item: items){
            HtmlElement line = new HtmlElement("li", item.value1);
            line.addLink(item.value2);
            list.addChild(line);
        }
        return list;
    }

    private static HtmlElement createNavigationLinks(List<Pair<String, String>> cols){
        HtmlElement table = createTable();
        for(Pair<String,String> p : cols) {
            HtmlElement col = new HtmlElement("th", p.value1);
            col.addLink(p.value2);
            table.addChild(col);
        }
        return table;
    }

    private static HtmlElement createPaging ( HashMap<String, String> paging ) {
        String next,prev;
        prev = paging.get("prev");
        prev = prev != null ? "<a href = \"" + paging.get("prev") + "\" >PREV</a>" : "PREV";
        next = paging.get("next");
        next = next != null ? "<a href = \"" + paging.get("next") + "\" >NEXT</a>" :  "NEXT";
        HtmlElement p = new HtmlElement("p",prev + "&nbsp;&nbsp;&nbsp;" + next).addAttributes("align","right");
        return p;
    }

    public String getHtml(){
        StringBuilder sb = new StringBuilder("<!DOCTYPE html>\n");
        getHtml(sb,root, 0);
        return  sb.toString();
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

    private void noResults(String title){
        addElementTo("head", HtmlTree.title("Not Found!")); // Tab Title
        addElementTo("body", heading("h2", title));
        addElementTo("body", heading("h4", "No results found!"));
    }

    private HtmlElement addDataToRow(HtmlElement row, String tag, Collection<String> vals) {
        for (String s : vals)
            row.addChild(new HtmlElement(tag, s));
        return row;
    }

    public void addData(ResultInfo resultInfo) {
        if(!resultInfo.generateresult) return;

        if (resultInfo.getValues().isEmpty()){
            noResults(resultInfo.getDisplayTitle());
            return;
        }

        addElementTo("head", HtmlTree.title(resultInfo.getDisplayTitle())); // Tab Title

        HtmlElement table = createTable();

        if (!resultInfo.getTitles().isEmpty())
            table.addChild(addDataToRow(tr(), "th", resultInfo.getTitles()));

        for (ArrayList<String> line : resultInfo.getValues()) {
            table.addChild(addDataToRow(tr(), "td", line));
        }

        addElementToDiv("data", heading("h2",resultInfo.getDisplayTitle()));
        addElementToDiv("data", table);
    }

    public void addLinksToTable(List<Pair<String,String>> lines) {
        for(Pair<String,String> p : lines) {
            addLinkToContent(p.value1, p.value2);
        }
    }

    public void addList(List<Pair<String,String>> items,String title){
        addElementTo("body", heading("h3",title));
        addElementTo("body", createList(items));
    }

    public void addNavigationLinks(List<Pair<String,String>> cols){
        addElementToDiv("header", createNavigationLinks(cols));
        addElementToDiv("header", p());
    }

    public void addFormGeneric(String legend, List<Pair<String,String>> formAtributes, List<HtmlElement> inputs) {
        addElementTo("body", p());
        addElementTo("body", p());
        addElementTo("body", createFormGeneric(legend, formAtributes, inputs));
    }

    public void addPaging(HashMap<String, String> paging) {
        addElementTo("body", p());
        addElementTo("body",createPaging(paging));
    }

    public void addFooter(HtmlElement data){
        addElementToDiv("footer", data);
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
