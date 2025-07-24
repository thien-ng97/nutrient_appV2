package loaders;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.opencsv.CSVReader;

import dbManager.DatabaseManager;


// With use of AI
public class FoodItemLoader {
    public void load(String filePath) {
        try (
            CSVReader reader = new CSVReader(new FileReader(filePath));
            Connection conn = DatabaseManager.getInstance().getConnection();
        ) {
            String[] line;
            reader.readNext(); // skip header
            String sql = "INSERT INTO food_items (food_id, food_group_id, food_name) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            while ((line = reader.readNext()) != null) {
                if (line.length < 3) continue;

                int foodId = Integer.parseInt(line[0].trim());
                int foodGroupId = Integer.parseInt(line[1].trim());
                String foodName = line[2].trim();

                stmt.setInt(1, foodId);
                stmt.setInt(2, foodGroupId);
                stmt.setString(3, foodName);
                stmt.executeUpdate();
            }

            System.out.println("Food items loaded successfully (comma-safe).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "/Users/thiennguyen/Downloads/cnf-fcen-csv/FOOD NAME.csv";
        new FoodItemLoader().load(path);
    }
}