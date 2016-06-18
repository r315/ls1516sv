package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
import exceptions.InvalidCommandException;
import exceptions.PostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pair;
import utils.Utils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MoviesServlet extends HttpServlet {
    private static final Logger _logger = LoggerFactory.getLogger(MoviesServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody=null;
        try{
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();

            _logger.info("New GET was received:" + path + (query == null ? "" : query));

            if (query == null)
                query = "top=5";
            else if (!query.contains("top="))
                query += "&top=5";

            HeaderInfo headerInfo = new HeaderInfo();
            CommandInfo command = new CommandInfo(new String[]{method,path,query});
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);
            produceTemplate(resultFormat, query, Optional.empty());
            respBody = resultFormat.getHtml();
        }catch(InvalidCommandException e){
            _logger.error(e.getMessage());
            resp.setStatus(404);
            respBody="Error 404.";
        }catch(SQLException e){
            _logger.error(e.getMessage());
            resp.setStatus(500);
            respBody="Error 500.";
        }
        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String respBody=null;
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        try {
            String method = req.getMethod();
            String path = req.getRequestURI();
            HeaderInfo headerInfo = new HeaderInfo();
            String params= String.format("title=%s&releaseYear=%s",req.getParameter("title"),req.getParameter("releaseYear"));
            CommandInfo command = new CommandInfo(method, path,params);
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);
            String ID= resultFormat.resultInfo.getGeneratedId();

            _logger.info("New POST fulfilled:" + String.format("/movies/%d",Integer.parseInt(ID)));

            resp.setStatus(303);
            resp.sendRedirect(String.format("/movies/%d",Integer.parseInt(ID)));
        }catch (PostException e){
            resp.setStatus(200);
            try{
                String method="GET";
                String path= req.getRequestURI();
                String query= req.getQueryString();
                HeaderInfo headerInfo = new HeaderInfo();
                CommandInfo command = new CommandInfo(method,path,query);
                HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);

                if (query == null)
                    query = "top=5";
                else if (!query.contains("top="))
                    query += "&top=5";

                produceTemplate(resultFormat, query, Optional.ofNullable(e.getMessage()));
                respBody= resultFormat.getHtml();
            }catch(InvalidCommandException e1){
                _logger.error(e1.getMessage());
                resp.setStatus(404);
                respBody="Error 404.";
            }catch(SQLException e1){
                _logger.error(e1.getMessage());
                resp.setStatus(500);
                respBody="Error 500.";
            }
        }catch(InvalidCommandException e){
            _logger.error(e.getMessage());
            resp.setStatus(404);
            respBody="Error 404.";
        }catch(SQLException e){
            _logger.error(e.getMessage());
            resp.setStatus(500);
            respBody="Error 500.";
        }

        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }

    private void produceTemplate(HtmlResult resultFormat, String query, Optional<String> errorMessage) {
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

        resultFormat.addPaging(Utils.paging(query, "/movies"));

        resultFormat.addForm("Insert a new Movie"
                ,Arrays.asList(new Pair("method","POST"),new Pair("action",String.format("/movies?%s",query)))
                ,Arrays.asList(
                        new Pair("Movie title",Arrays.asList(new Pair("name","title"),new Pair("type","text"),new Pair("required",null))),
                        new Pair("Release Year",Arrays.asList(new Pair("name","releaseYear"),new Pair("type","text"),new Pair("required",null)))
                )
        );

        if(errorMessage.isPresent()){
            resultFormat.addElementTo("fieldset"
                    ,new HtmlElement("b").
                            addChild(new HtmlElement("font",errorMessage.get())
                                    .addAttributes("color","red"))
                    ,1
            );
        }
    }
}