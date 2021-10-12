package step.library.filters.utils;

import org.json.simple.JSONObject;

import java.sql.Connection;

public class Db {

    private static JSONObject config = null;
    private static Connection connection = null;

    public static Connection getConnection(){
        return connection;
    }
    public static boolean setConnection (JSONObject json){
        try{
            return true;
        } catch (Exception ex){
            connection = null;
            config = null;
            return false;
        }
    }
}
