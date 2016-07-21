package Strutures.Server;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import console.Manager;
import exceptions.InvalidCommandException;
import exceptions.PostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Utils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by Red on 16/07/2016.
 */
public class Servlet extends HttpServlet {
    private static final Logger _logger = LoggerFactory.getLogger(Servlet.class);
    private static final int HTTP_OK = 200;
    private static final int HTTP_REDIRECT = 303;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_INTERNAL_ERROR = 500;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String respBody;
        String method= req.getMethod();
        String path= req.getRequestURI();
        String query= req.getQueryString();

        query = addPagingToQuery(query);

        try{
            respBody = Manager.executeCommand(new CommandInfo(method, path, query), new HeaderInfo());
            InfoStatusCode(resp, HTTP_OK, String.format("New GET was received: %s?%s",path,query));
        }catch(Exception e){
            if(e instanceof InvalidCommandException){
                respBody=ErrorStatusCode(resp, HTTP_NOT_FOUND, e.getMessage());
            }else{
                respBody=ErrorStatusCode(resp, HTTP_INTERNAL_ERROR, e.getMessage());
            }
        }
        doResponse(resp,respBody);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String respBody;
        try {
            String method= req.getMethod();
            String path= req.getRequestURI();
            String query= Utils.decodeParametersMap(req.getParameterMap());
            CommandInfo command = new CommandInfo(method,path,query);
            String redirect_path= Manager.executeCommand(command, new HeaderInfo());
            InfoStatusCode(resp, HTTP_REDIRECT, String.format("New POST fulfilled: %s?%s",path,query));
            resp.sendRedirect(redirect_path);
        }catch(Exception e) {
            if(e instanceof PostException || e instanceof InvalidCommandException){
                respBody=ErrorStatusCode(resp, HTTP_BAD_REQUEST, e.getMessage());
            }else{
                respBody=ErrorStatusCode(resp, HTTP_INTERNAL_ERROR, e.getMessage());
            }
            doResponse(resp,respBody);
        }
    }

    private String ErrorStatusCode(HttpServletResponse resp, int status, String error){
        resp.setStatus(status);
        _logger.error(error);
        return String.format("Error %d - ",status)+error;
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

    private static String addPagingToQuery(String query) {
        if (query == null) query = String.format("top=%d", Utils.PAG_DEFAULT);
        else if (!query.contains("top=")) query += String.format("&top=%d",Utils.PAG_DEFAULT);
        return query;
    }
}
