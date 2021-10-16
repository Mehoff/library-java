package step.library.utils;

import org.json.JSONObject;
import step.library.models.Book;

import java.sql.*;

public class BookOrm {

    private Connection connection;
    private String SUFFIX;
    private JSONObject config;
    public BookOrm(Connection connection, String SUFFIX, JSONObject config){
        this.connection = connection;
        this.SUFFIX = SUFFIX;
        this.config = config;
    }


    public boolean isTableExists(String tableName){

        final String query = "SELECT COUNT(*) FROM USER_TABLES T WHERE T.TABLE_NAME = '" + tableName + SUFFIX + "'";
        try(
            ResultSet res = connection
                    .createStatement()
                    .executeQuery(query))
        {
            if(res.next()){
                return res.getInt(1) == 1;
            }
        } catch (SQLException ex){
            System.err.println("BookOrm.isTableExists(" + tableName + "): "
                    + ex.getMessage());
            return false;
        }

        return false;
    }

    public Book[] getList() {
        if( connection == null ) return null ;
        String query, queryCount ;
        String dbms = config.getString( "dbms" ) ;
        if( dbms.equalsIgnoreCase( "Oracle")
                || dbms.equalsIgnoreCase( "MySQL" ) )
        {
            queryCount = "SELECT COUNT(*) FROM BOOKS" + SUFFIX;
            query = "SELECT B.ID, B.AUTHOR, B.TITLE, B.DESCRIPTION, B.COVER FROM BOOKS" + SUFFIX + " B";
        } else {
            return null ;
        }
        try( Statement statement = connection.createStatement() ) {
            ResultSet res = statement.executeQuery( queryCount ) ;
            res.next();
            int cnt = res.getInt( 1 ) ;
            res.close() ;
            res = statement.executeQuery( query ) ;
            Book[] ret = new Book[ cnt ] ;
            for( int i = 0; i < cnt; i++ ) {
                res.next() ;
                ret[ i ] = new Book(
                        res.getString( 1 ),
                        res.getString( 2 ),
                        res.getString( 3 ),
                        res.getString( 4 ),
                        res.getString( 5 )
                ) ;
            }
            //System.out.print(ret);
            return ret ;
        } catch( SQLException ex ) {
            System.err.println(
                    "BookOrm.getList: " + ex.getMessage() + "\n" + query ) ;
        }
        return null ;
    }

    public boolean pushToDb(Book book){
        if(connection == null) return false;
        String query = "INSERT INTO BOOKS" + SUFFIX + "(" +
                "author, title, description, cover) VALUES (?,?,?,?)";

        try(PreparedStatement prep = connection.prepareStatement(query)) {
            prep.setString(1, book.getAuthor());
            prep.setString(2, book.getTitle());
            prep.setString(3, book.getDescription());
            prep.setString(4, book.getCover());

            prep.executeUpdate();
            return true;
        } catch (SQLException ex){
            System.err.println(
                    "pushToDb: " + ex.getMessage() + " " + query ) ;
            return false;
        }
    }

    public boolean install() {
        String query ;
        String dbms = config.getString( "dbms" ) ;
        if( dbms.equalsIgnoreCase( "Oracle") ) {
            query = "CREATE TABLE BOOKS" + SUFFIX + "(" +
                    "id       RAW(16) DEFAULT SYS_GUID() PRIMARY KEY," +
                    "author   NVARCHAR2(128) NOT NULL," +
                    "title    NVARCHAR2(128) NOT NULL," +
                    "description    NVARCHAR2(128) NULL," +
                    "cover    NVARCHAR2(128) NULL)" ;
        } else {
            return false ;
        }
        try( Statement statement = connection.createStatement() ) {
            statement.executeUpdate( query ) ;
            return true ;
        } catch( SQLException ex ) {
            System.err.println(
                    "BookOrm.installTable: " + ex.getMessage() + "\n" + query ) ;
        }
        return false ;
    }
}
