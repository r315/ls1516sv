package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.IResultFormat;
import console.Manager;
import org.eclipse.jetty.servlet.ServletHandler;
import utils.Pair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Red on 28/05/2016.
 */
public class CollectionsServlet extends HttpServlet{

    public CollectionsServlet(){}

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody=null;
        try{
            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(),req.getRequestURI()});
            HtmlResult resultFormat= (HtmlResult)Manager.executeCommand(command,headerInfo);
            resultFormat.generate();

            //Set Navigation links
            resultFormat.addNavigationLinks(
                Arrays.asList(
                    new Pair<>("Home","/")
                )
            );

            //Remove id column
            List<String> list= resultFormat.resultInfo.removeColumn("id");

            //Add collections links to each column
            List<Pair<String,String>> pairs=new ArrayList<>();
            for (String s: list)
                pairs.add(new Pair<>("name","/collections/"+s));
            resultFormat.addLinksToTable(pairs);

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
