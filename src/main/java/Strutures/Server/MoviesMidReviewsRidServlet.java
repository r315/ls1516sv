package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
import exceptions.InvalidCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pair;

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

/**
 * Created by Luigi Sekuiya on 28/05/2016.
 */
public class MoviesMidReviewsRidServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(MoviesMidReviewsRidServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody;
        try{
            String method= req.getMethod();
            String path= req.getRequestURI();

            _logger.info("New GET was received:" + path);

            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{method,path});
            HtmlResult resultFormat = (HtmlResult) Manager.executeCommand(command,headerInfo);

            ArrayList<String> values = resultFormat.resultInfo.getValues().iterator().next();

            Pair<String,String> pair1 = new Pair<>(values.get(1),"/movies/"+values.get(0));
            Pair<String,String> pair2 = new Pair<>(values.get(1)+"'s Reviews","/movies/"+values.get(0)+"/reviews");

            List<Pair<String,String>> pairs = new ArrayList<>();

            resultFormat.resultInfo.removeColumn("Movie's ID");
            resultFormat.resultInfo.removeColumn("Movie's Title");
            resultFormat.resultInfo.removeColumn("Review's ID");
            resultFormat.generate();
            resultFormat.addLinksToTable(pairs);

            resultFormat.addNavigationLinks(
                    Arrays.asList(
                            new Pair<>("Home","/"),
                            pair1,
                            pair2
                    )
            );

            respBody = resultFormat.getHtml();

        } catch (InvalidCommandException | SQLException e) {
            respBody = e.getMessage();
            resp.setStatus(500);
            _logger.error("Error:" + e.getClass().getName() + " | " + respBody);
        }

        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}
