package step.library.filters.utils;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookOrm {

    private Connection connection;
    private String SUFFIX;
    private JSONObject config;
    public BookOrm(Connection connection, String SUFFIX, JSONObject config){
        this.connection = connection;
        this.SUFFIX = SUFFIX;
        this.config = config;
    }


    public boolean isTableExists(){

        final String query = "SELECT COUNT(*) FROM USER_TABLES T WHERE T.TABLE_NAME = 'Books" + SUFFIX + "'";
        try(
            ResultSet res = connection
                    .createStatement()
                    .executeQuery(query))
        {
            if(res.next()){
                return res.getInt(1) == 1;
            }
        } catch (SQLException ex){
            System.err.println("BookOrm.isTableExists: "
                    + ex.getMessage());
            return false;
        }
    }
}
