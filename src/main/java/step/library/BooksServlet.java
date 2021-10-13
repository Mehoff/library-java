package step.library;

import step.library.models.Book;
import step.library.utils.BookOrm;
import step.library.utils.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/books-servlet")
public class BooksServlet extends HttpServlet {

    // fetch data from front-end end send data to db
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("BooksServlet: doPOST");

        String author = req.getParameter("author");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        //Part filePart = req.getPart("cover");

        System.out.println(author);
        System.out.println(title);
        System.out.println(description);

        Book book = new Book(author, title, description, null);
        Db.getBookOrm().pushToDb(book);
        resp.sendRedirect(req.getRequestURI());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("BooksServlet: doGet");
        req
                .getRequestDispatcher("addBook.jsp")
                .forward(req, resp);
    }
}
