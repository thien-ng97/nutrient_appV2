package loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import dbManager.DatabaseManager;


// With use of AI
public class NutrientAmountLoader {
	 public void load(String filePath) {
	        try (
	            BufferedReader br = new BufferedReader(new FileReader(filePath));
	            Connection conn = DatabaseManager.getInstance().getConnection();
	        ) {
	            String line = br.readLine(); // Skip header
	            String sql = "INSERT INTO nutrient_data (food_id, nutrient_id, nutrient_value) VALUES (?, ?, ?)";
	            PreparedStatement stmt = conn.prepareStatement(sql);

	            while ((line = br.readLine()) != null) {
	                String[] parts = line.split(",", -1);
	                int foodId = Integer.parseInt(parts[0].trim());
	                int nutrientId = Integer.parseInt(parts[1].trim());
	                double value = Double.parseDouble(parts[2].trim());

	                stmt.setInt(1, foodId);
	                stmt.setInt(2, nutrientId);
	                stmt.setDouble(3, value);
	                stmt.executeUpdate();
	            }

	            System.out.println("Nutrient data loaded.");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        String path = "/Users/thiennguyen/Downloads/cnf-fcen-csv/NUTRIENT AMOUNT.csv";
	        new NutrientAmountLoader().load(path);
	    }
}