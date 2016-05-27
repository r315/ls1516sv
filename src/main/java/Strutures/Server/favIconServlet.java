package Strutures.Server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by Red on 24/05/2016.
 */
public class favIconServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("image/png; charset=%s",utf8.name()));
        resp.setStatus(404);
    }
}
