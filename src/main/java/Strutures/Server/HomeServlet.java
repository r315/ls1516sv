package Strutures.Server;

import Strutures.ResponseFormat.Html.HtmlElement;
import Strutures.ResponseFormat.Html.HtmlResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Red on 27/05/2016.
 */
public class HomeServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        _logger.info("New GET was received:" + req.getRequestURI() + " - Home");

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody;
        try{
            HtmlResult resultFormat= new HtmlResult();
            resultFormat.generate();
            resultFormat.addNavigationLinks(
                    Arrays.asList(
                            new Pair<>("Home","/"),
                            new Pair<>("Movies","/movies"),
                            new Pair<>("Collections","/collections"),
                            new Pair<>("Top Ratings","/tops/ratings")
                    )
            );
            resultFormat.addElementTo("body",
                    new HtmlElement("div","João Duarte | Luís Almeida | Hugo Reis").
                            addAttributes("style","position:absolute;bottom:0;width:99%;text-align:center"));
            respBody= resultFormat.getHtml();
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
