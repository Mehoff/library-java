package step.library.utils;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;

public class Db {

    final private static String SUFFIX = "_11";
    private static JSONObject config;
    private static Connection connection;
    private static BookOrm bookOrm;

    public static BookOrm getBookOrm() {
        if(bookOrm == null){
            bookOrm = new BookOrm(connection, SUFFIX, config);
        }
        return bookOrm;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static boolean setConnection(JSONObject json) {

        if(json == null){
            connection = null;
            return false;
        }
        String connectionString ;

        try {
            String dbms = json.getString("dbms");
            if (dbms.equalsIgnoreCase("oracle")) {
                config = json;

                connectionString = String.format (
                        "jdbc:oracle:thin:%s/%s@%s:%s:XE",
                        json.get( "user" ),
                        json.get( "pass" ),
                        json.get( "host" ),
                        json.get( "port" )
                ) ;

                DriverManager.registerDriver(
                        new oracle.jdbc.driver.OracleDriver()
                );
                connection = DriverManager.getConnection(connectionString);

                return true;
            } else {
                System.err.println("Db: Unsupported DBMS");
            }
        } catch (Exception ex) {
            System.err.println("Db exception: \n" + ex.getMessage());
            connection = null;
            config = null;
            return false;
        }
        return false;
    }

    public static void closeConnection() {
        if(connection != null){
            try{
                connection.close();
            }
            catch (Exception ex){
                System.err.println(ex.getMessage());
            }
        }
    }

//    public static void createAuthors() {
//        if( connection == null ) return ;
//        String query = null ;
//        try( Statement statement = connection.createStatement() ) {
//            query = "CREATE TABLE Authors" + SUFFIX +
//                    "(Id         RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, " +
//                    " Name       NVARCHAR2(256) NOT NULL )";
//            statement.executeUpdate( query ) ;
//        } catch( Exception ex ) {
//            System.err.println(
//                    "createAuthors: " + ex.getMessage() + " " + query ) ;
//        }
//    }

//    public static void createBooks() {
//        if( connection == null ) return ;
//        String query = null ;
//        try( Statement statement = connection.createStatement() ) {
//            query = "CREATE TABLE Books" + SUFFIX +
//                    "(Id          RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, " +
//                    " Title       NVARCHAR2(256) NOT NULL, " +
//                    " Author NVARCHAR2(256) NULL, " +
//                    " CONSTRAINT fk_Author FOREIGN KEY (Id) REFERENCES Authors" + SUFFIX + "(Id))";
//            statement.executeUpdate( query ) ;
//        } catch( Exception ex ) {
//            System.err.println(
//                    "createBooks: " + ex.getMessage() + " " + query ) ;
//        }
//    }

}
