package step.library.filters;
import org.json.JSONObject;

import step.library.utils.Db;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebFilter("/*")
public class DbFilter implements Filter{
    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        String req = ((HttpServletRequest) servletRequest).getRequestURI();

        for(String ext : new String[]{".css", ".js", ".jsp"})
        if(req.endsWith(ext)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

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
                    servletRequest
                            .getRequestDispatcher("/static.jsp")
                            .forward(servletRequest, servletResponse);
                    return;
                }
                if(Db.getBookOrm().isTableExists("BOOKS")){
                    System.out.println("Books exists");
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                } else {
                    servletRequest
                            .getRequestDispatcher("/install.jsp")
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