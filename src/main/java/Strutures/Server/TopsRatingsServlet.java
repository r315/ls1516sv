package Strutures.Server;

import Strutures.ResponseFormat.Html.HtmlResult;
import utils.Pair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Luigi Sekuiya on 27/05/2016.
 */
public class TopsRatingsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("--New request was received --");
        System.out.println(req.getRequestURI());

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody;
        try{
            HtmlResult resultFormat = new HtmlResult();

            resultFormat.generate();
            resultFormat.addNavigationLinks(
                    Arrays.asList(
                        new Pair<>("Home","/"),
                        new Pair<>("Movies","/movies")
                    )
            );

            resultFormat.addList(
                    Arrays.asList(
                        new Pair<>("Ratings Higher Average","/tops/5/ratings/higher/average"),
                        new Pair<>("Ratings Lower Average","/tops/5/ratings/lower/average"),
                        new Pair<>("Review Higher Count","/tops/5/reviews/higher/count"),
                        new Pair<>("Review Lower Count","/tops/5/reviews/lower/count")
                    ),"Tops"
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
