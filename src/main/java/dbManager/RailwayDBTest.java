package dbManager;
import java.sql.*;

public class RailwayDBTest {
	public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://root:LcfdIhCCmSEmknmYCPhoqoarFndkOHLL@turntable.proxy.rlwy.net:11982/railway", "root", "LcfdIhCCmSEmknmYCPhoqoarFndkOHLL");
            System.out.println("Connected successfully!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
