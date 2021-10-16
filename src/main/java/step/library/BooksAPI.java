package step.library;

import org.json.JSONObject;
import step.library.models.Book;
import step.library.utils.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/books-api")
public class BooksAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        try{
            if(id == null) {
                resp.sendError(403, "Id is null");
                return;
            }

            Book book = Db.getBookOrm().getBookById(id);

            if(book == null){
                resp.sendError(403, "Failed to delete book");
                return;
            }

            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", book.getId());
            jsonObject.put("author", book.getAuthor());
            jsonObject.put("title", book.getTitle());
            jsonObject.put("description", book.getDescription());
            jsonObject.put("cover", book.getCover());

            resp.getWriter().print(jsonObject.toString());
            resp.flushBuffer();

        } catch (Exception ex){
            resp.sendError(403, "Failed to delete");
            return;
        }

    }
}
