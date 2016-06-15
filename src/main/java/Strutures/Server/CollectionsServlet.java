package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.IResultFormat;
import console.Manager;
import org.eclipse.jetty.client.HttpRequest;
import org.eclipse.jetty.servlet.ServletHandler;
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
import java.util.*;

/**
 * Created by Red on 28/05/2016.
 */
public class CollectionsServlet extends HttpServlet{

    private static final Logger _logger = LoggerFactory.getLogger(CollectionsServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody;
        try{
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();

            _logger.info("New GET was received:" + path + (query == null ? "" : query));

            if (query == null) query = "top=5";
            else if (!query.contains("top=")) query += "&top=5";

            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{method,path,query});
            HtmlResult resultFormat= (HtmlResult)Manager.executeCommand(command,headerInfo);
            produceTemplate(resultFormat,query);


            respBody=resultFormat.getHtml();
        }catch(Exception e){
            //// TODO: 19/05/2016
            _logger.error("Exception:" + e.getMessage());
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

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));

        try {
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(),
                    req.getRequestURI(),
                    String.format("name=%s&description=%s",
                            req.getParameter("name"),
                            req.getParameter("description"))});

            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(
                    command,
                    new HeaderInfo(new String[]{}));

            String ID = resultFormat.resultInfo.getGeneratedId();

            _logger.info("New POST fulfilled:" + String.format("/collections/%d",Integer.parseInt(ID)));

            resp.sendRedirect(String.format("/collections/%d",Integer.parseInt(ID)));
            resp.setStatus(303);
        }catch (Exception e ){
            // TODO: 31/05/2016
            _logger.error("Exception:" + e.getMessage());
            resp.setStatus(404);
        }
    }

    private void produceTemplate(HtmlResult resultFormat, String query) {
        //Add collections links to each column
        List<Pair<String,String>> pairs=new ArrayList<>();
        for (ArrayList<String> line : resultFormat.resultInfo.getValues())
            pairs.add(new Pair<>(line.get(1),"/collections/"+line.get(0)));

        //Remove id column
        List<String> list= resultFormat.resultInfo.removeColumn("ID");

        resultFormat.generate();

        //Set Navigation links
        resultFormat.addNavigationLinks(
                Arrays.asList(
                        new Pair<>("Home","/")
                )
        );

        resultFormat.addLinksToTable(pairs);

        resultFormat.addPaging(Utils.paging(query, "/collections"));

        resultFormat.addForm("Add Collection"
                ,Arrays.asList(new Pair("method","POST"),new Pair("action","/collections"))
                ,Arrays.asList(
                        new Pair("Collection title",Arrays.asList(new Pair("name","name"),new Pair("type","text"),new Pair("required",null))),
                        new Pair("Description ",Arrays.asList(new Pair("name","description"),new Pair("type","text"),new Pair("required",null)))
                )
        );
    }

}
