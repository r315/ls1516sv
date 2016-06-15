package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
import exceptions.InvalidCommandParametersException;
import exceptions.PostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pair;
import utils.Utils;

import javax.servlet.ServletException;
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
 * Created by hmr on 28/05/2016.
 */
public class CollectionsCidServlet extends HttpServlet {
    private static final Charset utf8 = Charset.forName("utf-8");
    private static final Logger _logger = LoggerFactory.getLogger(CollectionsCidServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(String.format("text/html; charset=%s", utf8.name()));
        String respBody;
        try {
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();

            _logger.info("New GET was received:" + path + (query == null ? "" : query));

            if (query == null) query = "top=5";
            else if (!query.contains("top=")) query += "&top=5";

            respBody = produceTemplate(new CommandInfo(new String[]{method, path, query}),
                    req.getPathInfo().substring(1),"",query ).getHtml();
            resp.setStatus(200);
        } catch (Exception e) {
            //// TODO: 28/05/2016 better page
            respBody = e.getMessage();
            resp.setStatus(404);

        }
       setResponseData(resp, respBody);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        String respBody = null;
        try {
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(),
                    req.getRequestURI(),
                    String.format("mid=%s",req.getParameter("mid"))});

            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(
                    command,
                    new HeaderInfo(new String[]{}));

            // cid is inserted on command by Manager on path decode
            _logger.info("New POST fulfilled:" + String.format("/collections/%s",command.getData().get("cid")));

            resp.sendRedirect(String.format("/collections/%s",command.getData().get("cid")));
            resp.setStatus(303);
        }catch (InvalidCommandParametersException | PostException e ){
            try {
                String cid = req.getPathInfo().split("/")[1];
                String msg = "";

                if(e instanceof PostException){
                    switch(((PostException)e).getErrorCode()){
                        case PostException.ENTRY_EXISTS:
                            msg = "Movie Already Exists";break;
                        case PostException.ENTRY_NOT_FOUND:
                            msg = "Movie not found on database";break;
                    }
                }else
                    msg = "Invalid ID!";

                respBody = produceTemplate(new CommandInfo(new String[]{"GET",
                                req.getServletPath()+"/"+cid,req.getQueryString()}),
                        cid, msg, req.getQueryString() ).getHtml();
                resp.setStatus(200);
            } catch (Exception e1) {
                respBody = "Error creating error message!";
                resp.setStatus(400);
            }
        }catch (Exception e ) {
            respBody = e.getMessage();
            resp.setStatus(400);
        }
        setResponseData(resp, respBody);
    }

    private void setResponseData(HttpServletResponse resp, String page) throws IOException {
        byte[] respBodyBytes = page.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }

    private CommandInfo commandInfoCid(HttpServletRequest req){
        return  null;
    }

    private HtmlResult produceTemplate(CommandInfo ci, String cid,  String error, String query) throws Exception {

        HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(ci, new HeaderInfo(new String[]{}));

        //Add collections links to each column
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (ArrayList<String> line : resultFormat.resultInfo.getValues())
            pairs.add(new Pair<>(line.get(1), "/movies/" + line.get(0)));

        //Remove Release Year and Movie ID columns
        resultFormat.resultInfo.removeColumn("Release Year");
        resultFormat.resultInfo.removeColumn("Movie ID");

        resultFormat.generate();

        //Set Navigation links
        resultFormat.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home", "/"),
                        new Pair<>("Collections", "/collections")
                )
        );

        resultFormat.addLinksToTable(pairs);

        resultFormat.addPaging(Utils.paging(query, String.format("/collections/%s",cid)));

        resultFormat.addForm("Add Movie to Collection"
                ,Arrays.asList(
                        new Pair<String,String>("method","POST"),
                        new Pair<String,String>("action",String.format("/collections/%s/movies",cid)))
                ,Arrays.asList(
                        new Pair<String,List<Pair<String,String>>>("Movie ID",Arrays.asList(
                                new Pair<String,String>("name","mid"),
                                new Pair<String,String>("type","text"),
                                new Pair<String,String>("required",null)))
                )
        );

        if(error.length() != 0)
            resultFormat.addElementTo("fieldset",
                    new HtmlElement("b").addChild(
                            new HtmlElement("font", error).addAttributes("color", "red"))
                    ,HtmlElement.SECOND_ELEMENT);
        return resultFormat;
    }
}