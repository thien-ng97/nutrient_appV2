package dbManager;

import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            String url = "jdbc:mysql://root:LcfdIhCCmSEmknmYCPhoqoarFndkOHLL@turntable.proxy.rlwy.net:11982/railway";
            String user = "root"; // 
            String password = "LcfdIhCCmSEmknmYCPhoqoarFndkOHLL";

            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }

        try {
            if (instance.connection.isClosed()) {
                instance = null;
                return getInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    
    
}

