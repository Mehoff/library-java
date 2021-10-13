package step.library.filters;



import org.json.JSONObject;

import step.library.filters.utils.Db;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        System.out.println("DO FILTER");

        String path =
                servletRequest
                .getServletContext().getRealPath("/WEB-INF/");

        File config = new File(path + "configs//localdb.json");

        if(config.exists()){
            int len = (int) config.length();
            byte[] buf = new byte[len];
            try(InputStream reader = new FileInputStream(config)){
                if(len != reader.read(buf)){
                    throw new IOException("File integrity check error");
                }
                JSONObject configData = new JSONObject(new String(buf));
                if(!Db.setConnection(configData)){
                    // Отобразить статическую страницу
                    // throw new SQLException("Db connection error!");
                    servletRequest
                            .getRequestDispatcher("/static.jsp")
                            .forward(servletRequest, servletResponse);
                }
                if(Db.getBookOrm().isTableExists()){
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else{
                    servletRequest
                            .getRequestDispatcher("/static.jsp")
                            .forward(servletRequest, servletResponse);
                }
                return;
            } catch (Exception ex){
                System.out.print("Error:\n" + ex.getMessage());
                return;
            }
        } else {
            System.out.print("Error");
        }

    }

    public void destroy() {
        this.filterConfig = null;
    }
}