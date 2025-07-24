package loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import dbManager.DatabaseManager;

public class MasterDataLoader {

    private static final String CSV_FILE_PATH = "/Users/thiennguyen/Desktop/EECS 3311 - Marios/Group Project/MASTERDATA.csv";
    private static final int BATCH_SIZE = 500;

    public static void main(String[] args) {
        String sql = "INSERT INTO master_data (" +
                "food_id, food_description, nutrient_id, nutrient_name, nutrient_value, " +
                "food_group_id, food_group_name, measure_id, measure_description, " +
                "conversion_factor_value, nutrient_amount, nutrient_unit" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))
        ) {
            String line;
            int count = 0;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (tokens.length != 12) continue;

                try {
                    stmt.setInt(1, Integer.parseInt(tokens[0].trim()));                    // food_id
                    stmt.setString(2, tokens[1].trim().replace("\"", ""));                 // food_description
                    stmt.setInt(3, Integer.parseInt(tokens[2].trim()));                    // nutrient_id
                    stmt.setString(4, tokens[3].trim().replace("\"", ""));                 // nutrient_name
                    stmt.setDouble(5, Double.parseDouble(tokens[4].trim()));               // nutrient_value
                    stmt.setInt(6, Integer.parseInt(tokens[5].trim()));                    // food_group_id
                    stmt.setString(7, tokens[6].trim().replace("\"", ""));                 // food_group_name
                    stmt.setInt(8, Integer.parseInt(tokens[7].trim()));                    // measure_id
                    stmt.setString(9, tokens[8].trim().replace("\"", ""));                 // measure_description
                    stmt.setDouble(10, Double.parseDouble(tokens[9].trim()));              // conversion_factor_value
                    stmt.setDouble(11, Double.parseDouble(tokens[10].trim()));             // nutrient_amount
                    stmt.setString(12, tokens[11].trim().replace("\"", ""));               // nutrient_unit

                    stmt.addBatch();
                    count++;

                    if (count % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                    }

                } catch (NumberFormatException e) {
                    // Skip malformed rows
                    continue;
                }
            }

            stmt.executeBatch();
            System.out.println("Master data loaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
