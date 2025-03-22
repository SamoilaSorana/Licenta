package main.Sistem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static final String url = "jdbc:mariadb://localhost:3306/conturi";
    private static final String username = "root";
    private static final String password = "1234";
    private static Connection conn = null;
    public static Connection GetInfo()
    {
        if(conn == null)
        {
            try{
                conn = DriverManager.getConnection(url,username,password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
