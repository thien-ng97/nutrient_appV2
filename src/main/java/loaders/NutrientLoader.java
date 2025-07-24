package loaders;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.opencsv.CSVReader;

import dbManager.DatabaseManager;

public class NutrientLoader {

    public void load(String filePath) {
        try (
            CSVReader reader = new CSVReader(new FileReader(filePath));
            Connection conn = DatabaseManager.getInstance().getConnection();
        ) {
            String[] line;
            reader.readNext(); // skip header
            String sql = "INSERT INTO nutrients (nutrient_id, nutrient_name, unit) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            while ((line = reader.readNext()) != null) {
                int id = Integer.parseInt(line[0].trim());
                String name = line[1].trim();  // This will include commas inside quotes
                String unit = line[2].trim();

                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.setString(3, unit);
                stmt.executeUpdate();
            }

            System.out.println("Nutrient names loaded (with commas handled).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "/Users/thiennguyen/Downloads/cnf-fcen-csv/NUTRIENT NAME.csv";
        new NutrientLoader().load(path);
    }
}