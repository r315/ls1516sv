package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.IResultFormat;
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
            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{method,path});
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
}