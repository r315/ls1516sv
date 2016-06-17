package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Red on 28/05/2016.
 */
public class TopsRatingsReviewsServlet extends HttpServlet{

    private static final Logger _logger = LoggerFactory.getLogger(TopsRatingsReviewsServlet.class);

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
            HtmlResult resultFormat= (HtmlResult) Manager.executeCommand(command,headerInfo);

            //Add collections links to each column
            List<Pair<String,String>> pairs=new ArrayList<>();

            for (ArrayList<String> line : resultFormat.resultInfo.getValues())
                pairs.add(new Pair<>(line.get(1),"/movies/"+line.get(0)));

            //Remove id column
            List<String> list= resultFormat.resultInfo.removeColumn("ID");

            resultFormat.generate();

            //Set Navigation links
            resultFormat.addNavigationLinks(
                    Arrays.asList(
                            new Pair<>("Home", "/"),
                            new Pair<>("Top Ratings", "/tops/ratings")
                    )
            );

            resultFormat.addLinksToTable(pairs);

            respBody=resultFormat.getHtml();
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
