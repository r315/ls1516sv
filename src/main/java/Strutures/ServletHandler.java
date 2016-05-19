package Strutures;

/**
 * Created by Red on 18/05/2016.
 */


import java.io.IOException;

import console.MainApp;
import org.eclipse.jetty.server.Server;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHandler extends HttpServlet{

    private int LISTEN_PORT;
    private CommandMap commandMap;

    public ServletHandler(CommandMap commandMap, int port){
        this.LISTEN_PORT=port;
        this.commandMap=commandMap;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/plain; charset=%s",utf8.name()));
        String respBody=null;
        try{
            HeaderInfo headerInfo = new HeaderInfo(new String[]{req.getHeader("Accept")});
            CommandInfo command = new CommandInfo(new String[]{req.getMethod(),req.getRequestURI()});
            ResultInfo result = commandMap.get(command).execute(command.getData());
            //// TODO: 18/05/2016  
            IResultFormat resultFormat= MainApp.commandMap.getResponseMethod(headerInfo.getHeadersMap());
            resultFormat.generate(result,headerInfo.getHeadersMap());
        }catch(Exception e){
            e.getMessage();
        }
        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setStatus(200);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}
