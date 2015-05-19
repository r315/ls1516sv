package pt.isel.ls.apps.http;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.*;

/**
 * Created by pedro on 19/05/2015.
 */
public class StudentsServlet extends HttpServlet {

    private final SQLServerDataSource _ds;

    private String getFromEnv(String name)  {
        String value = System.getenv(name);
        if(value == null){
            throw new RuntimeException ("No value found for environment variable "+name);
        }
        return value;
    }

    public StudentsServlet() {
        _ds = new SQLServerDataSource();
        _ds.setServerName(getFromEnv("dbserver"));
        _ds.setUser(getFromEnv("dbuser"));
        _ds.setPassword(getFromEnv("dbpassword"));
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/plain; charset=%s",utf8.name()));
        byte[] respBodyBytes;

        try(Connection c = _ds.getConnection()){
            PreparedStatement stat = c.prepareStatement("select * from Students");
            ResultSet rs = stat.executeQuery();
            StringBuffer sb = new StringBuffer();
            while(rs.next()){
                int number = rs.getInt("number");
                String name = rs.getString("name");
                sb.append(String.format("%d, %s\n", number, name));
            }
            respBodyBytes = sb.toString().getBytes(utf8);
            resp.setStatus(200);

        } catch (SQLException e) {
            resp.setStatus(500);
            respBodyBytes = e.getMessage().getBytes(utf8);
        }
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}

