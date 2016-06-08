package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
import exceptions.PostException;
import utils.Pair;

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

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //System.out.println("--New request was received --");
        //System.out.println(req.getRequestURI());
        resp.setContentType(String.format("text/html; charset=%s", utf8.name()));
        String respBody = null;
        try {
            respBody = produceTemplate(new CommandInfo(new String[]{req.getMethod(), req.getRequestURI()}),
                    req.getPathInfo().substring(1),"" ).getHtml();
            resp.setStatus(200);
        } catch (Exception e) {
            //// TODO: 28/05/2016 better page
            resp.setStatus(404);
            respBody = "Error 404.";
        }
       setResponseData(resp, respBody);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Charset utf8 = Charset.forName("utf-8");
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
            resp.sendRedirect(String.format("/collections/%s",command.getData().get("cid")));
            resp.setStatus(303);
        }catch (PostException e ){
            try {
                String cid = req.getPathInfo().split("/")[1];
                respBody = produceTemplate(new CommandInfo(new String[]{"GET",
                                req.getServletPath()+"/"+cid}),
                        cid, e.getMessage() ).getHtml();
            } catch (Exception e1) {
                respBody = "Error 400.";
                resp.setStatus(400);
            }
            resp.setStatus(200);
        }catch (Exception e ) {
            respBody = "Error 400.";
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

    private HtmlResult produceTemplate(CommandInfo ci, String cid,  String error) throws Exception {

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