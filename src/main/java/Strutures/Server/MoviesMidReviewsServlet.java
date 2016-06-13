package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
import utils.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Luigi Sekuiya on 28/05/2016.
 */
public class MoviesMidReviewsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("--New request was received --");
        System.out.println(req.getRequestURI());

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody;
        try{
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();
            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{method,path,query});
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);

            produceTemplate(resultFormat, Optional.empty());

            respBody = resultFormat.getHtml();


            //// TODO: 25/05/2016
        }catch(Exception e){
            //// TODO: 19/05/2016
            resp.setStatus(404);
            respBody="Error 404.";
        }
        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(303);

        try {
            String method = req.getMethod();
            String path = req.getRequestURI();

            HeaderInfo headerInfo = new HeaderInfo();
            String params= String.format("reviewerName=%s&rating=%s&reviewSummary=%s&review=%s",req.getParameter("name"),req.getParameter("rating"),req.getParameter("summary"),req.getParameter("review"));

            CommandInfo command = new CommandInfo(method, path,params);
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);

            String mid = command.getResources().stream().collect(Collectors.toList()).get(1);
            String rid = resultFormat.resultInfo.getGeneratedId();
            resp.sendRedirect(String.format("/movies/%d/reviews/%d",Integer.parseInt(mid),Integer.parseInt(rid)));
            resp.setStatus(303);
        }catch (Exception e ){
            // TODO: 31/05/2016
            resp.setStatus(404);
        }

    }

    private void produceTemplate(HtmlResult resultFormat, Optional<String> errorMessage) throws Exception {

        ArrayList<String> values = resultFormat.resultInfo.getValues().iterator().next();
        String mid = values.get(0);
        String movie_name = values.get(1);

        Pair<String,String> pair = new Pair<>(values.get(1),"/movies/"+mid);

        List<Pair<String,String>> pairs = new ArrayList<>();

        for (ArrayList<String> line : resultFormat.resultInfo.getValues()){
            pairs.add(new Pair<>(line.get(5),"/movies/"+line.get(0)+"/reviews/"+line.get(2)));
        }

        resultFormat.resultInfo.removeColumn("Movie's ID");
        resultFormat.resultInfo.removeColumn("Movie's Title");
        resultFormat.resultInfo.removeColumn("Review's ID");
        resultFormat.generate();
        resultFormat.addLinksToTable(pairs);

        resultFormat.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home","/"),
                        pair
                )
        );

        resultFormat.addFormGeneric(
                String.format("Submit a review for movie %s", movie_name)
                ,Arrays.asList(new Pair("method","POST"),new Pair("action",String.format("/movies/%s/reviews",mid)))
                ,Arrays.asList(
                        new HtmlElement("br","Name:"),
                        new HtmlElement("input").addAttributes("type","text").addAttributes("name","name").addAttributes("required",null),

                        new HtmlElement("br","Rating:"),
                        new HtmlElement("select").addAttributes("name","rating").
                                addChild(new HtmlElement("option","1").addAttributes("value","1")).
                                addChild(new HtmlElement("option","2").addAttributes("value","2")).
                                addChild(new HtmlElement("option","3").addAttributes("value","3")).
                                addChild(new HtmlElement("option","4").addAttributes("value","4")).
                                addChild(new HtmlElement("option","5").addAttributes("value","5")),

                        // TODO: 13/06/2016 For reasons unknown, textarea appear with two tabs already filled.

                        new HtmlElement("br","Summary:"),
                        new HtmlElement("textarea").addAttributes("name","summary").addAttributes("rows","3").addAttributes("cols","50").addAttributes("required",null),

                        new HtmlElement("br","Review:"),
                        new HtmlElement("textarea").addAttributes("name","review").addAttributes("rows","10").addAttributes("cols","50").addAttributes("required",null)
                )
        );

    }
}