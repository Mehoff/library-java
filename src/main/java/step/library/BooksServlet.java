package step.library;

import org.json.JSONArray;
import org.json.JSONObject;
import step.library.models.Book;
import step.library.utils.BookOrm;
import step.library.utils.Db;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@WebServlet("/books-servlet")
@MultipartConfig
public class BooksServlet extends HttpServlet {

    // added last \\
    final String devFolder = "E:\\Code\\Repositories\\LibraryWeb\\web\\uploads\\";

    // fetch data from front-end end send data to db
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String author = req.getParameter("author");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        Part   cover = req.getPart("cover");

        if(!addBook(author, title, description, cover)){
            resp.sendError(403, "Data validation failed");
            return;
        }
        resp.sendRedirect(req.getRequestURI());
    }

    private boolean addBook(String author, String title, String description, Part cover){
        if( author == null || author.length() < 2 ) {
            System.out.println("VALIDATION DATA ERROR: Author");
            return false;
        } else if( title == null || title.length() < 2 ) {
            System.out.println("VALIDATION DATA ERROR: title");
            return false;
        } else if( cover.getSize() == 0 ) {
            System.out.println("VALIDATION DATA ERROR: cover");
            return false;
        } else if(description == null || description.length() < 2){
            System.out.println("VALIDATION DATA ERROR: description");
            return false;
        } else {
            String savedName = moveUploadedFile( cover, true ) ;
            if( savedName == null ) {
                System.out.println("VALIDATION DATA ERROR: Cover");
                return false;
            } else {
                if( Db.getBookOrm().pushToDb( new Book(
                        author,
                        title,
                        description,
                        savedName
                ) ) ) {
                    return true;
                } else {
                    System.out.println("SAVING BOOK ERROR");
                    return false;
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.lineSeparator());
            }
            JSONObject json = new JSONObject(buffer.toString());
            String id = json.getString("id");

            if(id == null) {
                resp.sendError(403, "Id is null");
                return;
            }

            if(!Db.getBookOrm().deleteById(id)){
                resp.sendError(403, "Failed to delete book");
                return;
            }

            resp.setStatus(200);
            resp.setContentType("application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.append("status", "OK");
            resp.getWriter().print(
                    jsonObject
            );
        } catch (Exception ex){
            resp.sendError(403, "Failed to delete");
            return;
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        resp.setContentType("application/json");
//        resp.getWriter().print(
//            new JSONArray(
//                    Db.getBookOrm().getList()
//            ).toString()
//        );

        req
                .getRequestDispatcher("addBook.jsp")
                .forward(req, resp);
    }

    private String moveUploadedFile( Part filePart, boolean makeDevCopy ) {
        if( filePart.getSize() == 0 ) {
            System.err.println( "moveUploadedFile: size - 0");
            return null ;
        }
        String hostingFolder =
                this.getServletContext().getRealPath("/uploads") + "/" ;
//        String devFolder = "C:\\Users\\samoylenko_d\\IdeaProjects\\Library\\web\\uploads\\" ;

        String uploadedFilename = null ;
        try {
            uploadedFilename = filePart.getSubmittedFileName() ;
        } catch( Exception ignored ) {
            String contentDisposition =
                    filePart.getHeader("content-disposition" ) ;
            if( contentDisposition != null ) {
                for( String part : contentDisposition.split( "; ") ) {
                    if( part.startsWith( "filename" ) ) {
                        uploadedFilename = part.substring( 10, part.length() - 1 ) ;
                        break ;
                    }
                }
            }
        }
        if( uploadedFilename == null ) {
            System.err.println( "moveUploadedFile: filename extracting error" ) ;
            return null ;
        }
        // TODO: trim filename length to 128
        int extPosition =  uploadedFilename.lastIndexOf( "." ) ;
        if( extPosition == -1 ) {
            System.err.println( "moveUploadedFile: filename without extension" ) ;
            return null ;
        }
        String fileExtension = uploadedFilename.substring( extPosition ) ;
        String initFileName = uploadedFilename.substring( 0, extPosition ) ;
        String fileName ;
        int counter = 1 ;
        File file ;
        do {
            fileName = "_" + initFileName + "(" + counter + ")" + fileExtension ;
            file = new File( hostingFolder + fileName ) ;
            ++counter ;
        } while( file.exists() ) ;
        try {
            Files.copy(
                    filePart.getInputStream(),
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            ) ;
            if( makeDevCopy ) {
                Files.copy(
                        filePart.getInputStream(),
                        new File( devFolder + fileName ).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                ) ;
            }
        } catch( IOException ex ) {
            System.err.println( "moveUploadedFile exception: " + ex.getMessage() ) ;
            return null ;
        }
        return fileName ;
    }
}
