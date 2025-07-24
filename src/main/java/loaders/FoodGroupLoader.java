package loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import dbManager.DatabaseManager;

public class FoodGroupLoader {
	public void load(String filePath) {
        try (
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            Connection conn = DatabaseManager.getInstance().getConnection();
        ) {
            String line = br.readLine(); // Skip header
            String sql = "INSERT INTO food_groups (food_group_id, food_group_name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                int groupId = Integer.parseInt(parts[0].trim());
                String groupName = parts[1].trim().replace("\"", "");

                stmt.setInt(1, groupId);
                stmt.setString(2, groupName);
                stmt.executeUpdate();
            }

            System.out.println("Food groups loaded.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		String path = "/Users/thiennguyen/Downloads/cnf-fcen-csv/FOOD GROUP.csv";
		new FoodGroupLoader().load(path);
	}
}
