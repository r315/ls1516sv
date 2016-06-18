package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;
import exceptions.InvalidCommandException;
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
import java.sql.SQLException;
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
    private static final String ERROR_TAG = "Exception";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(String.format("text/html; charset=%s", utf8.name()));
        String respBody;
        try {
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();

            _logger.info("New GET was received:" + path + (query == null ? "" : query));

            respBody = produceTemplate(new String[]{method, path, query},
                    req.getPathInfo().substring(1),"",query ).getHtml();

            resp.setStatus(200);
        } catch (Exception e) {
            respBody = e.getMessage();
            resp.setStatus(404);
            _logger.error(ERROR_TAG, respBody);
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
                    String.format("mid=%s",
                            req.getParameter("mid"))});

            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(
                    command,
                    new HeaderInfo(new String[]{}));

            // cid is inserted on command by Manager on path decode
            _logger.info("New POST fulfilled:" + String.format("/collections/%s",command.getData().get("cid")));

            resp.sendRedirect(String.format("/collections/%s",command.getData().get("cid")));
            resp.setStatus(303);
        }catch (PostException e ){
            try {
                String cid = req.getPathInfo().substring(1);
                String msg = "";

                if(e instanceof PostException)
                    msg = e.getMessage();
                else
                    msg = "Invalid ID!";

                respBody = produceTemplate(new String[]{"GET",
                                req.getServletPath()+"/"+cid,req.getQueryString()},
                        cid, msg, req.getQueryString() ).getHtml();
                resp.setStatus(200);
            } catch (Exception e1) {
                respBody = e.getMessage();
                resp.setStatus(500);
                _logger.error(ERROR_TAG, respBody);
            }
        } catch (SQLException e) {
            //TODO For RED catch exceptions
        } catch (InvalidCommandException e) {
            //TODO For RED catch exceptions
        }
        /*catch (Exception e ) {
            respBody = e.getMessage();
            resp.setStatus(500);
            _logger.error(ERROR_TAG, respBody);
        }*/
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

    private HtmlResult produceTemplate(String [] param, String cid,  String error, String query){

        if (query == null) query = "top=5";
        else if (!query.contains("top=")) query += "&top=5";

        HtmlResult resultFormat = doQuerytoDataBase(param,query);

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
                        new Pair<String,String>("action",String.format("/collections/%s",cid)))
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

    private HtmlResult doQuerytoDataBase(String[] param, String query){
        HtmlResult resultFormat;

        try {
            resultFormat = (HtmlResult) Manager.executeCommand(new CommandInfo(param[0],param[1],query), new HeaderInfo(new String[]{}));
        } catch (InvalidCommandException | SQLException e) {
            _logger.error(ERROR_TAG, e.getMessage());
            resultFormat = new HtmlResult(new ResultInfo(false));
        }
        return  resultFormat;
    }
}