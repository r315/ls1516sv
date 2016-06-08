package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
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
        try {
            String method = req.getMethod();
            String path = req.getRequestURI();
            String query= req.getQueryString();
            HeaderInfo headerInfo = new HeaderInfo();
            String params= String.format("title=%s&releaseYear=%s",req.getParameter("title"),req.getParameter("releaseYear"));
            CommandInfo command = new CommandInfo(method, path,params);
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);
            String ID= resultFormat.resultInfo.getGeneratedId();
            resp.sendRedirect(String.format("/movies/%d",Integer.parseInt(ID)));
            resp.setStatus(303);
        }catch (Exception e){
            Charset utf8 = Charset.forName("utf-8");
            resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
            resp.setStatus(200);
            String respBody=null;
            try{
                String method="GET";
                String path= req.getRequestURI();
                String query= req.getQueryString();
                HeaderInfo headerInfo = new HeaderInfo();
                CommandInfo command = new CommandInfo(method,path,query);
                HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);
                ProduceTemplate(resultFormat, Optional.ofNullable(e.getMessage()));
                respBody= resultFormat.getHtml();
            }catch (Exception c){
                resp.setStatus(400);
                respBody="Error 400.";
            }
            byte[] respBodyBytes = respBody.getBytes(utf8);
            resp.setContentLength(respBodyBytes.length);
            OutputStream os = resp.getOutputStream();
            os.write(respBodyBytes);
            os.close();
        }
    }

    private void ProduceTemplate(HtmlResult resultFormat, Optional<String> errorMessage) throws Exception {
        ArrayList<String> values = resultFormat.resultInfo.getValues().iterator().next();

        Pair<String,String> pair = new Pair<>(values.get(1),"/movies/"+values.get(0));

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

        resultFormat.addForm(
                String.format("Submit a rating to movie %s", values.get(0))
                ,Arrays.asList(new Pair("method","POST"),new Pair("action",String.format("/movies/%s/ratings",values.get(0))))
                ,Arrays.asList(
                        new Pair("Movie title",Arrays.asList(new Pair("name","title"),new Pair("type","text"),new Pair("required",null))),
                        new Pair("Release Year",Arrays.asList(new Pair("name","releaseYear"),new Pair("type","text"),new Pair("required",null)))
                )
        );
    }
}