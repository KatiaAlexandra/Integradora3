package mx.edu.utez.sgi.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection getConnection() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
           return DriverManager.getConnection("jdbc:mysql://sgi.cyodzozkjmfc.us-east-1.rds.amazonaws.com:3306/Inventory", "admin", "Elviasan1061271$");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        DBConnection con = new DBConnection();
        con.getConnection();
    }
}