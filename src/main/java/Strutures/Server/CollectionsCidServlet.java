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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hmr on 28/05/2016.
 */
public class CollectionsCidServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //System.out.println("--New request was received --");
        //System.out.println(req.getRequestURI());
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s", utf8.name()));
        resp.setStatus(200);
        String respBody = null;
        try {

            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(), req.getRequestURI()});
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);
            List<String> cresourses = (List<String>)command.getResources();
            String cid = cresourses.get(cresourses.size()-1);

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
                    ,Arrays.asList(new Pair("method","POST"),new Pair("action",String.format("/collections/%s/movies",cid)))
                    ,Arrays.asList(
                            new Pair("Movie ID",Arrays.asList(new Pair("name","name"),new Pair("type","text"),new Pair("required",null)))
                           // new Pair("Description ",Arrays.asList(new Pair("name","description"),new Pair("type","text"),new Pair("required",null)))
                    )
            );

            respBody = resultFormat.getHtml();

        } catch (Exception e) {
            //// TODO: 28/05/2016
            resp.setStatus(404);
            respBody = "Error 404.";
        }

        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));

        try {
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(),
                    req.getRequestURI(),
                    String.format("mid=%s",req.getParameter("mid"))});

            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(
                    command,
                    new HeaderInfo(new String[]{}));

            //String ID = resultFormat.resultInfo.getGeneratedId();
            //resp.sendRedirect(String.format("/collections/%d",Integer.parseInt(ID)));
            //resp.sendRedirect(req.getRequestURI());
            //TODO: get cid from resultInfo
            resp.sendRedirect("/collections");
            resp.setStatus(303);
        }catch (Exception e ){
            // TODO: 31/05/2016
            resp.setStatus(404);
        }

    }
}