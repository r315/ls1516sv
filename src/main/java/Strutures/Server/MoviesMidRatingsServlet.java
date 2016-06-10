package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.Html.HtmlTree;
import console.Manager;
import utils.Pair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Luigi Sekuiya on 28/05/2016.
 */
public class MoviesMidRatingsServlet extends HttpServlet {
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
            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{method,path});
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);
            ProduceTemplate(resultFormat, Optional.empty());
            respBody = resultFormat.getHtml();
        }catch(Exception e){
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
        String respBody;

        try {
            String method = req.getMethod();
            String path = req.getRequestURI();

            HeaderInfo headerInfo = new HeaderInfo();
            String params= String.format("rating=%s",req.getParameter("rating"));
            CommandInfo command = new CommandInfo(method, path,params);
            //HtmlResult resultFormat = (HtmlResult)
            Manager.executeCommand(command, headerInfo);
            String ID= command.getResources().stream().collect(Collectors.toList()).get(1);
            resp.sendRedirect(String.format("/movies/%d/ratings",Integer.parseInt(ID)));
            resp.setStatus(303);
        }catch (Exception e){
            resp.setStatus(400);
            respBody="Error 400."+e.getMessage();
            byte[] respBodyBytes = respBody.getBytes(utf8);
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();
        }
    }

    private void ProduceTemplate(HtmlResult resultFormat, Optional<String> errorMessage) throws Exception {
        ArrayList<String> values = resultFormat.resultInfo.getValues().iterator().next();
        String movie_id=values.get(0);
        String movie_name=values.get(1);

        Pair<String,String> pair = new Pair<>(movie_name,"/movies/"+movie_id);

        resultFormat.resultInfo.removeColumn("ID");
        resultFormat.resultInfo.removeColumn("Titulo");
        resultFormat.generate();

        resultFormat.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home","/"),
                        pair
                )
        );

        resultFormat.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Sort by Date","/movies?sortBy=addedDate"),
                        new Pair<>("Sort by Date Desc","/movies?sortBy=addedDateDesc"),
                        new Pair<>("Sort by Year","/movies?sortBy=year"),
                        new Pair<>("Sort by Year Desc","/movies?sortBy=yearDesc"),
                        new Pair<>("Sort by Title","/movies?sortBy=title"),
                        new Pair<>("Sort by Title Desc","/movies?sortBy=titleDesc"),
                        new Pair<>("Sort by Rating","/movies?sortBy=rating"),
                        new Pair<>("Sort by Rating Desc","/movies?sortBy=ratingDesc")
                )
        );

        resultFormat.addFormGeneric(
                String.format("Submit a rating to movie %s", movie_name)
                ,Arrays.asList(new Pair("method","POST"),new Pair("action",String.format("/movies/%s/ratings",movie_id)))
                ,Arrays.asList(
                        new HtmlElement("p","Rating:"),
                        new HtmlElement("select").addAttributes("name","rating").
                                addChild(new HtmlElement("option","1").addAttributes("value","1")).
                                addChild(new HtmlElement("option","2").addAttributes("value","2")).
                                addChild(new HtmlElement("option","3").addAttributes("value","3")).
                                addChild(new HtmlElement("option","4").addAttributes("value","4")).
                                addChild(new HtmlElement("option","5").addAttributes("value","5"))
                )
        );
    }
}