package Strutures.Server;

/**
 * Created by Red on 18/05/2016.
 */


import java.io.IOException;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.IResultFormat;
import console.Manager;

import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExampleServlet extends HttpServlet{

    public ExampleServlet(){}

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        resp.setStatus(200);
        String respBody=null;
        try{
            String  header= req.getHeader("Accept");
            String method= req.getMethod();
            String URI= req.getRequestURI();
            String query= req.getQueryString();
            //HeaderInfo headerInfo = new HeaderInfo(new String[]{req.getHeader("Accept")});
            HeaderInfo headerInfo = new HeaderInfo(new String[]{});
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(),req.getRequestURI()});
            IResultFormat resultFormat= Manager.executeCommand(command,headerInfo);
            respBody = resultFormat.generate();


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
