package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import console.Manager;
import exceptions.PostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;

/**
 * Created by Red on 16/07/2016.
 */
public class Servlet extends HttpServlet {
    private static final Logger _logger = LoggerFactory.getLogger(Servlet.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String respBody;
        try{
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();
            CommandInfo command = new CommandInfo(method, path, query);
            respBody = Manager.executeCommand(command,new HeaderInfo());
            InfoStatusCode(resp, 200, "New GET was received:" + path + (query == null ? "" : query));
        }catch(Exception e){
            respBody=ErrorStatusCode(resp, 404, e.getMessage());
        }
        doResponse(resp,respBody);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String respBody;
        try {
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= req.getQueryString();
            CommandInfo command = new CommandInfo(method,path,query);
            String redirect_path= Manager.executeCommand(command, new HeaderInfo());
            InfoStatusCode(resp, 303, "New POST fulfilled:" + path + (query == null ? "" : query));
            resp.sendRedirect(redirect_path);
        }catch(Exception e) {
            if(e instanceof SQLException) {
                if(e instanceof PostException){
                    respBody=ErrorStatusCode(resp, 400, e.getMessage());
                }else {
                    respBody=ErrorStatusCode(resp, 500, e.getMessage());
                }
            }else{
                respBody=ErrorStatusCode(resp, 404, e.getMessage());
            }
            doResponse(resp,respBody);
        }
    }

    private String ErrorStatusCode(HttpServletResponse resp, int status, String error){
        resp.setStatus(status);
        _logger.error(error);
        return String.format("Error %i - ",status)+error;
    }

    private void InfoStatusCode(HttpServletResponse resp, int status, String info){
        resp.setStatus(status);
        _logger.info(info);
    }

    private void doResponse(HttpServletResponse resp, String page) throws IOException {
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        byte[] respBodyBytes = page.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}
