package loaders;

import com.opencsv.CSVReader;

import dbManager.DatabaseManager;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConversionFactorLoader {
	public void load(String filePath) {
        try (
            CSVReader reader = new CSVReader(new FileReader(filePath));
            Connection conn = DatabaseManager.getInstance().getConnection();
        ) {
            String[] line;
            reader.readNext(); // Skip header
            String sql = "INSERT INTO conversion_factors (food_id, measure_id, conversion_factor_value) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            while ((line = reader.readNext()) != null) {
                int foodId = Integer.parseInt(line[0].trim());
                int measureId = Integer.parseInt(line[1].trim());
                double value = Double.parseDouble(line[2].trim());

                stmt.setInt(1, foodId);
                stmt.setInt(2, measureId);
                stmt.setDouble(3, value);
                stmt.executeUpdate();
            }

            System.out.println("Conversion factors loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ConversionFactorLoader().load("/Users/thiennguyen/Downloads/cnf-fcen-csv/CONVERSION FACTOR.csv");
    }
}
