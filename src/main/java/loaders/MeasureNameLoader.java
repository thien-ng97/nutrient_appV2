package loaders;

import com.opencsv.CSVReader;

import dbManager.DatabaseManager;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MeasureNameLoader {
	public void load(String filePath) {
        try (
            CSVReader reader = new CSVReader(new FileReader(filePath));
            Connection conn = DatabaseManager.getInstance().getConnection();
        ) {
            String[] line;
            reader.readNext(); // Skip header
            String sql = "INSERT INTO measure_names (measure_id, measure_description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            while ((line = reader.readNext()) != null) {
                int id = Integer.parseInt(line[0].trim());
                String desc = line[1].trim();

                stmt.setInt(1, id);
                stmt.setString(2, desc);
                stmt.executeUpdate();
            }

            System.out.println("Measure names loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void main(String[] args) {
        new MeasureNameLoader().load("/Users/thiennguyen/Downloads/cnf-fcen-csv/MEASURE NAME.csv");
    }
}
