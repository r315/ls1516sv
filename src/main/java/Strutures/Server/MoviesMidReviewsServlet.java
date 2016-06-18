package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqlserver.ConnectionFactory;
import utils.Pair;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Luigi Sekuiya on 28/05/2016.
 */
public class MoviesMidReviewsServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(MoviesMidReviewsServlet.class);

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
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);

            if (resultFormat.resultInfo.getValues().isEmpty()){
                resp.setStatus(404);
                respBody="Error 404.";
            } else {
                produceTemplate(resultFormat, query, command, Optional.empty());

                respBody = resultFormat.getHtml();
            }

            //// TODO: 25/05/2016
        }catch(Exception e){
            //// TODO: 19/05/2016
            _logger.error("Exception:" + e.getClass().getName() + " | " +  e.getMessage());
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

            _logger.info("New POST fulfilled:" + String.format("/movies/%d/reviews/%d",Integer.parseInt(mid),Integer.parseInt(rid)));

            resp.sendRedirect(String.format("/movies/%d/reviews/%d",Integer.parseInt(mid),Integer.parseInt(rid)));
        }catch (Exception e ){
            // TODO: 31/05/2016
            resp.setStatus(404);
        }

    }

    private void produceTemplate(HtmlResult resultFormat, String query, CommandInfo command, Optional<String> errorMessage) throws Exception {

        ArrayList<String> values;

        if (resultFormat.resultInfo.getValues().iterator().hasNext()) values = resultFormat.resultInfo.getValues().iterator().next();
        else {values = getInfo(command, resultFormat.resultInfo);}
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

        resultFormat.addPaging(Utils.paging(query, String.format("/movies/%s/reviews",mid)));

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

    private ArrayList<String> getInfo(CommandInfo command, ResultInfo resultInfo) {
        HashMap<String, String> param = command.getData();
        ArrayList<String> info = new ArrayList<>();

        info.add(param.get("mid"));

        Pattern p = Pattern.compile("^(.*?)'s Reviews");
        Matcher m = p.matcher(resultInfo.getDisplayTitle());

        if (m.find()) info.add(m.group(1));

        return info;
    }
}