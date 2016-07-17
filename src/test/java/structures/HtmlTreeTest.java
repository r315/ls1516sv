package structures;

import java.util.*;

import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlTree;
import org.junit.Test;

import Strutures.ResponseFormat.ResultInfo;
import utils.Pair;

public class HtmlTreeTest {
    public static HtmlTree htmlTree;

    @Test
    public void shouldGetBlankPage() {
        htmlTree = new HtmlTree();
        System.out.println("Blank Page" + htmlTree.getHtml());
    }


    @Test
    public void shouldCreateHthmlForNoResults() {
        htmlTree = new HtmlTree();
        htmlTree.addData(createResultInfo(0, 0));
        System.out.println("Page for no results: \n" +
                htmlTree.getHtml());
    }

    @Test
    public void shouldAddNavigationTable() {
        htmlTree = new HtmlTree();
        htmlTree.addData(createResultInfo(4, 4));
        htmlTree.addNavigationLinks(Arrays.asList(
                new Pair<String, String>("Home", "/home"),
                new Pair<String, String>("forward", "#\" onclick=\"history.go(1);return false;"),
                new Pair<String, String>("back", "#\" onclick=\"history.go(-1);return false;")));
        System.out.println("Page with navigation links \n" + htmlTree.getHtml());
    }

    @Test
    public void shouldAddLinksToResultsTable() {
        htmlTree = new HtmlTree();
        htmlTree.addData(createResultInfo(4, 4));
        List<Pair<String, String>> links = new ArrayList<Pair<String, String>>();
        for (int i = 1; i < 5; i++)
            links.add(new Pair<String, String>("Column Title " + Integer.toString(i), "/home"));
        htmlTree.addLinksToTable(links);
        System.out.println("Html with link on Column Title 1\n" + htmlTree.getHtml());
    }

    @Test
    public void shouldAddList() {
        htmlTree = new HtmlTree();
        htmlTree.addNavigationLinks(Arrays.asList(new Pair("Home", "/home"), new Pair("forward", "#\" onclick=\"history.go(1);return false;"), new Pair("back", "#\" onclick=\"history.go(-1);return false;")));
        htmlTree.addList(Arrays.asList(new Pair("Item 1", "/home"), new Pair("Item 2", "/home"), new Pair("Item 3", "/home")), "List A");
        htmlTree.addList(Arrays.asList(new Pair("Item 1", "/home"), new Pair("Item 2", "/home"), new Pair("Item 3", "/home")), "List B");
        System.out.println("Page with List \n" + htmlTree.getHtml());
    }


    @Test
    public void shouldInsertForm() {
        htmlTree = new HtmlTree();
        htmlTree.addFormGeneric("Legenda Fieldset",

                Arrays.asList(
                        new Pair<String, String>("method", "POST"),
                        new Pair<String, String>("action", "/movies")),

                Arrays.asList(
                        new HtmlElement("br","Movie Name"),
                        inputElement("name"),
                        new HtmlElement("br","Release Year"),
                        inputElement("description"))
        );

        System.out.println("Form insertion \n" + htmlTree.getHtml());
    }


    private HtmlElement inputElement(String name) {
        return new HtmlElement("input")
                .addAttributes("name", name)
                .addAttributes("type", "text")
                .addAttributes("required", null);
    }

    private ResultInfo createResultInfo(int r, int c){
        int rows = r;
        int cols = c;
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        for(int i = 0; i<rows;i++){
            columns.add("Column Title "+(i+1));
            ArrayList<String> line = new ArrayList<String>();
            for(int j = 0; j < cols ;j++){
                line.add("Data "+j);
            }
            data.add(line);
        }
        return new ResultInfo("Teste",columns,data);
    }

}