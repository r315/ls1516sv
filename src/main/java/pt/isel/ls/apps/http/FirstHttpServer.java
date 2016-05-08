package pt.isel.ls.apps.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class FirstHttpServer {

    /* 
     * TCP port where to listen. 
     * Standard port for HTTP is 80 but might be already in use
     */
    private static final int LISTEN_PORT = 8080;    
    
        
   public static void main(String[] args) throws Exception {
    	
    	Server server = new Server(LISTEN_PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(TimeServlet.class, "/*");
        server.start();
        System.out.println("Server is started");
        
        System.in.read();
        server.stop();
        System.out.println("Server is stopped, bye");        
    }    
}
