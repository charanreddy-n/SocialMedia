package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
//    private static final String URL = "jdbc:mysql://localhost:3306/social_media";
//    private static final String USER = "root";
//    private static final String PASSWORD = "Charan@052";
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);


    public static Connection getConnection(){
        String url = "jdbc:mysql://localhost:3306/social_media";
        String user = "root";
        String password = "Charan@052";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,user,password);
            return conn;
        }catch (ClassNotFoundException | SQLException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
}

