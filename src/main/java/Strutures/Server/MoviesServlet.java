package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
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

/**
 * Created by Luigi Sekuiya on 28/05/2016.
 */
public class MoviesServlet extends HttpServlet {
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

            List<Pair<String,String>> pairs = new ArrayList<>();

            for (ArrayList<String> line : resultFormat.resultInfo.getValues()){
                pairs.add(new Pair<>(line.get(1),"/movies/"+line.get(0)));
            }

            resultFormat.resultInfo.removeColumn("ID");
            resultFormat.generate();
            resultFormat.addLinksToTable(pairs);

            resultFormat.addNavigationLinks(
                    Arrays.asList(
                            new Pair<>("Home","/"),
                            new Pair<>("Movies","/movies"),
                            new Pair<>("Top Ratings","/tops/ratings")
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



            resultFormat.addForm("Legend"
                    ,Arrays.asList(new Pair("method","POST"),new Pair("action","/movies"))
                    ,Arrays.asList(
                            new Pair("Movie title",Arrays.asList(new Pair("name","title"),new Pair("type","text"),new Pair("required",null))),
                            new Pair("Release Year",Arrays.asList(new Pair("name","releaseYear"),new Pair("type","text"),new Pair("required",null)))
                    )
            );
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getParameterMap().forEach((k,v)-> {
            System.out.print("key: "+k);
            for (String s: v)
                System.out.println("Value: "+s);
        });

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(303);

        try {
            String method = req.getMethod();
            String path = req.getRequestURI();
            String query = req.getQueryString();
            Map<String, String[]> m= req.getParameterMap();
            //resp.getHeaderNames().forEach(s-> System.out.println(s));
            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            //System.out.println(m.get("title")[0]);
            //System.out.println(m.get("releaseYear")[0]);
            String params= String.format("title=%s&releaseYear=%s",m.get("title")[0],m.get("releaseYear")[0]);
            CommandInfo command = new CommandInfo(new String[]{method, path,params});
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);
            String ID= resultFormat.resultInfo.getValues().iterator().next().get(0);
            resp.sendRedirect(String.format("/movies/%d",Integer.parseInt(ID)));
        }catch (Exception e ){
            // TODO: 31/05/2016
            resp.setStatus(404);
        }

    }
}