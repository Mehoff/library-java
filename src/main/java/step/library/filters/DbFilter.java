package step.library.filters;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import step.library.filters.utils.Db;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@WebFilter("/*")
public class DbFilter implements Filter{
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Real file path - stores in Servlet Context
        String path =
                servletRequest
                .getServletContext().getRealPath("/WEB-INF/");

        final String itstepDbConfigPath = "configs//db.json";
        File config = new File(path + itstepDbConfigPath);

        if(config.exists()){
            // read file-config content to string;
            int len = (int) config.length();
            byte[] buf = new byte[len];
            try(InputStream reader = new FileInputStream(config)){
                if(len != reader.read(buf)){
                    throw new IOException("File integrity check error");
                }
                JSONObject configData = (JSONObject)
                        new JSONParser().parse(new String(buf));
                if(!Db.setConnection(configData)){
                    throw new SQLException("Db connection error!");
                }
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } catch (Exception ex){
                System.out.print("Error:\n" + ex.getMessage());
                return;
            }
        }

        System.out.print("Error");
    }

    public void destroy() {
        this.filterConfig = null;
    }
}