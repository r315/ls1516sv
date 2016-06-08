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
public class MoviesMidReviewsServlet extends HttpServlet {
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

            produceTemplate(resultFormat, Optional.empty());

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getParameterMap().forEach((k,v)-> {
            System.out.print("key: "+k);
            for (String s: v)
                System.out.println(" Value: "+s);
        });


        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(303);
            resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
            resp.setStatus(303);

            try {
                String method = req.getMethod();
                String path = req.getRequestURI();
                String query = req.getQueryString();
                Map<String, String[]> m= req.getParameterMap();
                //resp.getHeaderNames().forEach(s-> System.out.println(s));
                HeaderInfo headerInfo = new HeaderInfo(new String[]{});
                CommandInfo command = new CommandInfo(new String[]{method, path, query});
                //System.out.println(m.get("title")[0]);
                //System.out.println(m.get("releaseYear")[0]);
                String params= String.format("title=%s&releaseYear=%s",m.get("title")[0],m.get("releaseYear")[0]);
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);
            String ID= resultFormat.resultInfo.getValues().iterator().next().get(0);
            resp.sendRedirect(String.format("/movies/%d",Integer.parseInt(ID)));
        }catch (Exception e ){
            // TODO: 31/05/2016
            resp.setStatus(404);
        }

    }

    private void produceTemplate(HtmlResult resultFormat, Optional<String> errorMessage) throws Exception {

        ArrayList<String> values = resultFormat.resultInfo.getValues().iterator().next();
        String mid = values.get(0);

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

        resultFormat.addForm("Legend"
                ,Arrays.asList(new Pair("method","POST"),new Pair("action","/movies/"+mid+"/reviews"))
                ,Arrays.asList(
                        new Pair("Reviewer's Name",Arrays.asList(new Pair("name","name"),new Pair("type","text"),new Pair("required",null))),
                        new Pair("Rating",Arrays.asList(new Pair("name","rating"),new Pair("type","text"),new Pair("required",null))),
                        new Pair("Review's Summary",Arrays.asList(new Pair("name","summary"),new Pair("type","text"),new Pair("required",null))),
                        new Pair("Review",Arrays.asList(new Pair("name","review"),new Pair("type","text"),new Pair("required",null)))
                )
        );

    }
}